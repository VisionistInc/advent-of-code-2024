plugins {
	kotlin("jvm") version "2.0.20"
    id("maven-publish")
}

group = "aoc"
version = "0.2.0"

repositories {
	mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
