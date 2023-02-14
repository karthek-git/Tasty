package com.karthek.android.s.tasty.net

import com.squareup.moshi.Json

data class Recipe(
	val name: String,
	@Json(name = "thumbnail_url") val thumbnailUrl: String,
	@Json(name = "created_at") val createdAt: Long,
)
