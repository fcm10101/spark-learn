package com.fchaim.spark01

import com.fchaim.util.SparkContextUtil
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD

import scala.collection.mutable

object BroadCast {

  def main(args: Array[String]): Unit = {
    val sc: SparkContext = SparkContextUtil.getSc("local", "广播变量")
    val rdd: RDD[(Int, String)] = sc.parallelize(Seq(
      (1, "北京"),
      (2, "上海"),
      (3, "南京")
    ))

    val mp = new mutable.HashMap[Int, String]()
    mp.put(1,"张三")
    mp.put(2,"李四")
    mp.put(3,"王五")

    //将driver端创建的普通集合对象，广播出去
    // 广播的实质是：将driver端数据对象，序列化后，给每一个executor发送一份（每一个executor只持有一份广播变量的拷贝） 发送方式使用BitTorrent（人人为我，我为人人）
    val bc: Broadcast[mutable.HashMap[Int, String]] = sc.broadcast(mp)

    val res: RDD[(Int, String, Any)] = rdd.map(tp => {
      val dc: mutable.HashMap[Int, String] = bc.value

      val name: String = dc.get(tp._1).get


      (tp._1, tp._2, name)
    })

    res.foreach(println)

    sc.stop()

  }
}
