package ua.zloydi.gnews.data.error

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ua.zloydi.gnews.utils.NetworkChecker
import java.io.IOException

class NetworkInterceptor(private val networkChecker: NetworkChecker) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request: Request = chain.request()
		if (networkChecker.checkConnection()) {
			return chain.proceed(request)
		} else {
			throw IOException("No internet connection")
		}
	}
}
