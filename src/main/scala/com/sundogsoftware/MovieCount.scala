package com.sundogsoftware

import org.apache.spark._
import org.apache.log4j._

object MovieCount {

  def main(args: Array[String]) {


    println("hello")

    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "MovieCount")

    val linesRDD = sc.textFile("data/ml-100k/u.data")

    val movies = linesRDD.map(l => l.split("\t")(1))

    val countByMovie = movies.countByValue()

    val sortedMovies = countByMovie.toSeq.sortBy(_._1)

    sortedMovies.foreach(println)
  }
}
