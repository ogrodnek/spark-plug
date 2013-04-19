package com.bizo.hive.sparkplug.auth

import com.amazonaws.auth.AWSCredentials
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.JSONValue
import java.io.File
import java.io.{FileInputStream, InputStreamReader, InputStream}

/**
 * Parse credentials.json style creds
 */
class EmrJsonCredentials private (accessAndSecretKeys: (String, String)) extends AWSCredentials {
  def this(jsonFile: File) = this(EmrJsonCredentials.parseKeys(jsonFile))
  def this(jsonResourcePath: String) = this(EmrJsonCredentials.parseKeys(jsonResourcePath))

  override def getAWSAccessKeyId() = accessAndSecretKeys._1
  override def getAWSSecretKey() = accessAndSecretKeys._2
}

object EmrJsonCredentials {
  def apply() = {
    new EmrJsonCredentials(new File(System.getenv("AWS_EMR_HOME"), "credentials.json"))
  }

  private def parseKeys(jsonResourcePath: String): (String, String) = {
    Option(this.getClass.getClassLoader.getResourceAsStream(jsonResourcePath)).map { stream =>
      parseKeys(stream)
    } getOrElse { sys.error("unable to open resource '%s'".format(jsonResourcePath)) }
  }

  private def parseKeys(jsonFile: File): (String, String) = {
    parseKeys(new FileInputStream(jsonFile))
  }

  private[this] def parseKeys(in: InputStream): (String, String) = {
    val r = new InputStreamReader(in, "UTF-8")
    try {
      val o = JSONValue.parse(r).asInstanceOf[JSONObject]
      (o.get("access-id").toString, o.get("private-key").toString)
    } finally {
      r.close()
    }
  }
}
