# Usa a versão oficial do Azul Zulu 21 (Leve e otimizada)
FROM azul/zulu-openjdk-alpine:21-jre

LABEL maintainer="Erick Levy"

WORKDIR /app

COPY target/natalfy-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]