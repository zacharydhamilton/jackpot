# ENV
# --------------------
resource "confluent_environment" "main" {
    display_name = "realtime-jackpot"
}
# TF MANAGER
# --------------------
resource "confluent_service_account" "app_manager" {
    display_name = "app-manager-${random_id.confluent.hex}"
    description = "app-manager for 'jackpot'"
}
resource "confluent_role_binding" "app_manager_env_admin" {
    principal = "User:${confluent_service_account.app_manager.id}"
    role_name = "EnvironmentAdmin"
    crn_pattern = confluent_environment.main.resource_name
}
resource "confluent_api_key" "app_manager_sr" {
    display_name = "app-manager-sr-${random_id.confluent.hex}"
    description = "app-manager-sr-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.app_manager.id
        api_version = confluent_service_account.app_manager.api_version
        kind = confluent_service_account.app_manager.kind
    }
    managed_resource {
        id = confluent_schema_registry_cluster.main.id
        api_version = confluent_schema_registry_cluster.main.api_version
        kind = confluent_schema_registry_cluster.main.kind
        environment {
            id = confluent_environment.main.id
        }
    }
    depends_on = [
        confluent_service_account.app_manager,
        confluent_role_binding.app_manager_env_admin
    ]
}
resource "confluent_api_key" "app_manager_kafka" {
    display_name = "app-manager-kafka-${random_id.confluent.hex}"
    description = "app-manager-kafka-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.app_manager.id
        api_version = confluent_service_account.app_manager.api_version
        kind = confluent_service_account.app_manager.kind
    }
    managed_resource {
        id = confluent_kafka_cluster.main.id
        api_version = confluent_kafka_cluster.main.api_version
        kind = confluent_kafka_cluster.main.kind
        environment {
            id = confluent_environment.main.id
        }
    }
    depends_on = [
        confluent_service_account.app_manager,
        confluent_role_binding.app_manager_env_admin
    ]
}
# SCHEMA REGISTRY
# --------------------
data "confluent_schema_registry_region" "main" {
    cloud = "AWS"
    region = var.aws_region
    package = "ADVANCED"
}
resource "confluent_schema_registry_cluster" "main" {
    package = data.confluent_schema_registry_region.main.package
    environment {
        id = confluent_environment.main.id
    }
    region {
        id = data.confluent_schema_registry_region.main.id
    }
}
resource "confluent_schema_registry_cluster_config" "main" {
    schema_registry_cluster {
        id = confluent_schema_registry_cluster.main.id 
    }
    rest_endpoint = confluent_schema_registry_cluster.main.rest_endpoint
    compatibility_level = "NONE"
    credentials {
        key = confluent_api_key.app_manager_sr.id
        secret = confluent_api_key.app_manager_sr.secret
    }
}
resource "confluent_schema_registry_cluster_mode" "main" {
    schema_registry_cluster {
        id = confluent_schema_registry_cluster.main.id
    }
    rest_endpoint = confluent_schema_registry_cluster.main.rest_endpoint
    mode = "READWRITE"
    credentials {
        key = confluent_api_key.app_manager_sr.id 
        secret = confluent_api_key.app_manager_sr.secret
    }
}
resource "confluent_schema" "all" {
    schema_registry_cluster {
        id = confluent_schema_registry_cluster.main.id 
    }
    rest_endpoint = confluent_schema_registry_cluster.main.rest_endpoint
    subject_name = "wagers-value"
    format = "AVRO"
    schema = file("../apiserver/schemas/WagerRequest.avsc")
    credentials {
        key = confluent_api_key.app_manager_sr.id
        secret = confluent_api_key.app_manager_sr.secret
    }
}
# KAFKA
# --------------------
resource "confluent_kafka_cluster" "main" {
    display_name = "kafka-jackpot"
    availability = "SINGLE_ZONE"
    cloud = "AWS"
    region = var.aws_region
    basic {}
    environment {
        id = confluent_environment.main.id 
    }
}
# KSQL
# --------------------
resource "confluent_service_account" "ksql" {
    display_name = "ksql-${random_id.confluent.hex}"
    description = "service account for 'ksql-jackpot-${random_id.confluent.hex}'"
}
resource "confluent_role_binding" "ksql_sr" {
    principal = "User:${confluent_service_account.ksql.id}"
    role_name = "ResourceOwner"
    crn_pattern = format("%s/%s", confluent_schema_registry_cluster.main.resource_name, "subject=*")
}
resource "confluent_role_binding" "ksql_kafka" {
    principal = "User:${confluent_service_account.ksql.id}"
    role_name = "CloudClusterAdmin"
    crn_pattern = confluent_kafka_cluster.main.rbac_crn
}
resource "confluent_api_key" "ksql_sr" {
    display_name = "ksql-sr-${random_id.confluent.hex}"
    description = "ksql-sr-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.ksql.id
        api_version = confluent_service_account.ksql.api_version
        kind = confluent_service_account.ksql.kind
    }
    managed_resource {
        id = confluent_schema_registry_cluster.main.id
        api_version = confluent_schema_registry_cluster.main.api_version
        kind = confluent_schema_registry_cluster.main.kind
        environment {
            id = confluent_environment.main.id 
        }
    }
    depends_on = [
        confluent_role_binding.ksql_sr
    ]
}
resource "confluent_api_key" "ksql_kafka" {
    display_name = "ksql-kafka-${random_id.confluent.hex}"
    description = "ksql-kafka-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.ksql.id
        api_version = confluent_service_account.ksql.api_version
        kind = confluent_service_account.ksql.kind
        
    }
    managed_resource {
        id = confluent_kafka_cluster.main.id
        api_version = confluent_kafka_cluster.main.api_version
        kind = confluent_kafka_cluster.main.kind
        environment {
            id = confluent_environment.main.id 
        }
    }
    depends_on = [
        confluent_role_binding.ksql_kafka
    ]
}
resource "confluent_ksql_cluster" "main" {
    display_name = "ksql-jackpot-${random_id.confluent.hex}"
    csu = 1 
    environment {
        id = confluent_environment.main.id
    }
    kafka_cluster {
        id = confluent_kafka_cluster.main.id
    }
    credential_identity {
        id = confluent_service_account.ksql.id
    }
    depends_on = [
        confluent_schema_registry_cluster.main
    ]
}
# JACKPOT SERVER ACCESS
# --------------------
resource "confluent_service_account" "jackpot_server" {
    display_name = "jackpot-server-${random_id.confluent.hex}"
    description = "service account for jackpot-server"
}
resource "confluent_role_binding" "jackpot_server_sr" {
    principal = "User:${confluent_service_account.jackpot_server.id}"
    role_name = "ResourceOwner"
    crn_pattern = format("%s/%s", confluent_schema_registry_cluster.main.resource_name, "subject=*")
}
resource "confluent_role_binding" "jackpot_server_kafka" {
    principal = "User:${confluent_service_account.jackpot_server.id}"
    role_name = "CloudClusterAdmin"
    crn_pattern = confluent_kafka_cluster.main.rbac_crn
}
resource "confluent_api_key" "jackpot_server_sr" {
    display_name = "jackpot-server-sr-${random_id.confluent.hex}"
    description = "jackpot-server-sr-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.jackpot_server.id
        api_version = confluent_service_account.jackpot_server.api_version
        kind = confluent_service_account.jackpot_server.kind
    }
    managed_resource {
        id = confluent_schema_registry_cluster.main.id 
        api_version = confluent_schema_registry_cluster.main.api_version
        kind = confluent_schema_registry_cluster.main.kind
        environment {
            id = confluent_environment.main.id 
        }
    }
    depends_on = [
        confluent_role_binding.jackpot_server_sr
    ]
}
resource "confluent_api_key" "jackpot_server_kafka" {
    display_name = "jackpot-server-kafka-${random_id.confluent.hex}"
    description = "jackpot-server-kafka-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.jackpot_server.id
        api_version = confluent_service_account.jackpot_server.api_version
        kind = confluent_service_account.jackpot_server.kind
    }
    managed_resource {
        id = confluent_kafka_cluster.main.id
        api_version = confluent_kafka_cluster.main.api_version
        kind = confluent_kafka_cluster.main.kind
        environment {
            id = confluent_environment.main.id 
        }
    }
    depends_on = [
        confluent_role_binding.jackpot_server_kafka
    ]
}
resource "confluent_api_key" "jackpot_server_ksql" {
    display_name = "jackpot-server-ksql-${random_id.confluent.hex}"
    description = "jackpot-server-ksql-${random_id.confluent.hex}"
    owner {
        id = confluent_service_account.jackpot_server.id
        api_version = confluent_service_account.jackpot_server.api_version
        kind = confluent_service_account.jackpot_server.kind
    }
    managed_resource {
        id = confluent_ksql_cluster.main.id 
        api_version = confluent_ksql_cluster.main.api_version
        kind = confluent_ksql_cluster.main.kind
        environment {
            id = confluent_environment.main.id 
        }
    }
    # depends_on = [
    #     confluent_role_binding.jackpot_server_ksql
    # ]
}
# JACKPOT SERVER TOPICS
# --------------------
resource "confluent_kafka_topic" "wagers" {
    topic_name = "wagers"
    rest_endpoint = confluent_kafka_cluster.main.rest_endpoint
    partitions_count = 1
    kafka_cluster {
        id = confluent_kafka_cluster.main.id
    }
    credentials {
        key = confluent_api_key.app_manager_kafka.id
        secret = confluent_api_key.app_manager_kafka.secret
    }
}