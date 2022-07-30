plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdk = Versions.Android.compileSdk
    defaultConfig {
        applicationId = "com.bavian.simpleplayer"
        minSdk = Versions.Android.minSdk
        targetSdk = Versions.Android.targetSdk
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":player"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Kotlin.lang}")
    implementation("androidx.appcompat:appcompat:${Versions.AndroidX.appCompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintLayout}")
    implementation("androidx.legacy:legacy-support-v4:${Versions.AndroidX.legacySupportV4}")
    implementation("io.insert-koin:koin-android:${Versions.Kotlin.koin}")
//    implementation(project(mapOf("path" to ":player")))
    testImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.AndroidX.junit}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.AndroidX.espresso}")
}
