plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.watch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.watch"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    buildFeatures {
        viewBinding = true;
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation("androidx.wear:wear:1.3.0")

    // Add support for wearable specific inputs
    implementation("androidx.wear:wear-input:1.1.0")
    implementation("androidx.wear:wear-input-testing:1.1.0")

    // Use to implement wear ongoing activities
    implementation("androidx.wear:wear-ongoing:1.0.0")

    // Use to implement support for interactions from the Wearables to Phones
    implementation("androidx.wear:wear-phone-interactions:1.0.1")
    implementation(libs.constraintlayout)
}