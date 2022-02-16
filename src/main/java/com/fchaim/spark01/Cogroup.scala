package com.fchaim.spark01

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Cogroup {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("测试cogroup")

    val sc = new SparkContext(conf)

    //按照相同的key分组
    val rdd1: RDD[(String, Int)] = sc.parallelize(Seq(("a", 1), ("a", 3), ("a", 5), ("b", 2), ("b", 6), ("c", 10)), 3)
    val rdd2: RDD[(String, String)] = sc.parallelize(Seq(("a", "x"), ("b", "y"), ("c", "z"),("d","n")), 3)

    val cogroupRdd: RDD[(String, (Iterable[Int], Iterable[String]))] = rdd1.cogroup(rdd2)
    cogroupRdd.foreach(println)



    sc.stop()



  }

}
