package MailService
import com.amazonaws.services.simpleemail.model.{Body, Content, Destination, Message, SendEmailRequest}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}
import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Sink
import org.apache.kafka.common.serialization.StringDeserializer
import com.amazonaws.services.simpleemail.{AmazonSimpleEmailService, AmazonSimpleEmailServiceClientBuilder}
import com.typesafe.config.{Config, ConfigFactory}
object  MailServiceConsumer extends App {
  implicit val system: ActorSystem = ActorSystem("consumer-sys")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val config: Config = ConfigFactory.load("application")
  val consumerConfig = config.getConfig("akka.kafka.consumer")
  val consumerSettings = ConsumerSettings(consumerConfig, new StringDeserializer, new StringDeserializer)

  val consume = Consumer
    .plainSource(consumerSettings, Subscriptions.topics("SparkDataTopic"))
    .runWith(
      Sink.foreach(record => {
        print(record.value())
          AmazonSimpleEmailServiceClientBuilder
            .defaultClient()
            .sendEmail(emailRequest(record.value()))
      })
    )

  def emailRequest(data:String): SendEmailRequest = {
    val from = "tpatel79@uic.edu"
    val to = "tpatel79@uic.edu"
    val subject = "URGENT: Log update to your application"

    new SendEmailRequest().withDestination(
      new Destination().withToAddresses(to))
      .withMessage(new Message().withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(data)))
        .withSubject(new Content().withCharset("UTF-8").withData(subject))).withSource(from)
  }
  consume onComplete {
    case Success(_) => println("Done"); system.terminate()
    case Failure(err) => println(err.toString); system.terminate()
  }
}
