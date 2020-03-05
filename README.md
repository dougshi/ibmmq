# IBM MQ Demo
A Spring Boot Application for send/receiving message to IBM/MQ by mq-jms-spring-boot-starter

### Setup IBM MQ Locally
1. start IBM MQ
    ```bash
    # start ibmmq container
    docker run \
        --name ibmmq
        --env LICENSE=accept \
        --env MQ_QMGR_NAME=QM1 \
        --publish 1414:1414 \
        --publish 9445:9443 \
        --detach \
        ibmcom/mq
    ```

2. login to ibmmq console with admin/passw0rd
   * open browser https://localhost:9445/ibmmq/console/login.html
   * nagivate to Queue Manager properties->Extended and clear connection authentication
    

### Start the app:

```bash 
mvn spring-boot:run
``` 

### Test the app:

```bash
# publish text message
curl -XPOST http://localhost:8080/DEV.QUEUE.1?message=hello

# publish message using a text file
curl -XPOST -F 'file=@path/to/local/file' http://localhost:8080/DEV.QUEUE.1

# receive the message from the queue
curl -XGET http://localhost:8080/DEV.QUEUE.1

```
