package com.fchaim.spark01

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SortByKey {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("测试sortByKey")

    val sc = new SparkContext(conf)


    val rdd = sc.parallelize(Seq(("a",1),("a",3),("b",2),("a",5),("b",6),("c",10)),2)

    //根据key排序

    val sortByRdd: RDD[(String, Int)] = rdd.sortByKey()

    //sortByRdd.foreach(println)

    sortByRdd.saveAsTextFile("data/sortbykey/output/")





    sc.stop()



  }

}
