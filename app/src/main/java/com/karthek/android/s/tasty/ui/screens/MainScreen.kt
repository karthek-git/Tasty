package com.karthek.android.s.tasty.ui.screens

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.karthek.android.s.tasty.R
import com.karthek.android.s.tasty.net.Recipe
import com.karthek.android.s.tasty.state.MainViewModel
import com.karthek.android.s.tasty.ui.components.*
import kotlinx.coroutines.flow.Flow

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
	Surface(modifier = Modifier.fillMaxSize()) {
		MainScreenContent(viewModel.recipeFlow, viewModel::search)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
	recipeFlow: Flow<PagingData<Recipe>>?,
	onSearch: (String) -> Unit,
) {
	Scaffold(topBar = {
		TopAppBar(title = {
			Text(text = stringResource(id = R.string.app_name))
		})
	}) { paddingValues ->
		NetworkAwareComponent(paddingValues = paddingValues) { innerPaddingValues ->
			ExploreContent(paddingValues = innerPaddingValues,
				recipeFlow = recipeFlow,
				onSearch = onSearch)
		}
	}
}

@Composable
fun NetworkAwareComponent(
	paddingValues: PaddingValues,
	content: @Composable (PaddingValues) -> Unit,
) {
	var networkAvailable by rememberSaveable { mutableStateOf(false) }
	val context = LocalContext.current
	val connectivityManager =
		remember { getSystemService(context, ConnectivityManager::class.java) }
	DisposableEffect(LocalLifecycleOwner.current) {
		val networkCallback = object : ConnectivityManager.NetworkCallback() {
			override fun onLost(network: Network) {
				networkAvailable = false
			}

			override fun onCapabilitiesChanged(
				network: Network,
				networkCapabilities: NetworkCapabilities,
			) {
				val capability = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					NetworkCapabilities.NET_CAPABILITY_VALIDATED
				} else {
					NetworkCapabilities.NET_CAPABILITY_INTERNET
				}
				if (networkCapabilities.hasCapability(capability)) {
					networkAvailable = true
				}
			}
		}
		val networkRequest = NetworkRequest.Builder().build()
		connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
		onDispose {
			connectivityManager?.unregisterNetworkCallback(networkCallback)
		}
	}
	if (networkAvailable) {
		content(paddingValues)
	} else {
		NetworkNotAvailableContent(paddingValues)
	}
}

@Composable
fun NetworkNotAvailableContent(paddingValues: PaddingValues) {
	Row(
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.padding(paddingValues)
			.fillMaxSize(),
	) {
		val text = stringResource(R.string.no_net_connection)
		Icon(imageVector = Icons.Outlined.Warning, contentDescription = text)
		Text(text = text, modifier = Modifier.padding(start = 8.dp))
	}
}

@Composable
fun ExploreContent(
	paddingValues: PaddingValues,
	recipeFlow: Flow<PagingData<Recipe>>?,
	onSearch: (String) -> Unit,
) {
	Column(modifier = Modifier
		.padding(paddingValues)
		.fillMaxSize()) {
		var textFieldValue by rememberSaveable { mutableStateOf("") }
		SearchTextField(value = textFieldValue) { q ->
			textFieldValue = q
			onSearch(q)
		}
		if (textFieldValue.isEmpty()) {
			ContentInfo(text = stringResource(R.string.content_info),
				modifier = Modifier.weight(1f))
		} else {
			if (recipeFlow != null) {
				RecipeGrid(recipeFlow = recipeFlow)
			}
		}
	}
}

@Composable
fun RecipeGrid(recipeFlow: Flow<PagingData<Recipe>>) {
	val lazyPagingItems = recipeFlow.collectAsLazyPagingItems()
	LazyVerticalGrid(columns = GridCells.Adaptive(160.dp),
		contentPadding = PaddingValues(horizontal = 4.dp),
		modifier = Modifier
			.fillMaxSize()
			.wrapContentSize(Alignment.Center)) {
		lazyPagingItems.apply {
			val itemSpan = { s: LazyGridItemSpanScope ->
				GridItemSpan(s.maxCurrentLineSpan)
			}
			if (loadState.refresh is LoadState.NotLoading) {
				if (itemCount == 0) {
					item(span = itemSpan) {
						ContentInfo(text = stringResource(R.string.content_empty))
					}
				} else {
					items(itemCount) { index ->
						get(index)?.let { RecipeItemComponent(it) }
					}
				}
			}
			when {
				loadState.refresh is LoadState.Loading -> {
					item(span = itemSpan) { ContentLoading() }
				}
				loadState.append is LoadState.Loading -> {
					item(span = itemSpan) { ContentLoading(size = 48.dp) }
				}
				loadState.refresh is LoadState.Error -> {
					val e = lazyPagingItems.loadState.refresh as LoadState.Error
					item(span = itemSpan) {
						ContentError(
							text = e.error.localizedMessage!!,
							onClickRetry = { retry() },
						)
					}
				}
				loadState.append is LoadState.Error -> {
					val e = lazyPagingItems.loadState.append as LoadState.Error
					item(span = itemSpan) {
						ContentError(
							text = e.error.localizedMessage!!,
							onClickRetry = { retry() },
						)
					}
				}
			}
		}
	}
}