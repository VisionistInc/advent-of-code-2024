plugins {
	application
	id("com.gradleup.shadow") version "9.0.0-beta1"
	kotlin("jvm") version "2.0.20"
}

repositories {
	mavenCentral()
}

application {
	mainClass.set("SolutionKt")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
	kotlinDaemonJvmArgs = listOf("-Xmx8g", "-Xms4g")
}

dependencies {
	 implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

tasks {
	clean {
		file("${projectDir}/Solution.jar").delete()
	}

    shadowJar {
        archiveBaseName.set("Solution")
		archiveClassifier.set("")
		destinationDirectory.set(projectDir)
        mergeServiceFiles()
    }
}
