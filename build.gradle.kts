plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.pankajkumarrout.tasks"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Spring Boot Web for REST API (GET, POST, PUT, DELETE)
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Spring Boot Data JPA for Database Access
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// PostgreSQL Driver
	implementation("org.postgresql:postgresql")

	// Jackson for JSON Processing
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Kotlin Reflection and Stdlib
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// Lombok (Optional: For Reducing Boilerplate Code)
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// CSV Processing Library (Apache Commons CSV)
	implementation("org.apache.commons:commons-csv:1.10.0")

	// Spring Boot DevTools (Optional: For Live Reload)
	developmentOnly("org.springframework.boot:spring-boot-devtools")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
