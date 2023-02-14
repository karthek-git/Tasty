package com.karthek.android.s.tasty.di

import com.karthek.android.s.tasty.net.RecipeService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RecipeServiceModule {

	@Provides
	fun provideRecipeService(): RecipeService {
		val moshi = Moshi.Builder()
			.add(KotlinJsonAdapterFactory())
			.build()
		return Retrofit.Builder()
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.baseUrl("https://tasty.p.rapidapi.com/")
			.build()
			.create(RecipeService::class.java)
	}
}