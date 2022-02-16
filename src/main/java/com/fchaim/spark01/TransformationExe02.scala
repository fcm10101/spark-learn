package com.fchaim.spark01

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object TransformationExe02 {

  /**
   * 有如下学生信息，根据给定的经验样本数据，请判断他们的性别
   * @param args
   */
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
    conf.setAppName("action算子练习2 ")
    conf.setMaster("local")

    val sc = new SparkContext(conf)

    //加载样本数据集
    val sampleRdd: RDD[String] = sc.textFile("data/stu/input/sample.txt")
    val sampleHandleRdd: RDD[(String, String, List[Double])] = sampleRdd.map(line => {
      val strAry: Array[String] = line.split(",")

      (strAry(0), strAry(4), List(strAry(1).toDouble, strAry(2).toDouble, strAry(3).toDouble))
    })

    //加载测试数据集
    val testRdd: RDD[String] = sc.textFile("data/stu/input/test.txt")
    val testHandleRdd: RDD[(String, List[Double])] = testRdd.map(line => {
      val strAry: Array[String] = line.split(",")
      (strAry(0), List(strAry(1).toDouble, strAry(2).toDouble, strAry(3).toDouble))
    })

    //生成笛卡尔积
    val carteRdd: RDD[((String, List[Double]), (String, String, List[Double]))] = testHandleRdd.cartesian(sampleHandleRdd)
   // carteRdd.foreach(println)

    val tempRdd: RDD[(String, String, Double)] = carteRdd.map(p => {
      val test: List[Double] = p._1._2
      val sample: List[Double] = p._2._3
      val distince: Double = sample.zip(test).map(p => Math.pow(p._1 - p._2, 2.0)).sum
      (p._1._1, p._2._2, distince)
    })

    //tempRdd.foreach(println)

    //sql rownumber() over()实现  先分组，组内取第一条
    val groupedRdd: RDD[(String, Iterable[(String, String, Double)])] = tempRdd.groupBy(p => p._1)
    val resultRdd: RDD[(String, String, Double)] = groupedRdd.map(p => p._2.minBy(p => p._3))
    resultRdd.foreach(println)


    sc.stop()






  }
}
