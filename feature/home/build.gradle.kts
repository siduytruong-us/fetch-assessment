plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)

	alias(libs.plugins.kotlin.kapt)
}

android {
	namespace = "com.duyts.android.home"
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
		compose = true
		buildConfig = true
	}
}

dependencies {
	implementation(libs.ui.tooling)
	implementation(libs.androidx.material3)

	testImplementation(libs.junit)
	testImplementation (libs.mockito.mockito.core)
	testImplementation (libs.mockito.kotlin)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)

	implementation(project(":core:common"))
	implementation(project(":core:design"))
	implementation(project(":core:data"))
	implementation(project(":core:test"))

	kapt(libs.hilt.android.compiler)
}