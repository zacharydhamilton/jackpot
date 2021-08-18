# Jackpot

## Things you'll need before getting started

1. Confluent Cloud account. This example leverages fully-managed Kafka and ksqlDB on Confluent Cloud. If you don't already have a Confluent Cloud account, you can sign up for a free trial [here](https://www.confluent.io/confluent-cloud/tryfree/).
1.  Confluent Cloud cluster. Once you have a Confluent Cloud account, you'll need a cluster. Follow the instructions in [here](https://docs.confluent.io/cloud/current/clusters/create-cluster.html) if you're unfamiliar with creating clusters. 
    > **Note**: This stuff costs money, so the recommended cluster type for development and testing cases, like the one you'll walk through here, should use the **Basic** cluster type. 
1. ksqlDB app. ksqlDB will process all of the wagers placed so you'll need to create an instance of it to get started. If you've never created a ksqlDB app before, follow the following documentation [here](https://docs.confluent.io/cloud/current/get-started/ksql.html#create-a-ksql-cloud-application-in-ccloud) in order to get a quick start. 
    > **Note**: The documentation linked above is a tutorial of its own. You should only go as far as the first step, *"Create a ksqlDB application in Confluent Cloud"*.  
1. `ccloud-cli`. You will use the `ccloud-cli` to create API credentials and find the necessary information that needs to be passed to the application before running it. If you don't already have it, you can find it [here](https://docs.confluent.io/platform/current/tutorials/examples/ccloud/docs/beginner-cloud.html).
1. Some extra cash. This is a slot machine after all, so bring what you're prepared to bet! ***Just kidding***.

## Getting started

1. The first step to getting started is cloning this repo, if you haven't already. From there, you'll have everything you need to either edit, build, or run this game.

1. Using the `ccloud-cli`, retrieve the `bootstrap-servers` value for your cluster. 
    - Configure the CLI to use your environment as the default environment for future commands. 
        ```bash
        # List the environments in your account
        $ ccloud environment list
              Id      |    Name
        +-------------+-------------+
          * env-a1b2c | zach-env

        # Use a particular environment as the default active environment
        $ ccloud environment use env-a1b2c
        Now using "env-a1b2c" as the default (active) environment.
        ```
    - List the clusters in your current environment to retrieve your cluster ID. 
        ```bash
        # List the clusters in your environment
        $ ccloud kafka cluster list
              Id      |      Name      |   Type   | Provider |  Region  | Availability | Status
        +-------------+----------------+----------+----------+----------+--------------+--------+
            lkc-c3d4e | jackpot        | STANDARD | gcp      | us-east1 | single-zone  | UP
        ```
    - Describe your cluster to retrieve the `bootstrap-servers` endpoint for the cluster.
        ```bash
        # Describe your cluster by ID
        $ ccloud kafka cluster describe lkc-c3d4e 
        +--------------+--------------------------------------------------------+
        | Id           | lkc-c3d4e                                              |
        | Name         | jackpot                                                |
        | Type         | STANDARD                                               |
        | Ingress      |                                                    100 |
        | Egress       |                                                    100 |
        | Storage      | Infinite                                               |
        | Provider     | gcp                                                    |
        | Availability | single-zone                                            |
        | Region       | us-east1                                               |
        | Status       | UP                                                     |
        | Endpoint     | SASL_SSL://pkc-e5f6g.us-east1.gcp.confluent.cloud:9092 |
        | ApiEndpoint  | https://pkac-g7h8i.us-east1.gcp.confluent.cloud        |
        | RestEndpoint | https://pkc-e5f6g.us-east1.gcp.confluent.cloud:443     |
        +--------------+--------------------------------------------------------+
        ```
        The value you will need from above is in the "Endpoint" row, and should be copied as `pkc-e5f6g.us-east1.gcp.confluent.cloud:9092`. Once you have this value, paste it into `docker-compose.yml` where you see the placeholder value of `<bootstrap-servers>`. 

1. Using the `ccloud-cli`, create API keys for your cluster so the producer client will be able to produce messages to the cluster. 
    - Generate an API key pair for the cluster using the cluster's resource ID.
        ```bash
        # Create API key pair
        $ ccloud api-key create --resource lkc-c3d4e
        It may take a couple of minutes for the API key to be ready.
        Save the API key and secret. The secret is not retrievable later.
        +---------+------------------------------------------------------------------+
        | API Key | FYGVYTUGIMDHWK6E                                                 |
        | Secret  | [hidden]                                                         |
        +---------+------------------------------------------------------------------+
        ```
        Make sure that you save the secret as the output mentions. For the purpose of this guide, copy these values into `docker-compose.yml` where you see the placeholder values of `<api-key>` and `<api-secret>`.
1. Using the `ccloud-cli`, retrieve the ksqlDB endpoint for your ksqlDB app. 
    - List the ksqlDB apps in your current cluster to get the app ID and its endpoint.
        ```bash
        # List ksqlDB apps
        $ ccloud ksql app list
            Id       |      Name      | Topic Prefix |   Kafka   | Storage |                       Endpoint                        | Status
        +------------+----------------+--------------+-----------+---------+-------------------------------------------------------+--------+
        lksqlc-i9j0k | ksqldb-jackpot | pksqlc-k1l2m | lkc-c3d4e |     500 | https://pksqlc-k1l2m.us-east1.gcp.confluent.cloud:443 | UP
        ```
        Copy the value of the endpoint into `docker-compose.yml` where you see the placeholder value of `<ksqldb-endpoint>`.

1. Using the `ccloud-cli`, create an API key pair for your ksqlDB app. 
    - Create a new key pair specifically for your ksqlDB app.
        ```bash
        # Create new API key pair for ksqlDB
        $ ccloud api-key create --resource lksqlc-i9j0k
        It may take a couple of minutes for the API key to be ready.
        Save the API key and secret. The secret is not retrievable later.
        +---------+------------------------------------------------------------------+
        | API Key | PO4YH6UWE25M3XRG                                                 |
        | Secret  | [hidden]                                                         |
        +---------+------------------------------------------------------------------+
        ```
        Make sure that you save the secret as the output mentions. For the purpose of this guide, copy these values into `docker-compose.yml` where you see the placeholder values of `<ksqldb-key>` and `<ksqldb-secret>`.

1. Create ksqlDB streams, tables, and persistent queries.
    - In the Confluent Cloud UI, navigate to your ksqlDB app. In the the editor table, use the following query to create a stream from the `wagers` topic. 
        ```sql 
        CREATE STREAM wagers (
            pot VARCHAR,
            wager BIGINT
        ) WITH (
            kafka_topic='wagers',
            value_format='JSON'
        );
        ```
    - Once the stream `wagers` has been created, you can create a persistent query that will create a table with the state of the game's pot. 
        ```sql
        CREATE TABLE thepot AS 
            SELECT
                pot,
                SUM(wager) total
            FROM wagers
            GROUP BY pot
        EMIT CHANGES;
        ```
    Once you have the table `thepot`, you should be all set to start the Jackpot application.

1. Now that all of the base setup is complete, you should be able to use `docker-compose.yml` in order to build everything.
    - Launch everything in docker.
        ```bash
        $ docker compose up -d
        ```
    Assuming everything runs correctly, you should be able to navigate to `http://localhost` in your web browser and access the Jackpot game. 