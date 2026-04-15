plugins {
    id("dev.prism")
}

group = "com.leclowndu93150.leaderboards"
version = "26.1.2-1.0.0"

prism {
    metadata {
        modId = "leaderboards"
        name = "Leaderboards"
        description = "Adds leaderboards for vanilla statistics to Minecraft."
        license = "MIT"
        author("Leclowndu93150")
    }

    curseMaven()
    maven("leclown", "https://maven.leclowndu93150.dev/releases")
    maven("ftb", "https://maven.ftb.dev/releases")

    version("26.1.2") {
        common {
            implementation("com.leclowndu93150.baguettelib:baguettelib-26.1.2-common:2.0.3")
            compileOnly("dev.ftb.mods:ftb-library:26.1.2.1")
        }
        fabric {
            loaderVersion = "0.18.6"
            fabricApi("0.145.4+26.1.2")
            dependencies {
                modImplementation("com.leclowndu93150.baguettelib:baguettelib-26.1.2-fabric:2.0.3")
                modImplementation("dev.ftb.mods:ftb-library-fabric:26.1.2.1")
            }
        }
        neoforge {
            loaderVersion = "26.1.2.10-beta"
            loaderVersionRange = "[4,)"
            dependencies {
                implementation("com.leclowndu93150.baguettelib:baguettelib-26.1.2-neoforge:2.0.3")
                implementation("dev.ftb.mods:ftb-library-neoforge:26.1.2.1")
            }
        }
    }
}
