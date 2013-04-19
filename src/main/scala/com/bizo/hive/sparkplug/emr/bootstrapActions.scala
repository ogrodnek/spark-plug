package com.bizo.hive.sparkplug.emr


trait BootstrapAction {
  def args: Seq[String] = Seq.empty
  def name: String
  def path: String
}

case class BootstrapScript(
  val name: String, 
  val path: String, 
  override val args: Seq[String] = Seq.empty
) extends BootstrapAction



class MemoryIntensive extends BootstrapScript(
  "Memory Intensive",
  "s3://elasticmapreduce/bootstrap-actions/configurations/latest/memory-intensive"
)