plugins {
  id("org.springframework.boot") version "3.5.0"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  kotlin("plugin.jpa") version "1.9.25"
  kotlin("kapt") version "2.1.21"
}

group = "com.dustho"
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
  // Spring Boot Starter
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")

  // Kotlin necessary
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  // Hibernate necessary
  implementation("com.fasterxml:classmate:1.5.1")
  implementation("org.antlr:antlr4-runtime:4.13.1")

  // Swagger UI
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

  // Query DSL
  implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
  kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

  // DB Drivers
  runtimeOnly("com.h2database:h2")
  runtimeOnly("com.mysql:mysql-connector-j")

  // Test - Spring with kotlin
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

  // Test - Spring with kotest
  testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
  testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
  testImplementation("io.kotest:kotest-assertions-core:5.9.1")
  testImplementation("io.kotest:kotest-property:5.9.1")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

allOpen {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
  annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

ktlint {
  version.set("1.6.0")
}

tasks.named("build") {
  dependsOn("ktlintFormat")
}
