
import MailService.KafkaSink
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf

import java.io._
import java.util.Properties
import scala.collection.mutable.ArrayBuffer
//import com.typesafe.config.ConfigFactory
//import org.slf4j.{Logger, LoggerFactory}
//import MailService._
import org.apache.kafka.clients.producer.ProducerConfig
object SparkService {

  def main(args: Array[String]): Unit = {
//    val logger: Logger = LoggerFactory.getLogger(this.getClass)
//    val config = ConfigFactory.load("application")
    val brokers="127.0.0.1:9092"
    val topics="LogDataTopic"
    val  groupId="GRP1"

    val SparkConf = new SparkConf().setMaster("local[*]").setAppName("StreamingLogData")

    val ssc = new StreamingContext(SparkConf,Seconds(3))
//    logger.info("Spark Streaming Context instantiated")

    val sc =ssc.sparkContext

    sc.setLogLevel("OFF")

    val topicSet = topics.split(",").toSet

    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG-> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )

    val properties = new Properties()
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

    val messages = KafkaUtils.createDirectStream[String,String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String,String](topicSet,kafkaParams)
    )
    val lines = messages.map(_.value)

    lines.print()

    val kafkaSink = ssc.sparkContext.broadcast(KafkaSink(properties))

    lines.foreachRDD { rdd =>
      rdd.foreach { message =>
        kafkaSink.value.send("SparkDataTopic",message)
      }
    }
     ssc.start()
//    logger.info("Spark Streaming Context Started...")
    ssc.awaitTermination()

  }
}


