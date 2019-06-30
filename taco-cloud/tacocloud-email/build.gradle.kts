buildscript {
    repositories {
        mavenCentral()
        maven(url = "http://repo.spring.io/plugins-release")
    }
    dependencies {
        classpath("io.spring.gradle:propdeps-plugin:0.0.10.RELEASE")
    }

}

plugins {
    kotlin("kapt")
    id("cn.bestwu.propdeps")
    id("cn.bestwu.propdeps-idea")
}

kapt {
    useBuildCache = true
    includeCompileClasspath = false
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.integration:spring-integration-mail")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
    optional("org.springframework.boot:spring-boot-configuration-processor") //for docs in yaml configs for properties
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}
