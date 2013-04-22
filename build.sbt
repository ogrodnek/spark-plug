organization := "com.bizo"

name := "spark-plug"

version := "1.2.1"

crossScalaVersions := Seq("2.9.2", "2.10.1")

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.3.25",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "commons-lang" % "commons-lang" % "2.6",
  "junit" % "junit" % "4.10" % "test",
  "com.novocode" % "junit-interface" % "0.10-M4" % "test"
)
