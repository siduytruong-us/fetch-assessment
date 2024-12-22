package com.duyts.fetch.assessment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.duyts.fetch.home.HOME_ROUTE
import com.duyts.fetch.home.homeScreen


@Composable
fun AppNavigation() {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = HOME_ROUTE) {
		homeScreen()
	}
}