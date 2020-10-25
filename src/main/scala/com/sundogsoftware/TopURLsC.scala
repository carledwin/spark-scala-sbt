package com.sundogsoftware


import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, current_timestamp, regexp_extract, window}

object TopURLsC {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("TopURLs")
      .master("local[*]")
      .getOrCreate()

    val accessLinesLogs = spark.readStream.text("data/logs")

    val contentSizeExp = "\\s(\\d+)$"
    val statusExp = "\\s(\\d{3})\\s"
    val generalExp = "\"(\\S+)\\s(\\S+)\\s*(\\S*)\""
    val timeExp = "\\[(\\d{2}/\\w{3}/\\d{4}:\\d{2}:\\d{2}:d{2} -\\d{4})]"
    val hostExp = "(^\\S+\\.[\\S+\\.]+\\S+\\s)"

    val logsDF = accessLinesLogs.select(
      regexp_extract(col("value"), hostExp, 1).alias("host"),
      regexp_extract(col("value"), timeExp, 1).alias("timestamp"),
      regexp_extract(col("value"), generalExp, 1).alias("method"),
      regexp_extract(col("value"), generalExp, 2).alias("endpoint"),
      regexp_extract(col("value"), generalExp, 3).alias("protocol"),
      regexp_extract(col("value"), statusExp, 1).cast("Integer").alias("status"),
      regexp_extract(col("value"), contentSizeExp, 1).cast("Integer").alias("content_size")
    )

    val logsDF2 = logsDF.withColumn("eventTime", current_timestamp())

    val endpointCounts = logsDF2
      .groupBy(window(col("eventTime"),
            "30 seconds",
            "10 seconds"),
                        col("endpoint"))
      .count

    val sortedEndpointCounts = endpointCounts.orderBy(col("count").desc)

    val query = sortedEndpointCounts.writeStream
      .outputMode("complete")
      .format("console")
      .queryName("counts")
      .start

    query.awaitTermination

    spark.stop

  }
}