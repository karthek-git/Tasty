package com.karthek.android.s.tasty.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.karthek.android.s.tasty.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(value: String, onValueChange: (String) -> Unit) {
	val focusHandler = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current
	val focusCancel = {
		keyboardController?.hide()
		focusHandler.clearFocus()
	}
	TextField(
		value = value,
		onValueChange = onValueChange,
		singleLine = true,
		colors = TextFieldDefaults.textFieldColors(
			unfocusedIndicatorColor = Color.Transparent, focusedIndicatorColor = Color.Transparent
		),
		placeholder = { Text(text = stringResource(R.string.search)) },
		leadingIcon = {
			Icon(
				imageVector = Icons.Outlined.Search,
				contentDescription = stringResource(R.string.search),
				modifier = Modifier.padding(start = 16.dp),
			)
		},
		trailingIcon = {
			if (value.isNotEmpty()) {
				IconButton(onClick = {
					focusCancel()
					onValueChange("")
				}, modifier = Modifier.padding(end = 8.dp)) {
					Icon(
						imageVector = Icons.Outlined.Close,
						contentDescription = "",
					)
				}
			}
		},
		keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
		keyboardActions = KeyboardActions(onAny = { focusCancel() }),
		shape = CircleShape,
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp),
	)
}