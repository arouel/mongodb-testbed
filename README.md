# mongodb-testbed

## Requirements

Multi-document transactions are available for replica sets only. Transactions for sharded clusters are scheduled for MongoDB 4.2.

To build a development environment that supports multi-document transactions with MongoDB 4.0 we are using docker.  

First of all, create a docker network.

```
docker network create my-mongo-cluster
```

The next step is to create the MongoDB docker containers and start them.

```
docker run -p 27017:27017 --name mongo-node1 -d --net my-mongo-cluster mongo --replSet "rs0"
docker run --name mongo-node2 -d --net my-mongo-cluster mongo --replSet "rs0"
docker run --name mongo-node3 -d --net my-mongo-cluster mongo --replSet "rs0"
```

In order to form a replica set a configuration needs to be applied to one of the nodes.

```
docker exec -it mongo-node1 mongo
```

To initialize the replication a config object with connection details of all the member servers is required.

```
rs.initiate({
  _id: "rs0",
  version: 1,
  members: [
    { "_id" : 0, "host" : "mongo-node1:27017" },
    { "_id" : 1, "host" : "mongo-node2:27017" },
    { "_id" : 2, "host" : "mongo-node3:27017" }
  ],
  settings: {
  }
})
```

The server you're connected to will be elected as a member of the replica set. By running `rs.status()`, you can view the status of other MongoDB members within the replica set.
