plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "at.toastiii.craftflowers"
version = "1.2.4"

repositories {
    maven(url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/"))
    maven(url = uri("https://maven.enginehub.org/repo/"))
    maven(url = uri("https://libraries.minecraft.net"))

}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-experimental-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.6.25")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:2.7.0")
    implementation("fr.minuskube.inv:smart-invs:1.2.7") {
        isTransitive = false
    }
}

tasks.withType<ProcessResources> {
    filesMatching("plugin.yml") {
        expand(mapOf("version" to project.version))
    }
}

tasks {
    shadowJar {
        relocate("fr.minuskube.inv", "cm.ptks.craftflowers.smartinvs")
    }
    build {
        dependsOn(shadowJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

tasks {
    runServer {
        minecraftVersion("1.20.1")
    }
}
