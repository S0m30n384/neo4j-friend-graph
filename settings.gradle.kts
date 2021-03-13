rootProject.name = "neo4j-friend-graph"

pluginManagement {

    val kotlinVersion: String by settings
    val unbrokenDomeTestSetsPluginVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.kapt") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
        id("org.unbroken-dome.test-sets") version unbrokenDomeTestSetsPluginVersion
    }

}