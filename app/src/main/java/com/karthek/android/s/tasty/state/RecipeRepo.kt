package com.karthek.android.s.tasty.state

import androidx.paging.*
import com.karthek.android.s.tasty.net.Recipe
import com.karthek.android.s.tasty.net.RecipeService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepo @Inject constructor(private val recipeService: RecipeService) {
	fun getRecipeResultStream(q: String): Flow<PagingData<Recipe>> {
		return Pager(
			config = PagingConfig(pageSize = 20),
			pagingSourceFactory = { RecipePagingSource(recipeService, q) }
		).flow
	}
}

class RecipePagingSource(
	private val recipeService: RecipeService,
	private val q: String,
) : PagingSource<Int, Recipe>() {
	override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			val anchorPage = state.closestPageToPosition(anchorPosition)
			anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
		return try {
			val nextPageNumber = params.key ?: 0
			val loadSize = params.loadSize
			val response = recipeService.getRecipeList(nextPageNumber, loadSize, q)
			val nextKey = nextPageNumber + loadSize
			LoadResult.Page(
				data = response.results,
				prevKey = null,
				nextKey = if (nextKey < response.count) nextKey else null
			)
		} catch (e: Exception) {
			e.printStackTrace()
			LoadResult.Error(e)
		}
	}
}