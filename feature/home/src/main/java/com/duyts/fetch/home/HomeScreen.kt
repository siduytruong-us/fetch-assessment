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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.duyts.android.domain.DisplayHiringItem
import com.duyts.android.home.R
import com.duyts.fetch.core.data.model.HiringItem

@Composable
fun HomeScreen(
	viewModel: HomeScreenViewModel = hiltViewModel(),
) {
	val state by viewModel.state.collectAsStateWithLifecycle()

	LaunchedEffect(state) {
		(state as? HomeScreenState.Success)?.let { result ->
			if (result.hiringItems.isEmpty()) {
				viewModel.fetchNewHiringItems()
			}
		}
	}
	/*Other Contents*/
	HomeContent(state)
	/*Other Contents*/
}

@Composable
fun HomeContent(state: HomeScreenState) {
	when (state) {
		is HomeScreenState.Loading -> LoadingContent()
		is HomeScreenState.Error -> ErrorContent(state)
		is HomeScreenState.Success -> ListContent(state)
	}
}

@Composable
private fun LoadingContent() {
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		CircularProgressIndicator()
	}
}

@Composable
private fun ListContent(
	state: HomeScreenState.Success,
) {
	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	) {
		items(state.hiringItems, key = (DisplayHiringItem::toKey)) { displayItem ->
			when (displayItem) {
				is DisplayHiringItem.Header -> {
					ListHeader(stringResource(R.string.list_id, displayItem.listID))
				}

				is DisplayHiringItem.Item -> {
					HiringItemRow(displayItem.item)
				}
			}
		}
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
	ListContent(HomeScreenState.Success(emptyList()))
}

@Preview
@Composable
fun ErrorContentPreview() {
	ErrorContent(HomeScreenState.Error(stringResource(R.string.error_message)))
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
			text = hiringItem.name ?: stringResource(R.string.no_name),
			style = MaterialTheme.typography.bodyMedium,
			modifier = Modifier.weight(1f)
		)
		Text(
			text = stringResource(R.string.id, hiringItem.id), style = MaterialTheme.typography.bodyMedium
		)
	}
}

@Composable
fun ListHeader(title: String) {
	Row(
		modifier = Modifier
			.padding(vertical = 8.dp, horizontal = 16.dp)
			.height(16.dp),
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


private fun DisplayHiringItem.toKey() = when (this) {
	is DisplayHiringItem.Header -> listID
	is DisplayHiringItem.Item -> item.id
}