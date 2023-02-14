package com.karthek.android.s.tasty.net

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecipeService {
	@Headers(
		"X-RapidAPI-Key: 0",
		"X-RapidAPI-Host: tasty.p.rapidapi.com"
	)
	@GET("recipes/list")
	suspend fun getRecipeList(
		@Query("from") from: Int,
		@Query("size") size: Int,
		@Query("q") q: String,
	): RecipeRequestResult
}

data class RecipeRequestResult(val count: Int, val results: List<Recipe>)
