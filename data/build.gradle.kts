import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.lang)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "team.godsaeng.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        buildConfigField("String", "CLIENT_SECRET", gradleLocalProperties(rootDir).getProperty("client_secret"))
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
    buildFeatures {
        buildConfig = true
    }
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.bundles.common)
    implementation(libs.bundles.retrofit)

    // ksp
    ksp(libs.ksp.hilt)

    // datastore
    implementation(libs.bundles.datastore)

    // test
    testImplementation(libs.kotest)
    testImplementation(libs.mockK)
}