package ua.zloydi.gnews.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.zloydi.gnews.data.remote.RetrofitService

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
	@Provides
	fun provideRetrofit(): RetrofitService = Retrofit.Builder()
		.baseUrl(RetrofitService.LINK)
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(RetrofitService::class.java)
	
	
}