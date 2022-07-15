plugins {
    id("com.android.application")
    kotlin("android")
}

val projectId = "PROJECT_ID"
val projectSalt = "PROJECT_SALT"
val gPayMerchantId = "BCR2DN6TZ75OBLTH"

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.paymentpage.msdk.core.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("debug") {
            buildConfigField(
                "String",
                "PROJECT_SALT",
                "\"" + projectSalt + "\""
            )

            buildConfigField(
                "int",
                "PROJECT_ID",
                projectId
            )

            buildConfigField(
                "String",
                "GPAY_MERCHANT_ID",
                "\"" + gPayMerchantId + "\""
            )
        }
    }
}

dependencies {
    implementation("com.ecommpay:msdk-core-android:0.2.0")

    implementation("androidx.startup:startup-runtime:1.1.1")

    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.activity:activity-ktx:1.5.0")
    implementation("androidx.core:core-ktx:1.8.0")

    implementation("com.google.android.gms:play-services-wallet:19.1.0")

    implementation("com.google.code.gson:gson:2.9.0")
}
