package com.bizo.hive.sparkplug.s3

import org.junit.Assert._
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.Rule

class S3Test {
  @Test
  def testParseUrl() {
    val expected = ("reports", "api/year=2012/month=03") 
    assertEquals(expected, S3.parseS3Url("s3://reports/api/year=2012/month=03"))
    assertEquals(expected, S3.parseS3Url("reports:api/year=2012/month=03"))    
  }
  
  @Test(expected=classOf[RuntimeException])
  def testUnparseable() {
    S3.parseS3Url("bizo")
  }
}