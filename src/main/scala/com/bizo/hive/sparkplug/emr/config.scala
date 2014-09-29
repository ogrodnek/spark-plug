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
    "m1.small" -> 0.06,
    "m1.medium" -> 0.12,
    "m1.large" -> 0.24,
    "m1.xlarge" -> 0.48,

    // Second Generation
    "m3.xlarge" -> 0.50,
    "m3.2xlarge" -> 1.00,

    // Micro
    "t1.micro" -> 0.02,

    // High-Memory
    "m2.xlarge" -> 0.41,
    "m2.2xlarge" -> 0.82,
    "m2.4xlarge" -> 1.64,

    // High-CPU
    "c1.medium" -> 0.145,
    "c1.xlarge" -> 0.58,

    // Cluster Compute
    "cc1.4xlarge" -> 1.3,
    "cc2.8xlarge" -> 2.4,

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
