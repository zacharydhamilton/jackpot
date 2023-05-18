# Jackpot

## Things you'll need before getting started

1. Confluent Cloud account. This lab leverages fully-managed Kafka, ksqlDB, Kafka Connect, and Schema Registry on Confluent Cloud. If you don't already have a Confluent Cloud account, you can sign up for a free trial [here](https://www.confluent.io/confluent-cloud/tryfree/).
1. Terraform. This lab leverages Terraform to do a fair amount of the provisioning so the configurations can be just right. 
1. Docker. This lab includes Dockerfiles for each service, and public images for them in Docker hub. You should use Docker to provision all the services. 
1. (Optional) AWS. If you're going through this lab on your own, you'll need to make/provision your own instance of Postgres on AWS. Everything is provided for you in this repo, however, if you're going through this as part of a guided lab, one will be made available to you. 

## Getting started

1. First things first, clone this repo!
    ```bash
    git clone https://github.com/zacharydhamilton/jackpot.git && cd jackpot
    ```

1. To begin, the first thing you'll need to capture is a **Cloud API Key & Secret**. Start by creating this key pair. 
    > **Note**: *Now, it's important to note that "Cloud" is a specific name of a type of key in Confluent Cloud. See [here](https://docs.confluent.io/cloud/current/access-management/authenticate/api-keys/api-keys.html#cloud-cloud-api-keys) for more details.*

1. Once you have your Cloud API Key & Secret, copy them into an "environment" file. Something like `env.sh`. You can use the following command in order to create this file (replacing the placeholder with your values).
    ```bash
    echo "export CONFLUENT_CLOUD_API_KEY="<cloud-key>"\nexport CONFLUENT_CLOUD_API_SECRET="<cloud-secret>"" > env.sh
    ```

1. Once you have your handy `env.sh` file, you should source it to export the values to the console. This will allow Terraform to access them.
    ```bash
    source env.sh
    ```

1. With the secrets exported to the console, you can now start building the base infrastructure for this lab. Navigate to the Terraform directory and initialize Terraform. 
    ```bash
    cd terraform
    ```
    ```bash
    terraform init
    ```

1. Once Terraform is initialized, create the plan. After you've reviewed it, and are happy with what it's doing, apply it.
    ```bash
    terraform plan
    ```
    ```bash
    terraform apply
    ```

## Connect

With the basics set up, you can move on to creating the Connector that will capture the master data from Postgres. 

