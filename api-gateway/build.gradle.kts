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

repositories {
	mavenCentral()
}
extra["springCloudVersion"] = "2023.0.1"
dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// https://mvnrepository.com/artifact/io.projectreactor/reactor-test
	testImplementation("io.projectreactor:reactor-test:3.7.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	// https://mvnrepository.com/artifact/org.projectlombok/lombok

	compileOnly("org.projectlombok:lombok:1.18.30")

	annotationProcessor("org.projectlombok:lombok:1.18.30")


}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}
tasks.withType<Test> {
	useJUnitPlatform()
}
