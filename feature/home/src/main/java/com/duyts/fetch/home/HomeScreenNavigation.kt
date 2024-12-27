package com.duyts.fetch.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


const val HOME_ROUTE = "/home"

@Serializable
object HomeRoute

fun NavGraphBuilder.homeScreen() {
	composable<HomeRoute> {
		HomeScreen()
	}
}

fun NavController.navigateToHomeScreen() {
	navigate(HomeRoute)
}