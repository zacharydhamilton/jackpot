terraform {
    required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}
provider "aws" {
    region = local.aws_region
    default_tags {
        tags = {
            owner_email = "zhamilton@confluent.io"
        }
    }
}

locals {
    name_prefix = "rt-jackpot-"
    aws_region = "us-east-2"
    databases = 1
}
# Gather all the service ips from aws
data "http" "ec2_instance_connect" {
    url = "https://ip-ranges.amazonaws.com/ip-ranges.json"
}
# Specifically get the ec2 instance connect service ip so it can be whitelisted
locals {
    ec2_instance_connect_ip = [ for e in jsondecode(data.http.ec2_instance_connect.response_body)["prefixes"] : e.ip_prefix if e.region == "${local.aws_region}" && e.service == "EC2_INSTANCE_CONNECT" ]
}