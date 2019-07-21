dependencies {
    implementation(project(":tacocloud-data"))
    implementation(project(":tacocloud-domain"))
    implementation(project(":tacocloud-messaging-jms"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("com.jayway.jsonpath:json-path")
}
