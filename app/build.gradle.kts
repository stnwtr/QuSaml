plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation(project(":api"))
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.3.6")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("com.github.javafaker:javafaker:1.0.2")

}

application.mainClass.set("at.stnwtr.qusaml.Launcher")
