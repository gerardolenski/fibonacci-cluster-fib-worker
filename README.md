# FIBONACCI Worker

For more description see: [fibonacci-cluster-docker](https://github.com/gerardolenski/fibonacci-cluster-docker) and [fibonacci-cluster-k8s](https://github.com/gerardolenski/fibonacci-cluster-k8s)

## Configuration

App can be configured by environment variables:

- `BROKER_URL` - the URI to connect to the ActiveMQ cluster
- `BROKER_USER` - the AMQ user
- `BROKER_PASSWORD` - the AMQ password
- `WORKER_QUEUE_NAME` - the name of the worker queue
- `WORKER_CONCURRENCY` - the consumer concurrency, by default `1-10`
- `TOMCAT_PORT` - the port of exposed API, by default `8080` 
 
The example configuration:
```
BROKER_URL=failover:(tcp://localhost:61616)?jms.useAsyncSend=true
BROKER_USER=admin
BROKER_PASSWORD=admin

WORKER_QUEUE_NAME=worker;
WORKER_CONCURRENCY=1-10
```