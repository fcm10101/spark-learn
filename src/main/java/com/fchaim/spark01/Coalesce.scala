package com.fchaim.spark01

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Coalesce {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("测试coalesce")

    val sc = new SparkContext(conf)

    //按照相同的key分组
    val rdd = sc.parallelize(Seq(("a",1),("a",3),("b",2),("a",5),("b",6),("c",10)),3)
    println(rdd.partitions.size)

    //coalesce可以改变RDD的分区数
    //当shuffle=true时，可以将原来的rdd的分区书变多或变少
    //当shuffle=false时，可以将原来的rdd的分区书变少（只有改变shuffle才能使分区数量变多）
    val rdd2 = rdd.coalesce(6, false)
    val rdd3 = rdd.coalesce(6, true)

    println(rdd2.partitions.size)
    println(rdd3.partitions.size)

    println("---------------------------------------------------")

    //repartition的实质就是调用coalesce
    //coalesce(numPartitions,shuffle=true) //shuffle被设置为true
    //所以，repartition既可以让分区数变大，也可以将分区数变小（repartition一定会伴随着shuffle的改变）
    val rdd4: RDD[(String, Int)] = rdd.repartition(10)
    val rdd5: RDD[(String, Int)] = rdd.repartition(2)

    println(rdd4.partitions.size)
    println(rdd5.partitions.size)



    sc.stop()



  }

}
