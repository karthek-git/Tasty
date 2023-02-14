package com.karthek.android.s.tasty.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.karthek.android.s.tasty.net.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val recipeRepo: RecipeRepo) : ViewModel() {
	var recipeFlow by mutableStateOf<Flow<PagingData<Recipe>>?>(null)
	private var searchJob: Job? = null

	fun search(q: String) {
		searchJob?.cancel()
		searchJob = viewModelScope.launch {
			delay(500)
			recipeFlow = recipeRepo.getRecipeResultStream(q.trim()).cachedIn(viewModelScope)
		}
	}
}