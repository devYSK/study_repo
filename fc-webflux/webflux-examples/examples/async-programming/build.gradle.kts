repositories {
	mavenCentral()
}

dependencies {
	// reactor
	implementation("io.projectreactor:reactor-core:3.6.6")

	// rxjava
	implementation("io.reactivex.rxjava3:rxjava")

	// mutiny
	implementation("io.smallrye.reactive:mutiny:2.1.0")
}
