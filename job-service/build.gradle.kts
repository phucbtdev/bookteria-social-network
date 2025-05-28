plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.recruitment"
version = "0.0.1-SNAPSHOT"

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

val mapstructVersion = "1.5.5.Final"
val lombokMapstructBindingVersion = "0.2.0"
val springCloudVersion = "2023.0.1"
val commonVersion = "0.0.1"
val webmvcUi = "2.5.0"
val commonVer = "0.0.1"

repositories {
	mavenCentral()
	maven {
		url = uri("https://maven.pkg.github.com/phucbtdev/bookteria-social-network")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
			password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
		}
	}
}

dependencies {
	implementation("com.recruitment:common:$commonVer")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$webmvcUi")
	implementation("org.springframework.boot:spring-boot-starter-amqp")

	runtimeOnly("org.postgresql:postgresql")

	compileOnly("org.projectlombok:lombok")

	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapstructBindingVersion")
	annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
