package com.fchaim.spark01

import com.fchaim.util.SparkContextUtil
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

case class Order01(id:Int,uid:String,amount:Double)
object ZHExe01 {

  def main(args: Array[String]): Unit = {
    //需求1：统计每个分类的下单总金额，本且按照成交金额排序
    //需求2：将原来的数据，关联上维度信息，保存到HDFS中
    //要关联的维度数据存储在MySQL中
    val sc: SparkContext = SparkContextUtil.getSc("local", "综合练习")
    val txtRdd: RDD[String] = sc.textFile("data/zh/order/input/order.txt")
    val orderRdd: RDD[Order01] = txtRdd.map(line => {
      val lineAry: Array[String] = line.split(",")
      Order01(lineAry(0).toInt, lineAry(1), lineAry(2).toDouble)
    })

    val res1: RDD[(String, Double)] = orderRdd.map(order => (order.uid, order.amount)).reduceByKey(_ + _).sortBy(tp => tp._2,false)

    res1.foreach(println)


    sc.stop()
  }
}
