go to kafka 

go to zookeeper.properties

dataDir=C:\\Users\\i320209\\Documents\\kafka_2.12-1.1.0\zookeeper

go to server.properties 

listeners=PLAINTEXT://127.0.0.1:9092

log.dirs=C:\\Users\\i320209\\Documents\\kafka_2.12-1.1.0\\logs\kafka-logs

start zookeeper
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

start kafka
.\bin\windows\kafka-server-start.bat .\config\server.properties

Kafka Topics
1.payment-event
2.payment-command
3.delivery-event
4.delivery-command
5.restaurant-event
6.restaurant-command


SQL databases
1.delivery
2.payment
3.restaurant

GUI for kafka  
1.KaDeck
