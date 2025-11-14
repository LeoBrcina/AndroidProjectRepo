plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "hr.algebra.formula1data"
    compileSdk = 35

    defaultConfig {
        applicationId = "hr.algebra.formula1data"
        minSdk = 29
        targetSdk = 35
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
}

dependencies {

// Retrofit (API calls)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

// Gson (used for JSON parsing)
    implementation(libs.gson)

// Kotlin Coroutines (for background API calls)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

// AndroidX Lifecycle (ViewModel + LiveData)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

// Material Design Components (for bottom nav, etc.)
    implementation(libs.material)

// RecyclerView (for lists of standings, races, etc.)
    implementation(libs.androidx.recyclerview)

// Navigation Component (for handling fragments and nav graph)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

// Room core
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

// Kotlin extensions and coroutines support
    implementation(libs.room.ktx)

    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}