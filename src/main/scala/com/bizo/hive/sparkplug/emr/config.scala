package com.bizo.hive.sparkplug.emr

trait ClusterConfig {
  def hadoopVersion: Option[String] = None
  def logUri: Option[String] = None
  def sshKeyPair: Option[String] = None
  def amiVersion: Option[String] = None
  def defaultMasterSize: String = "m1.large"
  def defaultCoreSize: String = "m1.xlarge"
  def defaultTaskSize: String = defaultCoreSize
  def visibleToAllUsers: Option[Boolean] = None
  def availabilityZone: Option[String] = None
  def jobFlowRole: Option[String] = None
  def serviceRole: Option[String] = None
  def subnetId: Option[String] = None
}

class DefaultBidProvider extends BidProvider {
  val bidPrices = Map(
    // Standard 
    "m1.small" -> 0.048,
    "m1.medium" -> 0.096,
    "m1.large" -> 0.193,
    "m1.xlarge" -> 0.385,

    // Second Generation
    "m3.medium" -> 0.077,
    "m3.large" -> 0.154,
    "m3.xlarge" -> 0.308,
    "m3.2xlarge" -> 0.616,

    // Micro
    "t1.micro" -> 0.02,

    // High-Memory
    "r3.large" -> 0.193,
    "r3.xlarge" -> 0.385,
    "r3.2xlarge" -> 0.77,
    "r3.4xlarge" -> 1.54,
    "r3.8xlarge" -> 2.8,
    "m2.xlarge" -> 0.27,
    "m2.2xlarge" -> 0.539,
    "m2.4xlarge" -> 1.078,

    // High-CPU
    "c3.large" -> 0.116,
    "c3.xlarge" -> 0.231,
    "c3.2xlarge" -> 0.462,
    "c3.4xlarge" -> 0.924,
    "c3.8xlarge" -> 1.68,
    "c1.medium" -> 0.143,
    "c1.xlarge" -> 0.572,

    // Cluster Compute
    "cc1.4xlarge" -> 1.3,
    "cc2.8xlarge" -> 2.0,

    // High-Memory Cluster
    "cr1.8xlarge" -> 3.5,

    // Cluster GPU
    "cg1.4xlarge" -> 2.1,

    // High-I/O
    "hi1.4xlarge" -> 3.1,

    // High-Storage
    "hs1.8xlarge" -> 4.6
  )

  override def bid(size: String): Double = bidPrices(size)
}
