
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import java.io._
import scala.collection.mutable.ArrayBuffer
//import com.typesafe.config.ConfigFactory
//import org.slf4j.{Logger, LoggerFactory}
//import MailService._
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

    val messages = KafkaUtils.createDirectStream[String,String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String,String](topicSet,kafkaParams)
    )
    val lines = messages.map(_.value)

//    lines.print()
    val arr = new ArrayBuffer[String]()
    lines.foreachRDD {
      arr ++= _.collect()
    }
    arr.foreach(a => print(a))
//    val file = ssc.textFileStream("./results")
//    lines.foreachRDD(t=> {
//      t.saveAsTextFile("./results/file1.csv")
//    })
    val file = new File("./results/result1.txt")
    file.createNewFile()
    val bw = new BufferedWriter(new FileWriter(file))

    for(line <- arr){
      bw.write(line)
    }
    bw.close()
     ssc.start()
//    logger.info("Spark Streaming Context Started...")
    ssc.awaitTermination()

  }
}


