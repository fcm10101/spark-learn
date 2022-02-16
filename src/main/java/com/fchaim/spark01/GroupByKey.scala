package com.fchaim.spark01

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GroupByKey {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("测试groupByKey")

    val sc = new SparkContext(conf)

    //按照相同的key分组
    val rdd = sc.parallelize(Seq(("a",1),("a",3),("a",5),("b",2),("b",6),("c",10)))
    val groupByRdd: RDD[(String, Iterable[Int])] = rdd.groupByKey()
    groupByRdd.foreach(println)

    println("-------------------------------------------")

    //按照相同的key分组,并累加求和
    val groupBySumRdd: RDD[(String, Int)] = groupByRdd.map(tuple => {
      (tuple._1, tuple._2.sum)
    })
    groupBySumRdd.foreach(println)

    sc.stop()



  }

}
