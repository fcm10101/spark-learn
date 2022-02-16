package com.fchaim.spark01

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AggregateByKey {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf()
    conf.setMaster("local[2]")
    conf.setAppName("测试aggregateByKey")

    val sc = new SparkContext(conf)

    //按照相同的key分组
    val rdd = sc.parallelize(Seq(("a",1),("a",3),("b",2),("a",5),("b",6),("c",10)),2)

    //需求1：将相同key的元素进行累加
    val f11 = (u:Int,t:Int) => {u+t}
    val f12 = (u1:Int,u2:Int) => u1+u2

    // zeroValue 是做聚合时的初始话值 （初始值只在分区内聚合的时候使用，在分区间聚合的时候不在使用）
    // f11 用与分区内的局部聚合逻辑
    // f12 用于各分区局部聚合结果的全局聚合逻辑
    val aggregateByKeyRdd: RDD[(String, Int)] = rdd.aggregateByKey(100)(f11, f12)
    aggregateByKeyRdd.foreach(println)

    println("--------------------------------------------------")

    //需求2： 将相同key的元素聚合成一个list
    val f21 = (u:List[Int],e:Int) => {e::u}
    val f22 = (list1:List[Int],list2:List[Int]) => {list2 ::: list1}
    val rdd2: RDD[(String, List[Int])] = rdd.aggregateByKey(List[Int]())(f21, f22)
    rdd2.foreach(println)


    println("--------------------------------------------------")


    val rdd3 = sc.parallelize(Seq(1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2), 3)
    //这个算子的初始值，在分区内局部聚合的时候以及分区间聚合的时候都会被使用
    val rddx = rdd3.aggregate(100)((u, e) => u + e, (u1, u2) => u1 + u2)
    println(rddx)



    sc.stop()



  }

}
