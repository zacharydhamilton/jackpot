// Basic networking
resource "aws_default_vpc" "default_vpc" {
    tags = {
        name = "Default VPC"
    }
}
resource "aws_security_group" "web" {
    vpc_id = aws_default_vpc.default_vpc.id
    name = "${local.name_prefix}web-instances-sg"
    egress {
        description = "Allow all outbound"
        from_port = 0
        to_port = 0 
        protocol = -1
        cidr_blocks = [ "0.0.0.0/0" ]
    }
    tags = {
        Name = "${local.name_prefix}web-instances-sg"
    }
}
resource "aws_security_group_rule" "ec2_instance_connect_ssh" {
    type = "ingress"
    from_port = 0
    to_port = 22
    protocol = "TCP"
    cidr_blocks = [ "${local.ec2_instance_connect_ip[0]}" ]
    security_group_id = aws_security_group.web.id 
}
resource "aws_security_group_rule" "http" {
    type = "ingress"
    from_port = 0
    to_port = 80
    protocol = "TCP"
    cidr_blocks = [ "0.0.0.0/0" ]
    security_group_id = aws_security_group.web.id
}
resource "aws_security_group_rule" "http_alt" {
    type = "ingress"
    from_port = 0
    to_port = 8080
    protocol = "TCP"
    cidr_blocks = [ "0.0.0.0/0" ]
    security_group_id = aws_security_group.web.id
}
// Web instances
data "cloudinit_config" "web_instances" {
    gzip = false
    base64_encode = false 
    part {
        content_type = "text/x-shellscript"
        filename = "start.sh"
        content = <<-EOF
            #!/bin/sh
            yum update -y
            yum install docker -y
            service docker start
        EOF
    }
    part {
        content_type = "text/cloud-config"
        filename = "cloud-config.yaml"
        content = <<-EOF
            #cloud-confnig
            ${jsonencode({
                write_files = [
                    {
                        path = "/home/ec2-user/docker-env.sh"
                        permissions = "0777"
                        encoding = "b64"
                        content = filebase64("../../../docker-env.sh")
                    }
                ]
            })}
        EOF
    }
    part {
        content_type = "text/x-shellscript"
        filename = "run.sh"
        content = <<-EOF
            #!/bin/sh
            source /home/ec2-user/docker-env.sh
            docker run -p 8080:8080 -d --name apiserver \
            -e CONFIG_TYPE=ENV \
            -e BOOTSTRAP_SERVERS=$BOOTSTRAP_SERVERS \
            -e KAFKA_KEY=$KAFKA_KEY \
            -e KAFKA_SECRET=$KAFKA_SECRET \
            -e SCHEMA_REGISTRY_URL=$SCHEMA_REGISTRY_URL \
            -e SCHEMA_REGISTRY_KEY=$SCHEMA_REGISTRY_KEY \
            -e SCHEMA_REGISTRY_SECRET=$SCHEMA_REGISTRY_SECRET \
            -e KSQL_URL=$KSQL_URL \
            -e KSQL_PORT=443 \
            -e KSQL_KEY=$KSQL_KEY \
            -e KSQL_SECRET=$KSQL_SECRET \
            zachhamilton/rt-jackpot-apiserver:1.0
            export PUBLIC_IP=$(curl http://checkip.amazonaws.com)
            docker run -p 80:80 -d --name react \
            -e APISERVER=http://$PUBLIC_IP:8080 \
            zachhamilton/rt-jackpot-react:2.0
        EOF
    }
}
data "aws_ami" "amazon_linux" {
    owners = [ "amazon" ]
    most_recent = true
    filter {
        name = "name"
        values = [ "amzn2-ami-kernel-5.10-hvm-*" ]
    }
    filter {
        name = "architecture"
        values = [ "x86_64" ]
    }
}
data "aws_ec2_instance_type" "web" {
    instance_type = "t2.medium"
}
resource "aws_instance" "web" {
    count = 1
    ami = data.aws_ami.amazon_linux.id
    instance_type = data.aws_ec2_instance_type.web.instance_type
    security_groups = [ aws_security_group.web.name ]
    user_data = "${data.cloudinit_config.web_instances.rendered}"
    tags = {
        Name = "${local.name_prefix}web-instances-${count.index}"
    }
}
resource "aws_eip" "web" {
    count = 1
    vpc = true
    instance = aws_instance.web[count.index].id
    tags = {
        Name = "${local.name_prefix}web-instances-${count.index}-eip"
    }
}
output "instance_ips" {
    value = aws_eip.web.*.public_ip
}