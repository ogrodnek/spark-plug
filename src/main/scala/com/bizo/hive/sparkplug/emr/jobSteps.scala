package com.bizo.hive.sparkplug.emr

import org.apache.commons.lang.StringUtils.substringAfterLast
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig
import scala.collection.JavaConversions._

trait JobStep { self =>
  private var onFailure = "TERMINATE_JOB_FLOW"

  def toHadoopJarStep: HadoopJarStepConfig
  def name: String

  def actionOnFailure: String = onFailure
  def onFailureContinue: self.type = { onFailure = "CONTINUE"; this }
  def onFailureCancelAndWait: self.type = { onFailure = "CANCEL_AND_WAIT"; this }
  def onFailureTerminateJobFlow: self.type = { onFailure = "TERMINATE_JOB_FLOW"; this }
}

/** Executes a Hive script. */
case class HiveStep(version: String, script: String, args: Map[String, String]) extends JobStep {
  override def toHadoopJarStep: HadoopJarStepConfig = {
    val config = new HadoopJarStepConfig("s3://us-east-1.elasticmapreduce/libs/script-runner/script-runner.jar")

    val systemArgs = Seq("s3://us-east-1.elasticmapreduce/libs/hive/hive-script",
      "--base-path", "s3://us-east-1.elasticmapreduce/libs/hive/",
      "--hive-versions", version, "--run-hive-script", "--args",
      "-f", script)

    val userArgs = args.flatMap { case (k, v) => Seq("-d", "%s=%s".format(k, v)) }

    config.withArgs(systemArgs ++ userArgs)
  }

  override val name = "Hive: " + JobStepUtils.getScriptName(script)
}

abstract class AbstractJarStep(jar: String, mainClass: Option[String] = None, args: Seq[String] = Seq.empty) extends JobStep {
  override def toHadoopJarStep: HadoopJarStepConfig = {
    val config = new HadoopJarStepConfig(jar)
    for (main <- mainClass) {
      config.setMainClass(main)
    }
    config.withArgs(args)
  }
}

/** Generic JarStep for running a jar with an optional main class and optional args. */
case class JarStep(jar: String, mainClass: Option[String] = None, args: Seq[String] = Seq.empty) extends AbstractJarStep(jar, mainClass, args) {
  override val name = "Jar: " + JobStepUtils.getJarName(jar)
}

abstract class AbstractScriptStep(script: String, scriptArgs: Seq[String] = Seq.empty) extends AbstractJarStep(
  jar = "s3://us-east-1.elasticmapreduce/libs/script-runner/script-runner.jar",
  args = Seq(script) ++ scriptArgs)

/** Generic script to execute a shell script with optional arguments. */
case class ScriptStep(script: String, scriptArgs: Seq[String] = Seq.empty) extends AbstractScriptStep(script, scriptArgs) {
  override val name = "Script: " + JobStepUtils.getScriptName(script)
}

/** Installs Hive. */
case class InstallHive(version: String) extends AbstractScriptStep(
  script = "s3://us-east-1.elasticmapreduce/libs/hive/hive-script",
  scriptArgs = Seq("--base-path", "s3://us-east-1.elasticmapreduce/libs/hive/", "--install-hive", "--hive-versions", version)) {
  override val name = "Install Hive " + version
}

case class SetupDebugging() extends AbstractScriptStep("s3://us-east-1.elasticmapreduce/libs/state-pusher/0.1/fetch") {
  override val name = "Setup Debugging"
}

object JobStepUtils {
  def getScriptName(script: String) = substringAfterOrInput(script, "/")

  def getJarName(jar: String, mainClass: Option[String] = None) = {
    getScriptName(jar) + mainClass.map { m => ":" + substringAfterOrInput(m, ".") }.getOrElse("")
  }

  private def substringAfterOrInput(input: String, substring: String): String = {
    val last = substringAfterLast(input, substring)

    if (last == "") input else last
  }
}

