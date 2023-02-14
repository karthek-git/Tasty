package com.karthek.android.s.tasty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.karthek.android.s.tasty.R
import com.karthek.android.s.tasty.net.Recipe
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecipeItemComponent(recipe: Recipe) {
	Column(modifier = Modifier.padding(4.dp)) {
		AsyncImage(
			model = ImageRequest.Builder(LocalContext.current)
				.data(recipe.thumbnailUrl)
				.build(),
			contentDescription = recipe.name,
			modifier = Modifier
				.size(160.dp)
				.padding(1.dp)
				.clip(RoundedCornerShape(10.dp))
				.background(MaterialTheme.colorScheme.onSurfaceVariant),
			contentScale = ContentScale.Crop
		)
		Text(
			text = recipe.name,
			maxLines = 2,
			overflow = TextOverflow.Ellipsis,
			modifier = Modifier.padding(top = 4.dp),
			style = MaterialTheme.typography.labelLarge
		)
		val date = remember {
			SimpleDateFormat("dd-MM-yyyy", Locale.US).format(recipe.createdAt)
		}
		Text(
			text = "${stringResource(R.string.created_at)}: $date",
			style = MaterialTheme.typography.labelSmall,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			fontWeight = FontWeight.Normal
		)
	}
}