package com.sundogsoftware

import org.apache.spark.sql._
import org.apache.log4j._

object DataFramesDatasetC {

  case class Person(id:Int, name:String, age:Int, friends:Int)

  def main(args: Array[String]){

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("SparkDataFramesDataset")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._
    val people = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("data/fakefriends.csv")
      .as[Person]

    println("He is our inferred schema:")
    people.printSchema()

    println("Let's select the name column:")
    people.select("name").show()

    println("Filter out anyone over 21:")
    people.filter(people("age") < 21).show()

    println("Group by age:")
    people.groupBy("age").count().show()

    println("Make everyone 10 years olders:")
    people.select(people("name"), people("age") + 10).show()

    spark.stop()
  }

}
