name := "SparkService"

version := "0.1"

scalaVersion := "2.12.8"
val logbackVersion = "1.3.0-alpha10"
val sfl4sVersion = "2.0.0-alpha5"
val typesafeConfigVersion = "1.4.1"
val apacheCommonIOVersion = "2.11.0"
val scalacticVersion = "3.2.9"
val generexVersion = "1.0.2"
val AkkaVersion = "2.6.17"
val kafkaVersion = "2.8.0"

libraryDependencies ++= Seq(
//  "ch.qos.logback" % "logback-core" % logbackVersion,
//  "ch.qos.logback" % "logback-classic" % logbackVersion,
//  "org.slf4j" % "slf4j-api" % sfl4sVersion,
//  "com.typesafe" % "config" % typesafeConfigVersion,
//  "commons-io" % "commons-io" % apacheCommonIOVersion,
//  "org.scalactic" %% "scalactic" % scalacticVersion,
//  "org.scalatest" %% "scalatest" % scalacticVersion % Test,
//  "org.scalatest" %% "scalatest-featurespec" % scalacticVersion % Test,
//  "com.typesafe" % "config" % typesafeConfigVersion,
//  "com.github.mifmif" % "generex" % generexVersion,
//  "org.apache.spark" %% "spark-sql" % "3.2.0" ,
//  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.2.0" % Test,
  "org.apache.spark" %% "spark-core" % "2.4.0",
  "org.apache.kafka" % "kafka-clients" % "0.8.2.0",
  "org.apache.spark" %% "spark-streaming" % "2.4.0" ,
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.0",
)
