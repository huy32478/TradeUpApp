@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()                 // ✅ KHÔNG giới hạn content
        gradlePluginPortal()     // ✅ bắt buộc để load plugins như google-services
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("myLibs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "TradeUpAppMoi"
include(":app")

