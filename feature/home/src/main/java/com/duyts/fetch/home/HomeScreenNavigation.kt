package com.duyts.fetch.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val HOME_ROUTE = "/home"

fun NavGraphBuilder.homeScreen() {
	composable(HOME_ROUTE) {
		HomeScreen()
	}
}

fun NavController.navigateToHomeScreen() {
	navigate(HOME_ROUTE)
}