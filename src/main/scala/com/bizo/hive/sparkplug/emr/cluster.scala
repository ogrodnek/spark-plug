package com.bizo.hive.sparkplug.emr


abstract sealed class MasterDef
abstract sealed class TaskDef
abstract sealed class CoreDef
abstract sealed class UnspecifiedDef

class Cluster[M, C, T](val instances: Set[InstanceGroup]) {
  def +(m: Master)(implicit ev: M =:= UnspecifiedDef) = {
    new Cluster[MasterDef, C, T](instances + m)
  }
  
  def +(t: Task)(implicit ev: T =:= UnspecifiedDef) = {
    new Cluster[M, C, TaskDef](instances + t)
  }
  
  def +(c: Core)(implicit ev: C =:= UnspecifiedDef) = {
    new Cluster[M, CoreDef, T](instances + c)
  }
}

class RunnableCluster private(val instances: Set[InstanceGroup])

object RunnableCluster {
  implicit def masterToRunnable(m: Master): RunnableCluster = new RunnableCluster(Set(m))
  implicit def masterOnlyToRunnable(c: Cluster[MasterDef, UnspecifiedDef, UnspecifiedDef]): RunnableCluster = new RunnableCluster(c.instances)

  // Task is only allowed if Core is present.
  implicit def masterCoreToRunnable(c: Cluster[MasterDef, CoreDef, _]): RunnableCluster = new RunnableCluster(c.instances)
}

trait BidProvider {
  def bid(size: String): Double
}

class FixedBidProvider(bid: Double) extends BidProvider {
  def bid(size: String): Double = bid
}

abstract class InstanceGroup(val num: Int, val size: Option[String], val bid: Option[BidProvider] = None)

class Master(size: Option[String]) extends InstanceGroup(1, size)

object Master {
  def apply(size: String) = new Master(Some(size))
  def apply() = new Master(None)
}

class Core(num: Int, size: Option[String]) extends InstanceGroup(num, size)

object Core {
  def apply(num: Int, size: String) = new Core(num, Some(size))
  def apply(num: Int) = new Core(num, None)
}

class Task(num: Int, size: Option[String], bid: Option[BidProvider]) extends InstanceGroup(num, size, bid)

object Task {
  def apply(num: Int, size: String) = new Task(num, Some(size), None)
  def apply(num: Int) = new Task(num, None, None)
  def apply(num: Int, size: String, bid: Double) = new Task(num, Some(size), Some(new FixedBidProvider(bid)))
  def apply(num: Int, bid: Double) = new Task(num, None, Some(new FixedBidProvider(bid)))
}

object Spot {
  def apply(num: Int)(implicit bidProvider: BidProvider) = new Task(num, None, Some(bidProvider))
  def apply(num: Int, size: String)(implicit bidProvider: BidProvider) = new Task(num, Some(size), Some(bidProvider))
  def apply(num: Int, size: String, bid: Double) = new Task(num, Some(size), Some(new FixedBidProvider(bid)))
}
