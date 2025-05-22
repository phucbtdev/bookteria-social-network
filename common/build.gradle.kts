plugins {
	id("java-library")
	id("maven-publish")
}

group = "com.recruitment"
version = "0.0.1"

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

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.7.6")
	implementation("org.projectlombok:lombok:1.18.30")
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
}

publishing {
	publications {
		create<MavenPublication>("commonLib") {
			from(components["java"])
			groupId = "com.recruitment"
			artifactId = "common"
			version = "0.0.1"
		}
	}

	repositories {
		// Publish ra thư mục local trong build
		maven {
			name = "localCommonRepo"
			url = uri("$buildDir/repo")
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
