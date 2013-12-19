package com.bizo.hive.sparkplug.emr

import com.amazonaws.services.elasticmapreduce.model._
import scala.collection.JavaConversions._
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient
import com.amazonaws.auth.AWSCredentials
import com.bizo.hive.sparkplug.auth._
import org.apache.commons.lang.StringUtils.substringAfterLast
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce

class Emr(credentials: AWSCredentials) {
  lazy val emr: AmazonElasticMapReduce = new AmazonElasticMapReduceClient(credentials)

  def run(flow: JobFlow)(implicit config: ClusterConfig): String = {
    val request = new RunJobFlowRequest(flow.name, toJobFlowInstancesConfig(config, flow.cluster.instances.toSeq, flow.keepAlive, flow.terminationProtection))

    request.setAmiVersion(config.amiVersion.orNull)
    request.setLogUri(config.logUri.orNull)
    request.setVisibleToAllUsers(config.visibleToAllUsers.map(boolean2Boolean(_)).orNull)

    request.setBootstrapActions(flow.bootstrap.map(b => {
      new BootstrapActionConfig(b.name, new ScriptBootstrapActionConfig(b.path, b.args))
    }))

    request.setSteps(toStepConfig(flow.steps))

    emr.runJobFlow(request).getJobFlowId
  }

  private def toJobFlowInstancesConfig(config: ClusterConfig, instances: Seq[InstanceGroup], keepAlive: Boolean, terminationProtection: Boolean): JobFlowInstancesConfig = {
    val flow = new JobFlowInstancesConfig

    flow.setEc2KeyName(config.sshKeyPair.orNull)
    flow.setHadoopVersion(config.hadoopVersion.orNull)
    flow.setKeepJobFlowAliveWhenNoSteps(keepAlive)
    flow.setTerminationProtected(terminationProtection)

    for (az <- config.availabilityZone) {
      flow.setPlacement(new PlacementType().withAvailabilityZone(az))
    }
    
    val groups = instances.map(toInstanceGroupConfig(config, _))

    flow.withInstanceGroups(groups)
  }

  private def toStepConfig(steps: Seq[JobStep]): Seq[StepConfig] = {
    (hiveInstallStepsNeeded(steps) ++ steps).map { s =>
      new StepConfig(s.name, s.toHadoopJarStep).withActionOnFailure(s.actionOnFailure)
    }
  }

  private def hiveInstallStepsNeeded(steps: Seq[JobStep]): Seq[JobStep] = {
    val versions = steps.collect { case h: HiveStep => h.version }

    versions.distinct.map { v => InstallHive(v) }
  }

  private def toInstanceGroupConfig(config: ClusterConfig, instances: InstanceGroup) = {
    val group = new InstanceGroupConfig

    group.setInstanceCount(instances.num)

    group.setInstanceRole(instances match {
      case x: Master => "MASTER"
      case x: Core => "CORE"
      case x: Task => "TASK"
    })

    val size = instances.size.getOrElse(instances match {
      case x: Master => config.defaultMasterSize
      case x: Core => config.defaultCoreSize
      case x: Task => config.defaultTaskSize
    })

    group.setInstanceType(size)

    if (instances.bid.isDefined) {
      group.setMarket("SPOT")
      group.setBidPrice("%2.2f".format(instances.bid.get.bid(size)))
    } else {
      group.setMarket("ON_DEMAND")
    }

    group
  }
}

object Emr {
  def apply() = new Emr(EmrJsonCredentials())
  def apply(credentials: AWSCredentials) = new Emr(credentials)
  def run(flow: JobFlow)(implicit config: ClusterConfig): String = {
    apply().run(flow)(config)
  }
}