1. Navigate in the Confluent Cloud UI to the Connectors page (select your cluster if you haven't, then find "Connectors" in the left hand menu).

1. Search for and select the "Postgres CDC Source Connector".

1. Complete the various stages of the form using the following guidelines. Once completed, launch the connector.
    | **Property**                      | **Value**                                   |
    |-----------------------------------|---------------------------------------------|
    | Kafka Cluster Authentication mode | "Use an existing API key"                   |
    | Kafka API Key                     | *copy from `docker-env.sh` file*            |
    | Kafka API Secret                  | *copy from `docker-env.sh` file*            |
    | Database name                     | jackpot                                     | 
    | Database server name              | jackpot                                     |
    | SSL mode                          | disabled                                    |
    | Database hostname                 | *derived from Terraform output or provided* |
    | Database port                     | 5432                                        |
    | Database username                 | postgres                                    |
    | Database password                 | rt-j4ckp0t!                                 |
    | Output Kafka record value format  | AVRO                                        |
    | Output Kafka record key format    | JSON                                        |
    | Slot name                         | *something creative, like **camel**, it can be anything unique* |
    | Tasks                             | 1                                           |
    > **Note**: *There are multiple pages in the form. Look for all the options above, and be mindful of things like the "Show advanced properties" option.*

## Ksql

With the connector set up, you can move on to create the Ksql queries to process data. 
> **Note**: *If the instance of Ksql created by Terraform is not available yet, wait.*

1. Create the first stream in Ksql. This stream will be for the wagers placed by the players.
    ```sql
    CREATE STREAM wagers (
        jackpotPoolId STRING,
        wager INT
    ) WITH (
        KAFKA_TOPIC='wagers',
        VALUE_FORMAT='AVRO'
    );
    ```

1. Next, create the stream for the change events captured by the CDC Connector.
    ```sql
    CREATE STREAM pool_changes (
        pk STRUCT<jackpotPool VARCHAR> KEY,
        jackpotPool VARCHAR, 
        region VARCHAR
    ) WITH (
        KAFKA_TOPIC='jackpot.games.pools',
        KEY_FORMAT='JSON',
        VALUE_FORMAT='AVRO'
    );
    ```

1. With the change events captured as a stream, materialize them into a table to store their values statefully.
    ```sql
    CREATE TABLE jackpotPools WITH (
            KAFKA_TOPIC='pools',
            KEY_FORMAT='KAFKA',
            VALUE_FORMAT='AVRO'
        ) AS SELECT
            jackpotpool pool,
            LATEST_BY_OFFSET(region) AS region
        FROM pool_changes
        GROUP BY jackpotpool
    EMIT CHANGES;
    ```

1. Next, join the stream of wagers with the materialized master data in order to keep an enriched and stateful representation of the various wager pools.
    ```sql
    CREATE TABLE jackpots WITH (
            KAFKA_TOPIC='jackpots',
            KEY_FORMAT='KAFKA',
            VALUE_FORMAT='AVRO'
        ) AS SELECT 
            p.pool AS jackpotPoolId,
            LATEST_BY_OFFSET(p.region) region,
            SUM(w.wager) AS jackpot
        FROM wagers w
            JOIN jackpotPools p ON p.pool = w.jackpotPoolId
        GROUP BY p.pool
    EMIT CHANGES;
    ```

1. Finally, insert some zero's into the stream of wagers to "initialize" the wagering pools. 
    ```sql 
    INSERT INTO wagers (jackpotPoolId, wager) VALUES ('4a41a567', 0);
    INSERT INTO wagers (jackpotPoolId, wager) VALUES ('26531c2c', 0);
    INSERT INTO wagers (jackpotPoolId, wager) VALUES ('9876a556', 0);
    INSERT INTO wagers (jackpotPoolId, wager) VALUES ('8d4c0a19', 0);
    INSERT INTO wagers (jackpotPoolId, wager) VALUES ('fd5c17a6', 0);
    INSERT INTO wagers (jackpotPoolId, wager) VALUES ('5b535f6c', 0);
    ```

## API Server and React app

With everything else set up, the meat and potatoes of the architecture can be set up. Terraform should have created a `docker-env.sh` file with you secrets.

1. Make sure you're in the back base directory of the repo, then source the secrets file `docker-env.sh` so that the following docker commands will have the secrets' values.
    ```bash
    source docker-env.sh
    ```

1. Run the following command to start the API server.
    ```bash
    docker compose up apiserver -d
    ```
    Since the service is running detached, if you want to see the logs in the console use the follow command. You can command interrupt this whenever you please or not at all.
    ```bash
    docker compose logs apiserver -f
    ```

1. With the API server up and running, run the following command to start the React app.
    ```bash
    docker compose up react -d
    ```
    Like the above, if you want to see the access logs in the console for React and Nginx, use the follding command and interrupt it as you see fit.
    ```bash
    docker compose logs react -f
    ```

1. Now, click the following or type it into your web browser: [http://localhost](http://localhost). 

1. Once connected to the web page, you should be able to play with the slot machine by typing your name, selecting a wager pool, and typing in a bet!

## Cleanup

1. When you're done with the resources and ready to clean up, start by shutting down the containers.
    ```bash
    docker compose down
    ```

1. Next, tear down your Confluent environment and resources using Terraform.
    ```bash
    cd terraform
    ```
    ```bash
    terraform destroy
    ```