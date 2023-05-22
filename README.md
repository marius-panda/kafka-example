# kafka-example

## learnings

> Apache kafka decouples data-source-systems from data-target systems

- created by LinkedIn
- FOSS maintainted by Confluent, IBM and Cloudera
- high performance (less than 10ms latency)
- horizontal scalability
  - can scale to 100s of brokers
  - can scale to millions of messages per second

### Use cases

- messaging system
- activity tracking
- gather metrics from many different locations
- application logs gathering
- stream processing (with the Kafka Streams API for example)
- De-coupling of system dependencies
- Integration with Spark, Flink, Storm, Hadoop and other BigData tech
- micro-services pub/sub

> Concrete example: Netflix uses Kafka for real-time recommendations

### Architecture

- source-systems
  - website events
  - pricing data
  - financial transactions
  - user interactions
- target-systems
  - databases
  - analytics
  - email system
  - audit

### Fundamentals

#### Theory

##### Kafka Topics, Partitions and Offsets

a topic is a stream of data

comparable to a table in a database

a topic can be identified by its name

supports any kind of message format (binary, json, text)

the sequence of messages are called data streams

you can not query topics

topics are being split into partitions

messages in each partition are ordered

each message within a parition gets an upcounting id, called offset

Kafka topics are immutable, messages can not be deleted from partitions

data is kept only for a limited time (default is one week)

offsets have the meaning for their respective partition

order of offsets is only given within one partition not across partitions

data is assigned randomly to a partition unless a key is provided

there can be as many partitions per topic as wanted

##### Kafka Producers

producers knows in advance to which partition they write to

in case of kafka broker has a failure, the producers will automatically recover

messages can have a key (string, number, binary, etc.) that decides to which partition the message will go

if messages have the same key, they will go to the same partition

> kafka message anatomy
> key (can be null) + value (can be null) + compression mechanism / type + headers + partition + offset + timestamp

kafka message serializer means transforming objects /data into bytes
serializers only used for keys and values

kafka producers come with common serializers (e.g. string, int, float)

kafka message key hashing is used for determining the mapping between key and partition, therefore the murmur2 algorithm is used in the default Kafka partitioner

targetPartition = Math.abs(Utils.murmur2(keyBytes)) % (numPartitions -1)

##### Kafka Consumers

pull-model, topics don t push to consumers

in case of broker failures consumers know how to recover

consumers know automatically from which broker to read

data is read from lowest offset to highest offset within each partition

consumer deserializer bundled with apache kafka and used for value and key of a message

the consumer needs to know what the expected format is for key and value

instead of changing the data type of an xisting topic yo should create a new topic with the wished data type and consumers have to be re-pgrammed to use the new topic instead

##### Consumer groups

many consumers can be aggregated to consumer groups

each consumer within a group reads from exclusive partitions

if there are more consumers than partitions in a consumer group the too many consumers will be inactive

it is possible to have multiple consumer groups on the same topic, where than from two distinct consumer groups the consumers read from the same partition just within one group each consumer has an exclusive partition to read from

that means in practice that e.g. consumer groups exist per application

consumer can be grouped by using their propery group.id

there are \_\_consumer_offsets which are committed offsets by consumers to make it possible after a consumer died to know from where to start reading again

Java consumers will automatically commit offsets (at least once)

there are 3 delivery semantics if you choose to commit manually

- at least once (usually preferred): offsets are committed after the message was read, if processing goes wrong, the message will be read again, requires the processing of the message to be idempotent
- at most once: offsets are committed as soon as messages are received, if processing goes wrong, some messages get lost
- exactly once: using the transactional API (easy with Kafka Streams API), with an external system workflow idempotent consumers are required

##### Kafka Brokers

kafka cluster assembled out of multiple kafka brokers (servers)

each broker is identified with an ID

containing certain topic partitions

after connecting to any kafka broker the consumer knows how to connect to the entire cluster

a good number to start with are 3 brokers\

each kafka broker is called a bootstrap server

kafka client connection+metadata-request and receives from the bootstrap server a list of all brokers

each brokers know the entire cluster

##### Topic replication

on production they usually have a replication factor between 2 and 3

a partition has a leader partition from which the replicas are being copied/cloned to other brokers

only one broker can be the leader for a given partition, producers an only sent data to the broker that is leader of a partition

in-sync replicas if the leaders are replicated to other brokers fast enough

consumers only read from the brokers that keep the leader-partition

since Kafka 2.4 it is possible for consumers to read from the closest replica instead of the leader partition on another broker

##### Producers Acknowledgments & Topic Durability

producers can choose to receive acknowledgments of data writes

- acks=0 not waiting might result in data loss
- acks=1 producer waiting for the leader partition, might result in limited data loss
- acks=all producer is waiting for leader and all replica partitions

> topic durability

as a rule for a replication factor of N you can permanently lose up to N-1 brokers and still recover data

##### Zookeeper

slowly disappearing

manages kafka brokers

keepos list of kafka brokers

helps with leader election

since Kafka 3.x can work w/o Zookeeper (KIP-500) using Kafka Raft instead

since Kafka 4.x will not have Zookeeper

by design operates with an odd number of servers (1,3,5,7, ...)

Zookeeper has a leader, which writes to the rest of the follower-servers (reads)

##### Kafka KRaft

with Zookeeper there were scaling issues when Kafka clusers had more than 100.000 partitions

w/o Zookeeper Kafka can scale to millions of partitions and becomes easier to maintain and set-up

one kafka broker will be the quorum leader instead

#### Starting Kafka

- starting zookeeper

`zookeeper-server-start.sh kafka_2.12-3.4.0/config/zookeeper.properties`

- start kafka-server

`kafka-server-start.sh kafka_2.12-3.4.0/config/server.properties`

data storage directory can be changed in the either `zookeeper.properties` file or `server.properties` file

e.g. with `dataDir=/your/path` for zookeeper or with `log.dirs=/your/path` for kafka itself

#### CLI

### Real World

## course material

- https://www.conduktor.io/apache-kafka-for-beginners/
