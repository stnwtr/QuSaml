plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation(project(":api"))
}

application.mainClass.set("at.stnwtr.qusaml.QuSaml")
