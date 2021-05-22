# spring-kafka

## instruction

1. start Kafka brokers and Zookeeper with `docker-compose up`
2. run project using mvn spring-boot:run
3. use curl to send a message 
```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"type":"type1","message":"test"}' \
  http://localhost:8080/send-payload
```
4. use this to curl to send a message to multiple topics
```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"type":"type1,type2","message":"test2"}' \
  http://localhost:8080/send-payload
```