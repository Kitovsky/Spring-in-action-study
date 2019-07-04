import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val springBootVersion by extra("2.1.6.RELEASE")
    val kotlinVersion by extra("1.3.41")
    repositories {
        mavenCentral()
        maven(url = "http://repo.spring.io/plugins-release")
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
        classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
        classpath("io.spring.gradle:propdeps-plugin:0.0.10.RELEASE")
    }
}

plugins {
    kotlin("jvm") version "1.3.41"//fixme
    id("org.springframework.boot") version "2.1.6.RELEASE"
    kotlin("kapt") version "1.3.41"
    id("cn.bestwu.propdeps") version "0.0.10"
    id("cn.bestwu.propdeps-idea") version "0.0.10"
}

kapt {
    useBuildCache = true
    includeCompileClasspath = false
}

allprojects {

    group = "kit"
    version = "0.0.1-SNAPSHOT"

//    plugins {
////        kotlin("jvm")
//        idea
////        id("org.springframework.boot") version "2.1.6.RELEASE"
////        kotlin("spring")
////        id("io.spring.dependency-management") version "1.0.8.RELEASE"
//    }
    apply(plugin = "idea")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management") // mb disable?

    idea {
        module {
            inheritOutputDirs = false
            outputDir = file("$buildDir/classes/kotlin/main")
        }
    }

    repositories {
        mavenCentral()
    }

    tasks.test {
        useJUnitPlatform()
    }

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:2.1.6.RELEASE"))
        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
}

subprojects {
    if (name != "tacocloud-kitchen"
            || name != "dataflow"
            || name != "tacocloud-email") {
        tasks.bootJar {
            enabled = false
        }

        tasks.jar {
            enabled = true
        }
    }
}

dependencies {
    implementation(project(":tacocloud-domain"))
    implementation(project(":tacocloud-api"))
    implementation(project(":tacocloud-data"))
    implementation(project(":tacocloud-security"))
    implementation(project(":tacocloud-messaging-jms"))
//    implementation(project(":tacocloud-messaging-rabbitmq")) // do not use
//    implementation(project(":tacocloud-messaging-kafka")) // do not use
    implementation("joda-time:joda-time:2.3")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    runtime("com.h2database:h2")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    optional("org.springframework.boot:spring-boot-configuration-processor") //for docs in yaml configs for properties
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.seleniumhq.selenium:htmlunit-driver")
    testImplementation("org.seleniumhq.selenium:selenium-java")
}