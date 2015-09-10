package com.bizo.hive.sparkplug.emr

import org.junit.Test
import org.junit.Assert._
import com.amazonaws.auth.BasicAWSCredentials
import scala.collection.JavaConversions._

class EmrTest {
  val aws = new TestAmazonElasticMapReduceClient
  val emr = new Emr(null) {
    override lazy val emr = aws
  }
  implicit val config = new ClusterConfig() {}

  @Test
  def testPopulateHiveInstall() {

    val flow = JobFlow("test", Master() + Core(4) + Spot(4), Seq(
      new HiveStep(version = "0.7", "blah", Map()),
      new HiveStep(version = "0.7", "blah2", Map())))

    emr.run(flow)

    val steps = aws.jobFlowRequest.getSteps
    assertEquals(3, steps.size)
    assertEquals("Install Hive 0.7", steps(0).getName)
    assertEquals("Hive: blah", steps(1).getName)
    assertEquals("Hive: blah2", steps(2).getName)
  }

  @Test
  def testPopulateMultipleHiveInstalls() {
    val flow = JobFlow("test", Master() + Core(4) + Spot(4), Seq(
      new HiveStep(version = "0.7", "blah", Map()),
      new HiveStep(version = "0.7", "blah2", Map()),
      new HiveStep(version = "0.8", "blah3", Map())))

    emr.run(flow)

    val expected = Seq("Install Hive 0.7", "Install Hive 0.8", "Hive: blah", "Hive: blah2", "Hive: blah3")

    assertEquals(expected, aws.jobFlowRequest.getSteps.map(_.getName).toSeq)
  }

  @Test
  def testPopulateApplications() {
    val testConfig = new ClusterConfig {
      override def applications = Some(Seq(
        Application(name = "Spark")
      ))
    }

    val flow = JobFlow("test", Master() + Core(4) + Spot(4), Seq())

    emr.run(flow)(testConfig)

    val expected = Seq("Spark")
    val actual = aws.jobFlowRequest.getApplications.map(a => a.getName).toSeq

    assertEquals(expected, actual)
  }
}
