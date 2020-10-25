package com.sundogsoftware

import org.apache.spark._
import org.apache.log4j._

object FriendsByAgeC {

  def parseLine(line: String): (Int, Int) = {

    val fields = line.split(",")

    val age = fields(2).toInt

    val numFriends = fields(3).toInt

    (age, numFriends)
  }

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "FriendsByAgeC")

    val linesFAKEFriendsRDD = sc.textFile("data/fakefriends-noheader.csv")

    val rdd = linesFAKEFriendsRDD.map(parseLine)

    val totalsByAge = rdd.mapValues(age => (age, 1)).reduceByKey((age,friends) => (age._1 + friends._1, age._2 + friends._2))
    totalsByAge.foreach(println)

    val averagesByAge = totalsByAge.mapValues(e => e._1 / e._2)
    averagesByAge.foreach(println)

    val results = averagesByAge.collect()
    results.sorted.foreach(println)



  }
}
