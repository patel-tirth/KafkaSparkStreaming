# KafkaSparkStreaming
Kafka Spark streaming to consume date from Kafka topics and store relevant log messages in a text file.

To run this program:

``` sbt clean compile run```

This will start the spark program and will run continuously until termination.
The program consumes the messages from kafka by subscribing to the topic.