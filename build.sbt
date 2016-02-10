import SonatypeKeys._

sonatypeSettings

organization := "com.bizo"

organizationName := "com.bizo"

name := "spark-plug"

version := "1.2.6"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:_")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.10.16",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "commons-lang" % "commons-lang" % "2.6",
  "junit" % "junit" % "4.10" % "test",
  "com.novocode" % "junit-interface" % "0.10-M4" % "test"
)

EclipseKeys.withSource := true

site.settings

site.includeScaladoc()

ghpages.settings

git.remoteRepo := "git@github.com:ogrodnek/spark-plug.git"

pomExtra := {
 <url>https://github.com/ogrodnek/spark-plug</url>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>https://github.com/ogrodnek/spark-plug</url>
    <connection>https://github.com/ogrodnek/spark-plug.git</connection>
  </scm>
  <developers>
    <developer>
      <id>larry</id>
      <name>Larry Ogrodnek</name>
      <email>larry@bizo.com</email>
    </developer>
  </developers>
}
