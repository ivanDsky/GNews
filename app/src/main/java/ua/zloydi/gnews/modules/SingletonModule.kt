package ua.zloydi.gnews.modules

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.zloydi.gnews.data.remote.RetrofitService

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
	@Provides
	fun provideRetrofit(): RetrofitService = Retrofit.Builder().baseUrl(RetrofitService.LINK)
		.addConverterFactory(GsonConverterFactory.create()).build()
		.create(RetrofitService::class.java)
	
	@Provides
	fun provideResources(@ApplicationContext context: Context): Resources = context.resources
}