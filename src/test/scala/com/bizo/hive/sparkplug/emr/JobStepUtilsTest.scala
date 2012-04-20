package com.bizo.hive.sparkplug.emr

import org.junit.Test
import org.junit.Assert._
import com.bizo.hive.sparkplug.emr.JobStepUtils._

class JobStepUtilsTest {
  
  @Test
  def testGetScriptName() {
    assertEquals("campaign-report.sql", getScriptName("s3://reports/blah/script/campaign-report.sql"))
    assertEquals("campaign-report.sql", getScriptName("campaign-report.sql"))
  }
  
  @Test
  def testGetJarName() {
    assertEquals("sugar-hive-client-1.1-dev-20110418215352.jar", getJarName("s3://repository/bizo.com/sugar-hive-client/jars/sugar-hive-client-1.1-dev-20110418215352.jar"))
    assertEquals("sugar-hive-client-1.1-dev-20110418215352.jar", getJarName("sugar-hive-client-1.1-dev-20110418215352.jar"))
  }
  
  @Test
  def testGetJarNameWithMain() {
    assertEquals("sugar-hive-client-1.1-dev-20110418215352.jar:Indexer",
      getJarName("s3://repository/bizo.com/sugar-hive-client/jars/sugar-hive-client-1.1-dev-20110418215352.jar",
        Some("com.bizo.sugarcube.hive.index.Indexer")))
        
    assertEquals("sugar-hive-client-1.1-dev-20110418215352.jar:Indexer",
      getJarName("s3://repository/bizo.com/sugar-hive-client/jars/sugar-hive-client-1.1-dev-20110418215352.jar",
        Some("Indexer")))        
  }
}