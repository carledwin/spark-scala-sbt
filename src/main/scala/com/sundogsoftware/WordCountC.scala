package com.sundogsoftware

import org.apache.spark._
import org.apache.log4j._

object WordCountC {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "WordCountC")

    val inputTextRDD = sc.textFile("data/book.txt")

    val words = inputTextRDD.flatMap( l => l.split(" "))

    val wordCounts = words.countByValue()

    wordCounts.foreach(println)
  }

}