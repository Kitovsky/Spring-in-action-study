dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-artemis")
    implementation ("org.springframework.boot:spring-boot-starter-amqp")
    implementation ("org.springframework.kafka:spring-kafka")
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly ("org.springframework.boot:spring-boot-devtools")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
}