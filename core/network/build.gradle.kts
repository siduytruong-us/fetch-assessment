plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.secrets)
	alias(libs.plugins.kotlin.kapt)
}

android {
	namespace = "com.duyts.android.network"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
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

	buildFeatures {
		buildConfig = true
	}
}

secrets {
	defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
	api(libs.retrofit.core)
	implementation(libs.converter.gson)
	implementation(libs.okhttp.logging)
	testImplementation(libs.junit)

	implementation(project(":core:common"))

	kapt(libs.hilt.android.compiler)
}