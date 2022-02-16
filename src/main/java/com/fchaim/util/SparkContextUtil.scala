package com.fchaim.util

import org.apache.spark.{SparkConf, SparkContext}

object SparkContextUtil {

  def getSc(master:String="local",appName:String):SparkContext = {
    val conf = new SparkConf()
    conf.setMaster(master)
    conf.setAppName(appName)
    new SparkContext(conf)
  }
}
