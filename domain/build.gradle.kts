plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "team.godsaeng.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(libs.bundles.common)

    // ksp
    ksp(libs.ksp.hilt)

    // test
    testImplementation(libs.kotest)
    testImplementation(libs.mockK)
}