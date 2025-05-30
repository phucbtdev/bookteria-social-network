plugins {
	id("java-library")
	id("maven-publish")
}

group = "com.recruitment"
version = "1.0.0"

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
		create<MavenPublication>("mavenJava") {
			from(components["java"])
			groupId = "com.recruitment"
			artifactId = "common"
			version = "1.0.0"
		}
	}

	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/phucbtdev/bookteria-social-network")
			credentials {
				username = findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
				password = findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
			}
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
