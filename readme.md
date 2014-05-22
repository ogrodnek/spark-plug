# spark-plug
[![Build Status](https://travis-ci.org/ogrodnek/spark-plug.svg?branch=master)](https://travis-ci.org/ogrodnek/spark-plug)

A scala driver for launching Amazon EMR jobs

## why?

We run a lot of reports.  In the past, these have been kicked off by bash scripts that typically do things like date math, copy scripts and config files to s3 before calling to the amazon elastic-mapreduce command line client to launch the job.  The emr client invocation ends up being dozen of lines of bash code adding each step and passing arguments.

It's been a pain to share defaults or add any abstraction over common job steps.  Additionally, performing date arithmetic and conditionally adding EMR steps can be a pain.  Lastly, the EMR client offers less control over certain options available from the EMR API.

## simple example

```
val flow = JobFlow(
  name      = s"${stage}: analytics report [${date}]",
  cluster   = Master() + Core(8) + Spot(10),
  bootstrap = Seq(MemoryIntensive),
  steps     = Seq(
    SetupDebugging(),
    new HiveStep("s3://bucket/location/report.sql",
      Map("YEAR" -> year, "MONTH" -> month, "DAY" -> day))
  )
)

val id = Emr.run(flow)(ClusterDefaults(hadoop="1.0.3"))
println(id)

```

## download

* [spark-plug_2.10-1.2.2](https://drone.io/github.com/ogrodnek/spark-plug/files/target/scala-2.10/spark-plug_2.10-1.2.2.jar)

