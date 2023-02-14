package com.karthek.android.s.tasty.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.karthek.android.s.tasty.R

@Composable
fun ContentLoading(size: Dp = 64.dp) {
	CircularProgressIndicator(modifier = Modifier
		.size(size)
		.wrapContentSize(Alignment.Center),
		strokeWidth = 4.dp)
}

@Composable
fun ContentInfo(text: String, modifier: Modifier = Modifier) {
	Text(text = text, modifier = modifier
		.fillMaxSize()
		.wrapContentSize(Alignment.Center))
}

@Composable
fun ContentError(text: String, onClickRetry: () -> Unit) {
	Row(
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.wrapContentSize(Alignment.Center)
	) {
		Icon(imageVector = Icons.Outlined.Warning, contentDescription = text)
		Text(text = text,
			color = MaterialTheme.colorScheme.error,
			modifier = Modifier.padding(start = 8.dp))
		TextButton(onClick = onClickRetry) {
			Text(text = stringResource(R.string.retry))
		}
	}
}