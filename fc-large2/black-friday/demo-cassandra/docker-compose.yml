version: "3.8"

services:
  cassandra-node-0:
    image: cassandra
    environment:
      - CASSANDRA_SEEDS=cassandra-node-0 
      - CASSANDRA_CLUSTER_NAME=MyCluster
      - CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch
      - CASSANDRA_DC=datacenter11
    ports:
      - "7000:7000"   # 노드간 클러스터 내부 통신
      - "7001:7001"   # 노드간 보안 통신에 사용
      - "9042:9042"   # CQL 클라이언트와 통신

  # cassandra-node-1:
  #   image: cassandra
  #   environment:
  #     - CASSANDRA_SEEDS=cassandra-node-0 
  #     - CASSANDRA_CLUSTER_NAME=MyCluster
  #     - CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch
  #     - CASSANDRA_DC=datacenter11
  #   ports:
  #     - "17000:7000"
  #     - "17001:7001"
  #     - "19042:9042"

  # cassandra-node-2:
  #   image: cassandra
  #   environment:
  #     - CASSANDRA_SEEDS=cassandra-node-0 
  #     - CASSANDRA_CLUSTER_NAME=MyCluster
  #     - CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch
  #     - CASSANDRA_DC=datacenter11
  #   ports:
  #     - "27000:7000"
  #     - "27001:7001"
  #     - "29042:9042"

    



