package com.sundogsoftware

import org.apache.spark._
import org.apache.log4j._

object TotalAmountSpentFkaneC {

  def parseLine(line : String): (Int, Float) = {

    val customer = line.split(",")
    val customerId = line(0).toInt
    val customerSpent = line(2).toFloat

    (customerId, customerSpent)
  }

  def main(args : Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "TotalAmountSpentFkaneC")

    val inputCustomersRdd = sc.textFile("data/customer-orders")

    val customersOrders = inputCustomersRdd.map(parseLine)

    val totalCustomerSpents = customersOrders.reduceByKey((x, y) => x + y)

    val flipped = totalCustomerSpents.map(c => (c._1, c._2))

    val totalCustomersSorted = flipped.sortByKey()

    val results = totalCustomersSorted.collect()

    results.foreach(println)
  }
}
