package com.fchaim.spark01

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

case class Hero(id:Int,name:String,age:Int,city:String)
case class HeroInfo(id:Int,hType:String,num:Int)
object TransformationExe01 {

  /**
   * 每个城市的杀人总数，关押总数
   * 每个城市杀人最多的将军信息
   * @param args
   */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setAppName("action算子练习1")
    conf.setMaster("local")

    val sc = new SparkContext(conf)

    val heroFileRdd: RDD[String] = sc.textFile("data/sanguo/heros/heros.txt")
    val killFileRdd: RDD[String] = sc.textFile("data/sanguo/input/sanguo.txt")

    val heroRdd: RDD[Hero] = heroFileRdd.map(hero => {
      val jsonObject: JSONObject = JSON.parseObject(hero)
      val id = jsonObject.getIntValue("id")
      val name = jsonObject.getString("name")
      val age = jsonObject.getIntValue("age")
      val city = jsonObject.getString("city")
      Hero(id,name,age,city)
    })
    val heroInfoRdd: RDD[HeroInfo] = killFileRdd.map(heroInfo => {
      val arrStr: Array[String] = heroInfo.split(",")
      HeroInfo(arrStr(0).toInt, arrStr(1), arrStr(2).toInt)
    })

    //heroRdd.foreach(println)
    //heroInfoRdd.foreach(println)

    // 每个城市的杀人总数，关押总数
    val rddTempJoin: RDD[(Int, (Hero, HeroInfo))] = heroRdd.map(hero => (hero.id, hero)).join(heroInfoRdd.map(heroInfo => (heroInfo.id, heroInfo)))
    //多次使用时候，使用cache
    rddTempJoin.cache()
    val rddJoin: RDD[(Hero, HeroInfo)] = rddTempJoin.map(p => (p._2._1, p._2._2))
    val groupByCityKillNumRdd: RDD[(String, Int)] = rddJoin.filter(p => p._2.hType.equals("kill")).map(p => (p._1.city, p._2.num)).reduceByKey((u1, u2)=>u1+u2)
    //每个城市杀人总数
    groupByCityKillNumRdd.foreach(println)
    val groupByCityGuanyaNumRdd: RDD[(String, Int)] = rddJoin.filter(p => p._2.hType.equals("guanya")).map(p => (p._1.city, p._2.num)).reduceByKey((u1, u2)=>u1+u2)
    //每个城市关押总数
    groupByCityGuanyaNumRdd.foreach(println)

    //每个城市杀人最多的将军信息
    val sortByNumRdd: RDD[((String, String), Int)] = rddJoin.filter(p => p._2.hType.equals("kill")).map(p => ((p._1.city, p._1.name), p._2.num)).reduceByKey((u1, u2) => u1 + u2).sortByKey()
    sortByNumRdd.foreach(println)



    sc.stop()









  }
}
