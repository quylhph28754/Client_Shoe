plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.fpoly.shoes_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fpoly.shoes_app"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding.enable = true
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.maps:google-maps-services:0.15.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation ("com.google.maps:google-maps-services:0.18.0")

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    // Hilt
    val hiltVersion = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Dots Indicator
    implementation("com.tbuonomo:dotsindicator:5.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Coil
    implementation("io.coil-kt:coil:2.4.0")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}