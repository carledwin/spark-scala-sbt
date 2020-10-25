package com.sundogsoftware

import org.apache.spark._
import org.apache.log4j._

object WordCountBetterC {

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "WordCountBetterC")

    val inputTextRdd = sc.textFile("data/book.txt")

    val onlyWords = inputTextRdd.flatMap(l => l.split("\\W+"))

    val lowercaseWords = onlyWords.map(w => w.toLowerCase())

    val countWords = lowercaseWords.countByValue()

    countWords.foreach(println)


  }
}
