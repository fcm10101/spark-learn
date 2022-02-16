package com.fchaim.spark01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local[1]")
    conf.setAppName("单词计数")
    val sc = new SparkContext(conf)

    val rdd : RDD[String] = sc.textFile("data/wordcount/input/wc.txt")
    val rs : RDD[(String, Int)] = rdd.flatMap(s => s.split(" ")).map((_, 1)).reduceByKey(_ + _)
    rs.foreach(println)

    sc.stop()
  }
}
