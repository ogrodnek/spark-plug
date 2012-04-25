package com.bizo.hive.sparkplug.emr

import com.amazonaws.services.elasticmapreduce.model._
import scala.collection.JavaConversions._
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient
import com.amazonaws.auth.AWSCredentials
import com.bizo.hive.sparkplug.auth._
import org.apache.commons.lang.StringUtils.substringAfterLast

class Emr(credentials: AWSCredentials) {
  lazy val emr = new AmazonElasticMapReduceClient(credentials)
  
  def run(name: String, cluster: ClusterConfig, instances: (Master, Core), steps: Seq[JobStep]) {
    run(name, cluster, Seq(instances._1, instances._2), steps)
  }
  
  def run(name: String, cluster: ClusterConfig, instances: (Master, Core, Task), steps: Seq[JobStep]) {
    run(name, cluster, Seq(instances._1, instances._2, instances._3), steps)
  }  

  def run(name: String, cluster: ClusterConfig, instances: Seq[InstanceGroup], steps: Seq[JobStep]) {

    val request = new RunJobFlowRequest(name, toJobFlowInstancesConfig(cluster, instances))
    request.setLogUri(cluster.logUri.getOrElse(null))
    request.setSteps(toStepConfig(steps))
    request.setAmiVersion(cluster.amiVersion.getOrElse(null))
    
    emr.runJobFlow(request)
  }
  
  private def toJobFlowInstancesConfig(cluster: ClusterConfig, instances: Seq[InstanceGroup]): JobFlowInstancesConfig = {
    val config = new JobFlowInstancesConfig
    
    config.setEc2KeyName(cluster.sshKeyPair.getOrElse(null))
    config.setHadoopVersion(cluster.hadoopVersion.getOrElse(null))
    config.setKeepJobFlowAliveWhenNoSteps(cluster.keepAlive)
        
    val groups = instances.map(toInstanceGroupConfig(_))

    config.withInstanceGroups(groups)
  }
  
  private def toStepConfig(steps: Seq[JobStep]): Seq[StepConfig] = steps.map { s =>
    new StepConfig(s.getName, s.toHadoopJarStep)
  }
  
  private def toInstanceGroupConfig(i: InstanceGroup) = {
    val conf = new InstanceGroupConfig
    
    conf.setInstanceCount(i.num)

    conf.setInstanceRole(i match {
      case x: Master => "MASTER"
      case x: Core => "CORE"
      case x: Task => "TASK"
    })
      
    conf.setInstanceType(i.size)
    
    if (i.bid.isDefined) {
      conf.setMarket("SPOT")
      conf.setBidPrice("%2.2f".format(i.bid.get))
    } else {
      conf.setMarket("ON_DEMAND")
    }
    
    conf
  }
}

object Emr {
  def apply() = new Emr(EmrJsonCredentials())
}

object Steps {
  def installHive(version: String) = {
    runScript("s3://us-east-1.elasticmapreduce/libs/hive/hive-script",
      Seq("--base-path", "s3://us-east-1.elasticmapreduce/libs/hive/", "--install-hive",
        "--hive-versions", version), name="Install Hive")
  }
  
  def setupDebugging() = {
    runScript("s3://us-east-1.elasticmapreduce/libs/state-pusher/0.1/fetch", name="Setup Debugging")
  }

  def runScript(script: String, args: Seq[String] = Seq(), name: String = "Run Script") = {
    new JarStep("s3://us-east-1.elasticmapreduce/libs/script-runner/script-runner.jar", None, Seq(script) ++ args) {
      override def getName() = name
    }
  }
}

trait ClusterConfig {
  def hadoopVersion: Option[String] = None
  def logUri: Option[String] = None
  def sshKeyPair: Option[String] = None
  def keepAlive: Boolean = false
  def amiVersion: Option[String] = None
}

abstract class InstanceGroup(val num: Int, val size: String, val bid: Option[Double] = None)

class Master(size: String) extends InstanceGroup(1, size)

object Master {
  def apply(size: String = "m1.large") = new Master(size)
}

class Core(num: Int, size: String) extends InstanceGroup(num, size)

object Core {
  def apply(num: Int, size: String = "m1.xlarge") = new Core(num, size)
}

class Task(num: Int, size: String, bid: Option[Double] = None) extends InstanceGroup(num, size, bid)

object Task {
  def apply(num: Int, size: String, bid: Double) = new Task(num, size, Some(bid))
  def apply(num: Int, bid: Double) = new Task(num, "m1.xlarge", Some(bid))
  def apply(num: Int, size: String = "m1.large") = new Task(num, size, None)
}

trait JobStep {
  def toHadoopJarStep(): HadoopJarStepConfig
  def getName(): String
}

case class HiveStep(version: String, script: String, args: Map[String, String]) extends JobStep {
  def toHadoopJarStep(): HadoopJarStepConfig = {
    val config = new HadoopJarStepConfig("s3://us-east-1.elasticmapreduce/libs/script-runner/script-runner.jar")
    
    val systemArgs = Seq("s3://us-east-1.elasticmapreduce/libs/hive/hive-script",
      "--base-path", "s3://us-east-1.elasticmapreduce/libs/hive/",
      "--hive-versions", version, "--run-hive-script", "--args",
      "-f", script) 
  
    val userArgs = args.flatMap { case (k, v) => Seq("-d", "%s=%s".format(k, v)) }
    
    config.withArgs(systemArgs ++ userArgs)
  }
  
  def getName() = "Hive: " + JobStepUtils.getScriptName(script)
}

case class JarStep(jar: String, mainClass: Option[String] = None, args: Seq[String]) extends JobStep {
  def toHadoopJarStep(): HadoopJarStepConfig = {
    val config = new HadoopJarStepConfig(jar)
    
    for (main <- mainClass) {
      config.setMainClass(main)
    }
        
    config.withArgs(args)
  }
  
  def getName() = "Jar: " + JobStepUtils.getJarName(jar, mainClass)
}

object JobStepUtils {
  def getScriptName(script: String) = substringAfterOrInput(script, "/")
  
  def getJarName(jar: String, mainClass: Option[String] = None) = {
    getScriptName(jar) + mainClass.map{ m => ":" + substringAfterOrInput(m, ".") }.getOrElse("")
  }
  
  private def substringAfterOrInput(input: String, substring: String): String = {
    val last = substringAfterLast(input, substring)

    if (last == "") input else last
  } 
}
