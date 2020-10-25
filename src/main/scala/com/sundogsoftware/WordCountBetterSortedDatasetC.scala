package com.sundogsoftware

import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object WordCountBetterSortedDatasetC{

  case class Book(value:String)

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("WordCount")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._
    val input  = spark.read.text("data/book.txt").as[Book]

    val words = input
      .select(explode(split($"value", "\\W+"))
      .alias("word"))
      .filter($"word" =!= "")

    val lowercaseWords = words.select(lower($"word").alias("word"))

    val wordCounts = lowercaseWords.groupBy("word").count()

    val wordCountsSorted = wordCounts.sort("count")

    wordCountsSorted.show(wordCountsSorted.count().toInt)

    println("RDD ============================================================")
    //ANOTHER WAY TO DO IT (BLENDING RDD's AND DATASETs)
    val bookRDD = spark.sparkContext.textFile("data/book.txt")
    val wordsRDD = bookRDD.flatMap(x => x.split("\\W+"))
    val wordsDS = wordsRDD.toDS()

    val lowercaseWordsDS = wordsDS.select(lower($"value").alias("word"))
    val wordCountsDS = lowercaseWordsDS.groupBy("word").count()
    val wordCountsSortedDS = wordCountsDS.sort("count")

    wordCountsSortedDS.show(wordCountsSortedDS.count.toInt)


  }
}