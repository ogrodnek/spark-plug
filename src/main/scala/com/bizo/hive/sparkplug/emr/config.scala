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
  // bid standard on-demand prices:
  // - http://aws.amazon.com/ec2/pricing/
  // - http://aws.amazon.com/ec2/previous-generation/
  val bidPrices = Map(
    // General Purpose - Previous Generation
    "m1.small"  -> 0.044,
    "m1.medium" -> 0.087,
    "m1.large"  -> 0.175,
    "m1.xlarge" -> 0.350,
    
    // Compute Optimized - Previous Generation
    "c1.medium"   -> 0.130,
    "c1.xlarge"   -> 0.520,
    "cc2.8xlarge" -> 2.000,
    "cc1.4xlarge" -> 1.300,
    
    // GPU Instances - Previous Generation
    "cg1.4xlarge" -> 2.100,

    // Memory Optimized - Previous Generation
    "m2.xlarge"   -> 0.245,
    "m2.2xlarge"  -> 0.490,
    "m2.4xlarge"  -> 0.980,
    "cr1.8xlarge" -> 3.500,
    
    // Storage Optimized - Previous Generation
    "hi1.4xlarge" -> 3.100,
    
    // Micro Instances - Previous Generation
    "t1.micro" -> 0.020,    
    
    // General Purpose - Current Generation
    "t2.micro"    -> 0.013,
    "t2.small"    -> 0.026,
    "t2.medium"   -> 0.052,
    "m3.medium"   -> 0.070,
    "m3.large"    -> 0.140,
    "m3.xlarge"   -> 0.280,
    "m3.2xlarge"  -> 0.560,
    
    // Compute Optimized - Current Generation
    "c3.large"   -> 0.105,
    "c3.xlarge"  -> 0.210,
    "c3.2xlarge" -> 0.420,
    "c3.4xlarge" -> 0.840,
    "c3.8xlarge" -> 1.680,
    
    // GPU Instances - Current Generation
    "g2.2xlarge" -> 0.650,
    
    // Memory Optimized - Current Generation
    "r3.large"   -> 0.175,
    "r3.xlarge"  -> 0.350,
    "r3.2xlarge" -> 0.700,
    "r3.4xlarge" -> 1.400,
    "r3.8xlarge" -> 2.800,
    
    // Storage Optimized - Current Generation
    "i2.xlarge"   -> 0.853,
    "i2.2xlarge"  -> 1.705,
    "i2.4xlarge"  -> 3.410,
    "i2.8xlarge"  -> 6.820,
    "hs1.8xlarge" -> 4.600
  )

  override def bid(size: String): Double = bidPrices(size)
}
