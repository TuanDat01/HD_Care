# Sử dụng base image là Ubuntu
FROM ubuntu:latest

# Thông tin tác giả
LABEL authors="ADMIN"

# Cài đặt các dependencies cần thiết
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Đặt biến môi trường JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

# Copy file Maven project vào container
WORKDIR /app
COPY . /app

# Biên dịch Maven project
RUN mvn clean package -DskipTests

# Expose port để ứng dụng có thể truy cập
EXPOSE 8082

# Command để chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "target/PD_project-0.0.1-SNAPSHOT.jar"]
