plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

group = "io.github.liconrepo"
version = "0.1.0"

android {
    namespace = "io.github.liconrepo.mds"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    // A design system is public API first: every declaration must state its visibility.
    explicitApi()
    compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) }
}

dependencies {
    api(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = "io.github.liconrepo"
                artifactId = "mds"
                version = project.version.toString()
                pom {
                    name.set("Mobile Design System")
                    description.set("A View-based, token-driven design system for Android. No Compose dependency.")
                    url.set("https://github.com/liconrepo/MobileDesignSystem")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    scm { url.set("https://github.com/liconrepo/MobileDesignSystem") }
                }
            }
        }
    }
}
