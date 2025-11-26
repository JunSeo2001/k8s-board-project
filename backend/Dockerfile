# 1단계: 빌드
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

# 2단계: 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 결과 jar 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 기본 프로필 (원하면 prod 말고 default 써도 됨)
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]