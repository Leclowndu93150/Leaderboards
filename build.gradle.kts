plugins {
    id("dev.prism")
}

group = "com.leclowndu93150.leaderboards"
version = "2.0.3"

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
    maven("architectury", "https://maven.architectury.dev/")
    maven("shedaniel", "https://maven.shedaniel.me/")
    maven("blamejared", "https://maven.blamejared.com/")
    maven("forge", "https://maven.minecraftforge.net/")

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
            publishingDependencies {
                requires("ftb-library-fabric")
                requires("baguettelib")
            }
        }
        neoforge {
            loaderVersion = "26.1.2.10-beta"
            loaderVersionRange = "[4,)"
            dependencies {
                implementation("com.leclowndu93150.baguettelib:baguettelib-26.1.2-neoforge:2.0.3")
                implementation("dev.ftb.mods:ftb-library-neoforge:26.1.2.1")
            }
            publishingDependencies {
                requires("ftb-library-forge")
                requires("baguettelib")
            }
        }
    }

    version("1.21.1") {
        common {
            compileOnly("dev.ftb.mods:ftb-library-neoforge:2101.1.31")
            compileOnly("dev.architectury:architectury-neoforge:13.0.6")
        }
        fabric {
            loaderVersion = "0.18.6"
            fabricApi("0.116.11+1.21.1")
            dependencies {
                modImplementation("dev.ftb.mods:ftb-library-fabric:2101.1.31")
            }
            publishingDependencies {
                requires("ftb-library-fabric")
                requires("fabric-api")
            }
        }
        neoforge {
            loaderVersion = "21.1.222"
            loaderVersionRange = "[4,)"
            dependencies {
                implementation("dev.ftb.mods:ftb-library-neoforge:2101.1.31")
            }
            publishingDependencies {
                requires("ftb-library-forge")
            }
        }
    }

    version("1.20.1") {
        version = "2.0.3"
        common {
            compileOnly("dev.ftb.mods:ftb-library-forge:2001.2.12")
            compileOnly("dev.architectury:architectury-forge:9.0.8")
        }
        fabric {
            loaderVersion = "0.18.6"
            fabricApi("0.92.8+1.20.1")
            dependencies {
                modImplementation("dev.ftb.mods:ftb-library-fabric:2001.2.12")
                compileOnly("net.minecraftforge:eventbus:6.0.5")
            }
            publishingDependencies {
                requires("ftb-library-fabric")
                requires("fabric-api")
            }
        }
        forge {
            loaderVersion = "47.4.18"
            loaderVersionRange = "[4,)"
            dependencies {
                modImplementation("dev.ftb.mods:ftb-library-forge:2001.2.12")
                modImplementation("curse.maven:architectury-api-419699:5137938")
                modRuntimeOnly("curse.maven:ftb-quests-forge-289412:7909594")
                modRuntimeOnly("curse.maven:ftb-teams-forge-404468:7499810")
            }
            publishingDependencies {
                requires("ftb-library-forge")
            }
        }
    }

    publishing {
        type = STABLE
        changelogFile = "CHANGELOG.md"

        curseforge {
            accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
            projectId = "1367888"
        }
    }
}
