package com.bizo.hive.sparkplug.auth

import com.amazonaws.auth.AWSCredentials
import java.io.File
import java.util.Properties
import java.io.FileInputStream

/** Loads credentials from a property file, e.g. ~/.aws_credentials used by the CLI tools. */
class DotAwsCredentials(file: File) extends AWSCredentials {
  def this() = this(new File(System.getProperty("user.home") + "/.aws_credentials"))

  val p = {
    val p = new Properties()
    val in = new FileInputStream(file.getAbsoluteFile)
    try {
      p.load(in)
    } finally {
      in.close()
    }
    p
  }

  override def getAWSAccessKeyId() = p.getProperty("AWSAccessKeyId")
  override def getAWSSecretKey() = p.getProperty("AWSSecretKey")
}
