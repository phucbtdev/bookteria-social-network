plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    java
}

group = "com.recruitment"
version = "0.0.1-SNAPSHOT"
description = "Identity service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

val lombokVersion = "1.18.30"
val mapstructVersion = "1.5.5.Final"
val lombokMapstructBindingVersion = "0.2.0"
val spotlessVersion = "2.43.0"
val springCloudVersion = "2023.0.1"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/phucbtdev/bookteria-social-network")
        credentials {
            username = findProperty("gpr.user") as String? ?: System.getenv("GH_USERNAME")
            password = findProperty("gpr.key") as String? ?: System.getenv("GH_TOKEN")
        }
    }
}

dependencies {
    implementation("com.recruitment:common:0.0.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // MapStruct
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0") // Sử dụng phiên bản mới nhất
    // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
    implementation("org.springframework.kafka:spring-kafka:3.3.4")
    //runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.postgresql:postgresql")
    //https://mvnrepository.com/artifact/org.springframework/spring-jdbc
    runtimeOnly("org.postgresql:postgresql")

    //Lombok (as annotationProcessor)
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapstructBindingVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2:2.2.224")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
