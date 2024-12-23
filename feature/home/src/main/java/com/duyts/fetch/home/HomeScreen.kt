package com.duyts.fetch.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duyts.fetch.core.data.model.HiringItem

@Composable
fun HomeScreen(
	viewModel: HomeScreenViewModel = hiltViewModel(),
) {
	when (val state = viewModel.state.collectAsStateWithLifecycle().value) {
		is HomeScreenState.Loading -> LoadingContent()
		is HomeScreenState.Error -> ErrorContent(state)
		is HomeScreenState.Success -> HomeContent(state, onRefresh = {})
	}
}

@Composable
private fun LoadingContent() {
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		CircularProgressIndicator()
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
	state: HomeScreenState.Success,
	isRefreshing: Boolean = false,
	onRefresh: (() -> Unit)? = null,
) {
	val items = state.hiringItems
//	PullToRefreshBox(
//		isRefreshing = isRefreshing,
//		onRefresh = { onRefresh?.invoke() },
//	) {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	) {
		items.forEach { (listId, items) ->
			item {
				ListHeader("List ID: $listId")
			}
			items(items) { hiringItem ->
				HiringItemRow(hiringItem)
			}
		}
//		}
	}


}

@Composable
private fun ErrorContent(state: HomeScreenState.Error) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
		contentAlignment = Alignment.Center
	) {
		Text(state.error)
	}
}

@Preview
@Composable
fun HomeContentPreview() {
	HomeContent(HomeScreenState.Success(emptyMap()))
}

@Preview
@Composable
fun ErrorContentPreview() {
	ErrorContent(HomeScreenState.Error("Error message!"))
}


@Composable
fun HiringItemRow(hiringItem: HiringItem) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
			.background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
			.padding(16.dp), verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = hiringItem.name ?: "No Name",
			style = MaterialTheme.typography.bodyMedium,
			modifier = Modifier.weight(1f)
		)
		Text(
			text = "ID: ${hiringItem.id}", style = MaterialTheme.typography.bodyMedium
		)
	}
}

@Composable
fun ListHeader(title: String) {
	Row(
		modifier = Modifier
			.padding(vertical = 8.dp, horizontal = 16.dp)
			.height(16.dp)
	) {
		ListHeaderDivider()
		Text(
			text = title,
			modifier = Modifier.padding(horizontal = 16.dp),
			style = MaterialTheme.typography.labelLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		ListHeaderDivider()
	}
}


@Composable
private fun RowScope.ListHeaderDivider() {
	HorizontalDivider(
		modifier = Modifier
			.weight(1f)
			.align(Alignment.CenterVertically),
		color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
	)
}
