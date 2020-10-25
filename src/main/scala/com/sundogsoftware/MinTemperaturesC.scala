package com.sundogsoftware

import org.apache.spark._
import org.apache.log4j._
import scala.math.min

object MinTemperaturesC {

  def parseLine(line: String): (String, String, Float) = {

    val fields = line.split(",")
    val stationID = fields(0)
    val entryType = fields(2)
    val temperature = fields(3).toFloat * 0.1f * (9.0f / 5.0f) + 32.0f
    (stationID, entryType, temperature)
  }

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "MinTemperaturesC")

    val linesRDD = sc.textFile("data/1800.csv")

    val parsedLines = linesRDD.map(parseLine)

    val minTemps = parsedLines.filter(t => t._2 == "TMIN")

    val stationTemps = minTemps.map(t => (t._1, t._3.toFloat))

    val reducedByTempStationId = stationTemps.reduceByKey((station, temperature) => min(station, temperature))

    val stations = reducedByTempStationId.collect()

    for(station <- stations.sorted){

      val stat = station._1
      val temp = station._2
      val formattedTemp = f"$temp%.2f F"
      println(s"$stat minimun temperature: $formattedTemp")
    }
  }

}