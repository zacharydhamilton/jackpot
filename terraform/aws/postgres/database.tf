// Basic networking
resource "aws_default_vpc" "default_vpc" {
    tags = {
        name = "Default VPC"
    }
}
resource "aws_security_group" "database" {
    vpc_id = aws_default_vpc.default_vpc.id
    name = "${local.name_prefix}database-instance-sg"
    egress {
        description = "Allow all outbound"
        from_port = 0
        to_port = 0 
        protocol = -1
        cidr_blocks = [ "0.0.0.0/0" ]
    }
    tags = {
        Name = "${local.name_prefix}database-instance-sg"
    }
}
resource "aws_security_group_rule" "ec2_instance_connect_ssh" {
    type = "ingress"
    from_port = 0
    to_port = 22
    protocol = "TCP"
    cidr_blocks = [ "${local.ec2_instance_connect_ip[0]}" ]
    security_group_id = aws_security_group.database.id
}
resource "aws_security_group_rule" "debezium_cdc_connector" {
    type = "ingress"
    from_port = 0
    to_port = 5432
    protocol = "TCP"
    cidr_blocks = [ "0.0.0.0/0" ]
    security_group_id = aws_security_group.database.id
}
// Database instance
data "template_cloudinit_config" "bootstrap" {
    base64_encode = true
    part {
        content_type = "text/x-shellscript"
        content = "${file("scripts/bootstrap.sh")}"
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
data "aws_ec2_instance_type" "database" {
    instance_type = "t2.medium"
}
resource "aws_instance" "database" {
    count = local.databases
    ami = data.aws_ami.amazon_linux.id
    instance_type = data.aws_ec2_instance_type.database.instance_type
    security_groups = [ aws_security_group.database.name ]
    user_data = "${data.template_cloudinit_config.bootstrap.rendered}"
    tags = {
        Name = "${local.name_prefix}database-instance-${count.index}"
    }
}
resource "aws_eip" "database" {
    count = local.databases
    vpc = true
    instance = aws_instance.database[count.index].id
    tags = {
        Name = "${local.name_prefix}database-instance-${count.index}-eip"
    }
}
output "database_ips" {
    value = aws_eip.database.*.public_ip
}