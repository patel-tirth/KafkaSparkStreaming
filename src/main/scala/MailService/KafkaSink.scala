package MailService

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
// src: https://blog.allegro.tech/2015/08/spark-kafka-integration.html

class KafkaSink(createProducer: () => KafkaProducer[String, String]) extends Serializable {

  lazy val producer = createProducer()

  def send(topic: String, value: String): Unit = producer.send(new ProducerRecord(topic, value))
}
object KafkaSink {
  def apply(properties: Properties): KafkaSink = {
    val f = () => {
      val producer = new KafkaProducer[String, String](properties)

      sys.addShutdownHook {
        producer.close()
      }
      producer
    }
    new KafkaSink(f)
  }
}