plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.bavian.simpleplayer"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation("androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintLayout}")
    implementation("androidx.legacy:legacy-support-v4:${Versions.AndroidX.legacySupportV4}")
    testImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.AndroidX.junit}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.AndroidX.espresso}")

    // Jetpack Compose
    // Integration with activities
    implementation("androidx.activity:activity-compose:${Versions.JetpackCompose.activity}")
    // Compose Material Design
    implementation("androidx.compose.material:material:${Versions.JetpackCompose.extra}")
    // Animations
    implementation("androidx.compose.animation:animation:${Versions.JetpackCompose.extra}")
    // Tooling support (Previews, etc.)
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.JetpackCompose.extra}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.JetpackCompose.extra}")
    implementation("androidx.compose.ui:ui:${Versions.JetpackCompose.extra}")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:${Versions.JetpackCompose.extra}")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:${Versions.JetpackCompose.extra}")
    // Material Design
    implementation("androidx.compose.material:material:${Versions.JetpackCompose.extra}")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:${Versions.JetpackCompose.extra}")
    implementation("androidx.compose.material:material-icons-extended:${Versions.JetpackCompose.extra}")

}
