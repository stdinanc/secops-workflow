terraform {
  required_version = ">= 1.3.0"
}

resource "aws_security_group" "demo_open_sg" {
  name        = "secops-demo-open-sg"
  description = "Intentionally open security group for IaC scanner testing"

  ingress {
    description = "Open SSH - intentionally unsafe"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Open app port"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
