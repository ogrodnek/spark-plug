package com.bizo.hive.sparkplug.auth

import com.amazonaws.auth.AWSCredentials
import java.io.File
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.JSONValue
import java.io.FileReader

/**
 * Parse credentials.json style creds
 */
class EmrJsonCredentials(jsonFile: File) extends AWSCredentials {
  val (accessKey, secretKey) = parseKeys(jsonFile)
  
  override def getAWSAccessKeyId() = accessKey
  override def getAWSSecretKey() = secretKey
  
  private def parseKeys(jsonFile: File): (String, String) = {
    val r = new FileReader(jsonFile)
    
    try {
      val o = JSONValue.parse(r).asInstanceOf[JSONObject]
      (o.get("access-id").toString, o.get("private-key").toString)
    } finally {
      r.close()
    }
  }
}

object EmrJsonCredentials {
  def apply() = {
    new EmrJsonCredentials(new File(System.getenv("AWS_EMR_HOME"), "credentials.json"))
  }
}