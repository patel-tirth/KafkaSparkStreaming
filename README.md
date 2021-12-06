# KafkaSparkStreaming
Kafka Spark streaming to consume date from Kafka topics and sending email to stakeholders.

To run this program:

``` sbt clean compile run```

This will start the spark program and will run continuously until termination.
The program consumes the messages from kafka by subscribing to the topic.

#### Project explanation
``` 
/src/main/scala/SparkService.scala 

This file contains the spark service that consumes data from kafka topic by creating a direct stream.
It also sends the data to another kafka topic using the KafkaSink class in /src/main/scala/MailService/KafkaSink.scala.
It uses sparkContext's brodcast method to send the kafkaSink object to ensure that the object is send only once.
```

```
/src/main/scala/MailService/KafkaSink.scala.
Class to produce data to kafka topic. All buffered messages are flushed to by the producer. This is done by adding a shutdown hook. 
```

```
/src/main/scala/MailService/MailServiceConsumer.scala.
This class uses Akka Stream connector for subscribing to Kafka topics. Email is sent to stakeholders using Amazon SES.
```
