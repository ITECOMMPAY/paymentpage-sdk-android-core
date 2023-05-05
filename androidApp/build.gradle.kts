plugins {
    id("com.android.application")
    kotlin("android")
}

val projectId = "123"
val projectSecretKey = "321"
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
                "PROJECT_SECRET_KEY",
                "\"" + projectSecretKey + "\""
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
    implementation("com.ecommpay:msdk-core-android:0.9.1")

    implementation("androidx.startup:startup-runtime:1.1.1")

    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.activity:activity-ktx:1.5.1")
    implementation("androidx.core:core-ktx:1.8.0")

    implementation("com.google.android.gms:play-services-wallet:19.1.0")

    implementation("com.google.code.gson:gson:2.9.0")
}
