#!/usr/bin/env bash
set -euo pipefail
mvn -DskipTests package
docker build -t secops-vulnerable-demo:local .
docker run --rm -p 8080:8080 secops-vulnerable-demo:local
