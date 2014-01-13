organization := "com.bizo"

name := "spark-plug"

version := "1.2.2"

scalaVersion := "2.10.2"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-language:_")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.6.10",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "commons-lang" % "commons-lang" % "2.6",
  "junit" % "junit" % "4.10" % "test",
  "com.novocode" % "junit-interface" % "0.10-M4" % "test"
)

EclipseKeys.withSource := true
