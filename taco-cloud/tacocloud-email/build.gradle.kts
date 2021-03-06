buildscript {
    repositories {
        mavenCentral()
        jcenter()
//        maven(url = "http://repo.spring.io/plugins-release")
    }
    dependencies {
//        classpath("io.spring.gradle:propdeps-plugin:0.0.10.RELEASE")
    }

}

plugins {
//    kotlin("kapt")
//    id("cn.bestwu.propdeps")
//    id("cn.bestwu.propdeps-idea")
}

//kapt {
//    useBuildCache = true
//    includeCompileClasspath = false
//}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.integration:spring-integration-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("javax.mail:mail:1.4")
    implementation("org.apache.commons:commons-text:1.2")
//    kapt("org.springframework.boot:spring-boot-configuration-processor")
//    optional("org.springframework.boot:spring-boot-configuration-processor") //for docs in yaml configs for properties
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor") //for docs in yaml configs for properties
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
