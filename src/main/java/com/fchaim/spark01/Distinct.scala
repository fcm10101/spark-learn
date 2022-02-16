package com.fchaim.spark01

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object Distinct {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("测试distinct")

    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(Seq("a", "b", "c", "d", "e", "f","c","d","e","f"))
    val distinctRdd = rdd.distinct()
    distinctRdd.foreach(println)

    sc.stop()



  }

}
