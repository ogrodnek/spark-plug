# spark-plug
[![Build Status](https://drone.io/github.com/ogrodnek/spark-plug/status.png)](https://drone.io/github.com/ogrodnek/spark-plug/latest)

A scala driver for launching Amazon EMR jobs

## why?

We run a lot of reports.  In the past, these have been kicked off by bash scripts that typically do things like date math, copy scripts and config files to s3 before calling to the amazon elastic-mapreduce command line client to launch the job.  The emr client invocation ends up being dozen of lines of bash code adding each step and passing arguments.

It's been a pain to share defaults or add any abstraction over common job steps.  Additionally, performing date arithmetic and conditionally adding EMR steps can be a pain.  Lastly, the EMR client offers less control over certain options available from the EMR API.

## simple example

```
val steps = Seq(
  Steps.setupDebugging(),
    HiveStep("s3://bucket/location/report.sql",
      Map("YEAR" -> year, "MONTH" -> month, "DAY" -> day))
)

val name = "%s: analytics report %s".format(stage, date)
val instances = (Master("m1.large"), Core(4), Task(12, 0.30))

Emr().run(name, ClusterDefaults(hadoop = "0.20"), instances, steps)
```

## download

* [spark-plug_2.9.2-1.2.1](https://drone.io/github.com/ogrodnek/spark-plug/files/target/scala-2.9.2/spark-plug_2.9.2-1.2.1.jar)

