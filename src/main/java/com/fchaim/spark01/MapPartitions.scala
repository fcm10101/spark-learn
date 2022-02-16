package com.fchaim.spark01

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

import java.sql.{Driver, DriverManager}

object MapPartitions {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)


    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("MapPartitions")
    val sc = new SparkContext(conf)
    val rdd = sc.textFile("data/wordcount/input/wc.txt")
    val rddM = rdd.flatMap(s => s.split("\\s+"))
    //map 方式是每个元素都会执行一次func
    val rdd2 = rddM.map(s=>{
      println("map方式执行")
      (s, 1)
    })

    //mapPartitions方式是每个分区只会执行一次 但输入输出都是iteration迭代器
    val rdd3 = rddM.mapPartitions(iter => {
      println("MapPartitions方式执行")
      iter.map((_, 1))
    })

    //rdd2.foreach(println)

    //rdd3.foreach(println)

    val rdd4 = rddM.mapPartitions(iter => {
      List("aa", "bb", "cc").iterator
    })
    //rdd4.foreach(println)

    val rddSanGuo = sc.textFile("data/sanguo/input/sanguo.txt")


    //map实现
    val rddSanGuoRs = rddSanGuo.map(row => {
      val strAry = row.split(",")
      val id = strAry(0).toInt

      // 链接数据库
      val conn = DriverManager.getConnection("jdbc:mysql://192.168.133.101:3306/test", "root", "root")
      val stmt = conn.prepareStatement("select name,role,`battel` from battel where id=?")
      stmt.setInt(1, id)
      val rs = stmt.executeQuery()
      rs.next()

      val name = rs.getString(1)
      val role = rs.getString(2)
      val battel = rs.getString(3)

      row + "," + id + "," + name + "," + role + "," + battel
    })
    //rddSanGuoRs.foreach(println)

    val rddSanGuoRsMap = rddSanGuo.mapPartitions(iter => {
      val conn = DriverManager.getConnection("jdbc:mysql://192.168.133.101:3306/test", "root", "root")
      val stmt = conn.prepareStatement(" select name,role,battel from battel where id=?")

      iter.map(row => {
        val strArr = row.split(",")
        val id = strArr(0).toInt

        stmt.setInt(1, id)
        val rs = stmt.executeQuery()
        rs.next()

        val name = rs.getString(1)
        val role = rs.getString(2)
        val battel = rs.getString(3)
        row + "," + id + "," + name + "," + role + "," + battel
      })
    })
    rddSanGuoRsMap.foreach(println)



    sc.stop()
  }
}
