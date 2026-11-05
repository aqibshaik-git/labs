plugins { java }

java { toolchain { languageVersion = JavaLanguageVersion.of(25) } }

repositories { mavenCentral() }

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test { useJUnitPlatform() }

// Thu 12 Nov: ./gradlew lostUpdate   (runs the broken UnsafeAccounts and prints the damage)
tasks.register<JavaExec>("lostUpdate") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("dev.aqib.labs.concurrency.UnsafeAccounts")
}
