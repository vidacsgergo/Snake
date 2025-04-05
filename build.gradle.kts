plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    kotlin("jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application.mainClass = "com.snake.MainKt"
