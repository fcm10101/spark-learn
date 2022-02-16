package com.fchaim.spark01

import org.apache.spark.{SparkConf, SparkContext}

object MapPartitionsWithIndex {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("测试MapPartitionsWithIndex")

    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(Seq("a", "b", "c", "d", "e", "f"), 2).mapPartitionsWithIndex((idx,iter)=>{
      iter.map(row => (idx,row))
    })

    rdd.foreach(println)

    sc.stop()



  }

}
