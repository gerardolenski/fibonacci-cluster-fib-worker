# FIBONACCI Worker

For more description see: [fibonacci-cluster-docker](https://github.com/gerardolenski/fibonacci-cluster-docker)
and [fibonacci-cluster-k8s](https://github.com/gerardolenski/fibonacci-cluster-k8s)

## Requirements

- Java 21
- Artemis
- Redis

## Configuration

App can be configured by environment variables:

- `BROKER_URL` - the URI to connect to the ActiveMQ cluster
- `BROKER_USER` - the AMQ user
- `BROKER_PASSWORD` - the AMQ password
- `WORKER_QUEUE_NAME` - the name of the worker queue
- `WORKER_CONCURRENCY` - the consumer concurrency, by default `1-10`
- `JMS_SESSION_CACHE_SIZE` - the size of the cache for `SessionConnectionFactory`, by default `5`


- `FIB_CALC_CACHE_ACTIVATED` - feature toggle of the Fibonacci number calculation cache (in Redis), by default `true`
- `REDIS_HOST` - the host address of the Redis database
- `REDIS_PORT` - the port of the Redis database, by default `6379`
- `REDIS_KEY_PREFIX` - the prefix for all keys, by default `fib-worker-`
- `REDIS_CACHE_TTL` - the TTL for the cache entry, by default `10m`

- `TOMCAT_PORT` - the port of exposed API, by default `8080`

The example configuration:

```
BROKER_URL=failover:(tcp://localhost:61616)?jms.useAsyncSend=true
BROKER_USER=admin
BROKER_PASSWORD=admin

WORKER_QUEUE_NAME=worker;
WORKER_CONCURRENCY=1-10

REDIS_HOST=localhost
```