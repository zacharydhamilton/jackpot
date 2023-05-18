# PROVIDERS
# --------------------
terraform {
    required_providers {
        aws = {
            source  = "hashicorp/aws"
            version = "4.51"
        }
        confluent = {
            source = "confluentinc/confluent"
            version = "1.32.0"
        }
    }
}
# RANDOM IDS
# --------------------
resource "random_id" "confluent" {
    byte_length = 4
}
# VARS
# --------------------
variable "aws_region" {
    type = string
    default = "us-east-2"
}
# FILE OUTPUTS
# --------------------
resource "local_file" "jackpot_server_properties" {
    filename = "../apiserver/server.properties"
    content = <<-EOF
    ## Confluent Cloud Cluster
    bootstrap.servers=${substr(confluent_kafka_cluster.main.bootstrap_endpoint,11,-1)}
    security.protocol=SASL_SSL
    sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule   required username='${confluent_api_key.jackpot_server_kafka.id}'   password='${confluent_api_key.jackpot_server_kafka.secret}';
    sasl.mechanism=PLAIN
    # Confluent Cloud Schema Registry
    schema.registry.url=${confluent_schema_registry_cluster.main.rest_endpoint}
    basic.auth.credentials.source=USER_INFO
    basic.auth.user.info=${confluent_api_key.jackpot_server_sr.id}:${confluent_api_key.jackpot_server_sr.secret}
    # Confluent Cloud Ksql
    ksql.url=${substr(confluent_ksql_cluster.main.rest_endpoint, 8, length(confluent_ksql_cluster.main.rest_endpoint)-8-4)}
    ksql.port=443
    ksql.key=${confluent_api_key.jackpot_server_ksql.id}
    ksql.secret=${confluent_api_key.jackpot_server_ksql.secret}
    # Required for correctness in Apache Kafka clients prior to 2.6
    client.dns.lookup=use_all_dns_ips
    # Best practice for higher availability in Apache Kafka clients prior to 3.0
    session.timeout.ms=45000
    EOF
}
locals {
    docker_compose_template = templatefile("../docker-compose.tmpl", {
        "bootstrap-servers" = substr(confluent_kafka_cluster.main.bootstrap_endpoint,11,-1)
        "kafka-key" = confluent_api_key.jackpot_server_kafka.id
        "kafka-secret" = confluent_api_key.jackpot_server_kafka.secret
        "schema-registry-url" = confluent_schema_registry_cluster.main.rest_endpoint
        "schema-registry-key" = confluent_api_key.jackpot_server_sr.id
        "schema-registry-secret" = confluent_api_key.jackpot_server_sr.secret
        "ksql-url" = substr(confluent_ksql_cluster.main.rest_endpoint, 8, length(confluent_ksql_cluster.main.rest_endpoint)-8-4)
        "ksql-key" = confluent_api_key.jackpot_server_ksql.id
        "ksql-secret" = confluent_api_key.jackpot_server_ksql.secret
    })
}
resource "local_file" "docker_env_sh" {
    filename = "../docker-env.sh"
    content = <<-EOF
        #!/bin/sh
        export BOOTSTRAP_SERVERS="${substr(confluent_kafka_cluster.main.bootstrap_endpoint,11,-1)}"
        export KAFKA_KEY="${confluent_api_key.jackpot_server_kafka.id}"
        export KAFKA_SECRET="${confluent_api_key.jackpot_server_kafka.secret}"
        export SCHEMA_REGISTRY_URL="${confluent_schema_registry_cluster.main.rest_endpoint}"
        export SCHEMA_REGISTRY_KEY="${confluent_api_key.jackpot_server_sr.id}"
        export SCHEMA_REGISTRY_SECRET="${confluent_api_key.jackpot_server_sr.secret}"
        export KSQL_URL="${substr(confluent_ksql_cluster.main.rest_endpoint, 8, length(confluent_ksql_cluster.main.rest_endpoint)-8-4)}"
        export KSQL_PORT=443
        export KSQL_KEY="${confluent_api_key.jackpot_server_ksql.id}"
        export KSQL_SECRET="${confluent_api_key.jackpot_server_ksql.secret}" 
    EOF
}