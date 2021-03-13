plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.unbroken-dome.test-sets")
    id("maven-publish")
}

val springCloudStarterParentVersion: String by project
val kotlinVersion: String by project
val testContainersBomVersion: String by project
val springDataNeo4jVersion: String by project

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(platform("org.springframework.cloud:spring-cloud-starter-parent:${springCloudStarterParentVersion}"))

    kapt(platform("org.springframework.cloud:spring-cloud-starter-parent:${springCloudStarterParentVersion}"))

    testImplementation(platform("org.testcontainers:testcontainers-bom:${testContainersBomVersion}"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.springframework.data:spring-data-neo4j:${springDataNeo4jVersion}")

    testImplementation("org.jetbrains.kotlin:kotlin-test:${kotlinVersion}")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:neo4j")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

testSets {
    create("repositoryTest") {
        createArtifact = true
    }
}