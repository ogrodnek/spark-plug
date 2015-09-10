package com.bizo.hive.sparkplug.emr

import com.amazonaws.services.elasticmapreduce.model.{Application => AwsApplication}

import java.util.Collection

import scala.collection.JavaConverters._

case class Application(
  val name: String,
  val version: Option[String] = None,
  val args: Option[List[String]] = None,
  val additionalInfo: Option[Map[String, String]] = None
)

trait ApplicationImplicits {
  implicit def application2AwsApplication(a: Application): AwsApplication = {
    val awsApp = new AwsApplication()
      .withName(a.name)
      .withVersion(a.version.orNull)

    a.args.foreach { args => awsApp.setArgs(args.asJava) }
    a.additionalInfo.foreach { infos => awsApp.setAdditionalInfo(infos.asJava) }

    awsApp
  }

  implicit def applicationList2Java(l: Seq[Application]): Collection[AwsApplication] = {
    l.map(application2AwsApplication).asJava
  }
}
