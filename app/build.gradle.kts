plugins {
    androidApp()
    kotlinAndroid()
    kotlinKapt()
    dagger()
    androidGitVersion()
}

val appName = hashMapOf(
    "debug" to "iMin-app-debug.apk",
    "release" to "iMin-app-release.apk"
)

android {

    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion(Versions.buildToolsVersion)

    defaultConfig {

        applicationId = "com.kyawhut.imindemoapp"

        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)

        versionCode = 1//androidGitVersion.code()
        versionName = androidGitVersion.name()

        multiDexEnabled = true

        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {

        getByName("release") {
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    android.applicationVariants.all {
        val variant = this
        variant.outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val buildOutputPath = "../../release/${variant.versionName}/%s"
                output.outputFileName = String.format(
                    buildOutputPath,
                    appName[variant.buildType.name]
                )
            }
    }
}


dependencies {

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.testJunit)
    androidTestImplementation(Libs.espresso)

    implementation(Libs.kotlinLib)
    implementation(Libs.coreKtx)
    implementation(Libs.appCompact)
    implementation(Libs.material)
    implementation(Libs.constraintLayout)
    implementation(Libs.navigationFragmentKtx)
    implementation(Libs.navigationUI)
    implementation(Libs.vectorDrawable)

    // dependency injection
    implementation(Libs.hiltAndroid)
    kapt(Libs.hiltAndroidCompiler)

    // ViewModel and LiveData
    implementation(Libs.lifeCycleExt)
    implementation(Libs.fragmentKtx)

    //Timber(Logging)
    implementation(Libs.timber)

    //rx
    implementation(Libs.rxJava)
    implementation(Libs.rxAndroid)

    //iMin SDK
    implementation(fileTree("libs/imin/IminLibs1.0.15.jar"))
    implementation(fileTree("libs/imin/iminPrinterSDK.jar"))
}
