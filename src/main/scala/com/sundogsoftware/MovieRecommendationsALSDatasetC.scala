package com.sundogsoftware

import org.apache.log4j._
import org.apache.spark.ml.recommendation._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, LongType, StringType, StructType}

import scala.collection.mutable

object MovieRecommendationsALSDatasetC{

  case class MoviesNames(movieId:Int, movieTitle:String)
  case class Rating(userID:Int, movieID:Int, rating:Float)

  def getMovieName(movieNames:Array[MoviesNames], movieId:Int): String ={
    val result = movieNames.filter(_.movieId == movieId)(0)
    result.movieTitle
  }
  // set the argument 0 to inform the userid
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("ALSMovieRecommendationALS")
      .master("local[*]")
      .getOrCreate()

    println("Loading movie names...")

    val moviesNamesSchema = new StructType()
      .add("movieID", IntegerType, nullable=true)
      .add("movieTitle", StringType, nullable=true)

    val moviesRatingSchema = new StructType()
      .add("userID", IntegerType, nullable= true)
      .add("movieID", IntegerType, nullable= true)
      .add("rating", IntegerType, nullable= true)
      .add("timestamp", LongType, nullable= true)

    import spark.implicits._

    val movieNamesDS = spark.read
      .option("sep", "|")
      .option("charsert", "ISO-8859-1")
      .schema(moviesNamesSchema)
      .csv("data/ml-100k/u.item")
      .as[MoviesNames]

    val movieNamesList = movieNamesDS.collect

    val ratingsDS = spark.read
      .option("sep","\t")
      .schema(moviesRatingSchema)
      .csv("data/ml-100k/u.data")
      .as[Rating]

    println("\n Training recommendation model...")

    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol("userID")
      .setItemCol("movieID")
      .setRatingCol("rating")

    val model = als.fit(ratingsDS)

    val userID:Int = args(0).toInt

    val users = Seq(userID).toDF("userID")

    val recommendations = model.recommendForUserSubset(users, 10)

    println("\n Top 10 recommendations for user ID " + userID + ":")

    for(userRecs <- recommendations){

      val myRecs = userRecs(1)

      val temp = myRecs.asInstanceOf[mutable.WrappedArray[Row]]

      for(rec <- temp){

        val movie = rec.getAs[Int](0)

        val rating = rec.getAs[Float](1)

        val movieName = getMovieName(movieNamesList, movie)

        println(movieName, rating)
      }
    }

    spark.stop
  }
}
