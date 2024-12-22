plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.kapt)
	alias(libs.plugins.hilt.android)
}

android {
	namespace = "com.duyts.android.common"
	compileSdk = 34
	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {
	implementation(libs.retrofit.core)
//
	api(libs.androidx.lifecycle.runtime.compose)

	//Navigation
	api (libs.androidx.navigation.compose)

	//Hilt
//	api (libs.javax.inject)
	api(libs.hilt.android)
	api(libs.androidx.navigation.compose)
	api (libs.androidx.hilt.navigation.compose)
	kapt(libs.hilt.android.compiler)
}


kapt {
	correctErrorTypes = true
}
