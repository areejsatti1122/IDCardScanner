plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.idcardscanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.idcardscanner"
        minSdk = 24
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("com.itextpdf:itextg:5.5.10")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Image Picker
    implementation("com.github.dhaval2404:imagepicker:2.1")

    // MLKit Text Recognition
    implementation("com.google.mlkit:text-recognition:16.0.0")
}