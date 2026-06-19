# SecOps Vulnerable Demo

Bu proje, GitHub Actions üzerinde kurulan DevSecOps/SecOps pipeline yapısını test etmek için hazırlanmış **kasıtlı olarak hatalı ve zafiyetli** bir demo uygulamadır.

> Gerçek sistemlerde kullanılmamalıdır. İçindeki secret benzeri değerler tamamen dummy/test amaçlıdır.

## Amaç

Bu repo ile aşağıdaki araçların çıktı üretmesini hedefleyebilirsin:

- SonarQube: code smell, bug, security hotspot, SQL injection, weak crypto, hardcoded secret benzeri bulgular
- Trivy FS/Image/Config: dependency, container image, Dockerfile, Kubernetes manifest ve Terraform bulguları
- GitHub Actions self-hosted runner testi
- İsteğe bağlı OWASP ZAP baseline testi

## İçerik

- Java 17 + Spring Boot 2.7.x
- H2 database
- Dockerfile
- docker-compose.yml
- Kubernetes manifest
- Terraform security group örneği
- GitHub Actions workflow

## Kasıtlı Güvenlik/Kalite Problemleri

| Alan | Örnek |
|---|---|
| SQL Injection | `UserRepository.findByUsernameUnsafe` |
| Command Injection riski | `AdminController.diagnostics` |
| Hardcoded secret | `AuthService.JWT_SECRET`, compose env |
| Weak crypto | MD5 password hashing |
| Info disclosure | Environment değişkenlerini dönen health endpoint |
| Insecure CORS | `allowedOrigins("*")` |
| H2 console açık | `/h2-console` |
| Stacktrace açık | `include-stacktrace: always` |
| Container hardening eksikliği | root user, eski base image yaklaşımı |
| Kubernetes misconfiguration | privileged container, runAsUser 0 |
| Terraform misconfiguration | SSH 0.0.0.0/0 |

## Lokal Çalıştırma

```bash
mvn -DskipTests package
docker build -t secops-vulnerable-demo:local .
docker run --rm -p 8080:8080 secops-vulnerable-demo:local
```

Alternatif:

```bash
docker compose up --build
```

## Örnek Endpointler

```bash
curl "http://localhost:8080/api/users/search?username=admin"
curl -X POST "http://localhost:8080/api/users/token?username=admin&password=admin123"
curl "http://localhost:8080/api/admin/health"
```

## GitHub Secrets

Workflow için repo settings altında şunları ekle:

- `SONAR_HOST_URL` → örnek: `http://<sonarqube-server-ip>:9000`
- `SONAR_TOKEN` → SonarQube üzerinden oluşturduğun token

## Runner Notu

Workflow `runs-on: self-hosted` olarak ayarlandı. GitHub repo üzerinde self-hosted runner aktif değilse job kuyruğa düşer veya çalışmaz.

## Beklenen Pipeline Davranışı

İlk aşamada amaç pipeline'ın fail olması değil, bulgu üretmesidir. Bu yüzden Trivy adımlarında `--exit-code 0` kullanıldı. İstersen daha sonra kalite kapısı mantığı için HIGH/CRITICAL bulgularda pipeline'ı fail edecek şekilde `--exit-code 1` yapabilirsiniz.
