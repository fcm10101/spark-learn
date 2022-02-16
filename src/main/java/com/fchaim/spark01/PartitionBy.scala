package com.fchaim.spark01

import com.fchaim.util.SparkContextUtil
import org.apache.spark.{HashPartitioner, SparkContext}
import org.apache.spark.rdd.RDD

case class Order(id:Int,uid:Int,amount:Double)
object PartitionBy {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkContextUtil.getSc("local[*]", "分区器")

    val rddOrder: RDD[(Order, Int)] = sc.parallelize(Seq(
      (Order(1, 10, 100), 1),
      (Order(2, 10, 80), 1),
      (Order(3, 12, 100), 1),
      (Order(2, 10, 100), 1),
      (Order(3, 12, 100), 1)
    ), 2)

    // partitionBy 可以根据用户自定义的Partitioner
    val rddOrderNew: RDD[(Order, Int)] = rddOrder.partitionBy(new HashPartitioner(3)) //HashPartitioner 是将key的hashcode%分区数 来决定一条数据该分到那个取

    println(rddOrderNew.partitions.size)

    val rddOrderNew2: RDD[(Order, Int)] = rddOrder.partitionBy(new HashPartitioner(3) {
      override def numPartitions: Int = super.numPartitions

      override def getPartition(key: Any): Int = {
        val od: Order = key.asInstanceOf[Order]
        super.getPartition(od.id)
      }
    })

    println(rddOrderNew2.partitions.size)

    val rddOrderNew3: RDD[(Order, Int)] = rddOrder.partitionBy(new OrderPartition(3))

    println(rddOrderNew3.partitions.size)



    sc.stop()

  }

}

class OrderPartition(partitions : Int) extends HashPartitioner(partitions : Int) {
  override def numPartitions: Int = super.numPartitions

  override def getPartition(key: Any): Int = {
    val od: Order = key.asInstanceOf[Order]
    super.getPartition(od.id)
  }
}