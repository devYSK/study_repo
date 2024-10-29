dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
//	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security
	implementation("org.springframework.boot:spring-boot-starter-security:3.3.4")

	implementation("org.springframework.boot:spring-boot-starter-graphql")
	testImplementation("org.springframework.graphql:spring-graphql-test")


	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.4")

}
