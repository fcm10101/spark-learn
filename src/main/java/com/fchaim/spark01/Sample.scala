package com.fchaim.spark01

import org.apache.spark.{SparkConf, SparkContext}

object Sample {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("测试Sample")

    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(Seq("a", "b", "c", "d", "e", "f"), 2)

    //允许样本被重复抽取，抽取50%
    val randomRdd = rdd.sample(true, 0.5)
    randomRdd.foreach(println)

    rdd.takeSample(true,5).foreach(println)


    sc.stop()



  }

}
