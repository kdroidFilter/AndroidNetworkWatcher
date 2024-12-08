import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.vannitktech.maven.publish)
}

group = "com.kdroid.netwatcher"
version = "0.1.0"

kotlin {
    jvmToolchain(11)
    androidTarget {
        publishLibraryVariants("release")
    }

    sourceSets {
        commonMain.dependencies {
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

    }

}

android {
    namespace = "com.kdroid.netwatcher"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

}

mavenPublishing {
    coordinates(
        groupId = "io.github.kdroidfilter",
        artifactId = "netwatcher",
        version = version.toString()
    )

    pom {
        name.set("NetWatcher")
        description.set("ConnectivityMonitor is a lightweight Android library that monitors real-time network connectivity changes. It detects whether the network is available and identifies its type (Wi-Fi, cellular, etc.), with support for older Android versions. Easy to integrate, it provides callbacks to quickly respond to changes in network status.")
        inceptionYear.set("2024")
        url.set("https://github.com/kdroidFilter/NetWatcher")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("kdroidfilter")
                name.set("Elyahou Hadass")
                email.set("elyahou.hadass@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/kdroidFilter/NetWatcher")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}

