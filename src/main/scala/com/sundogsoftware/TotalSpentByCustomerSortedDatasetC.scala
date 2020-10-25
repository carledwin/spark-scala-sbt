package com.sundogsoftware

import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, IntegerType, StructType}

object TotalSpentByCustomerSortedDatasetC{

  case class CustomerOrders(cust_id:Int , item_id:Int, amount_spent:Double)

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession
      .builder
      .appName("TotalCustomerOrders")
      .master("local[*]")
      .getOrCreate()

    val customerOrdersSchema = new StructType()
      .add("cust_id", IntegerType, nullable = true)
      .add("item_id", IntegerType, nullable = true)
      .add("amount_spent", DoubleType, nullable = true)

    import spark.implicits._
    val customerDS = spark.read
      .schema(customerOrdersSchema)
      .csv("data/customer-orders.csv")
      .as[CustomerOrders]

    val totalByCustomer = customerDS
      .groupBy("cust_id")
      .agg((round(sum("amount_spent"), 2)
        .alias("total_spent")))

    val totalByCustomerSorted = totalByCustomer.sort("total_spent")
    totalByCustomerSorted.show(totalByCustomer.count.toInt)
  }
}