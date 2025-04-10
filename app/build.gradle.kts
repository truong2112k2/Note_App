plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.dagger.hilt)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.1.0" // hoặc version Kotlin của bạn
}

android {
    namespace = "com.example.noteapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.noteapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
  //  implementation (libs.androidx.work.runtime.ktx)



    implementation(libs.ui)
    //noinspection UseTomlInstead
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.foundation)
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    //noinspection UseTomlInstead
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation(libs.coil.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.androidx.hilt.android)
    ksp(libs.androidx.hilt.compiler)

    // Hilt Navigation Compose
    implementation(libs.androidx.hilt.navigation.compose)

    // navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")

    implementation("androidx.paging:paging-runtime:3.2.1") // Paging cho Room
    implementation("androidx.paging:paging-compose:3.2.1")
    implementation("androidx.room:room-paging:2.6.1")
    //Data store preferences
    implementation("androidx.datastore:datastore-preferences:1.1.3")

    //Animation
    implementation("androidx.compose.animation:animation-core:1.5.4") // Cập nhật version phù hợp


    implementation("androidx.compose.material3:material3:1.3.1")
    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

}