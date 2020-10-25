package com.sundogsoftware

import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object MostPopularSuperheroDatasetC {

  case class SuperHeroNames(id:Int, name:String)
  case class SuperHero(value:String)

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("MostPopularSuperhero")
      .master("local[*]")
      .getOrCreate()

    val superHeroNamesSchema = new StructType()
      .add("id", IntegerType, nullable=true)
      .add("name", StringType, nullable=true)

    import spark.implicits._
    val names = spark.read
      .schema(superHeroNamesSchema)
      .option("sep", " ")
      .csv("data/Marvel-names.txt")
      .as[SuperHeroNames]

    val lines = spark.read
      .text("data/Marvel-graph.txt")
      .as[SuperHero]

    val connections =lines
      .withColumn("id", split(col("value"), " ") (0))
      .withColumn("connections", size(split(col("value"), " ")) -1)
      .groupBy("id").agg(sum("connections").alias("connections"))

    val mostPopular = connections
      .sort($"connections".desc)
      .first()

    val mostPopularName = names
      .filter($"id" === mostPopular(0))
      .select("name")
      .first()

    println(s"${mostPopularName(0)} is most popular superhero with ${mostPopular(1)} co-appearances.")



  }
}
