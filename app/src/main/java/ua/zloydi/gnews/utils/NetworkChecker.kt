package ua.zloydi.gnews.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkChecker @Inject constructor(@ApplicationContext context: Context) {
	private val service =
		context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	
	fun checkConnection(): Boolean {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val capabilities: NetworkCapabilities =
				service.getNetworkCapabilities(service.activeNetwork) ?: return false
			when {
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)     -> return true
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
				capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)      -> return true
			}
		} else {
			val activeNetwork: NetworkInfo = service.activeNetworkInfo ?: return false
			when (activeNetwork.type) {
				ConnectivityManager.TYPE_WIFI   -> return true
				ConnectivityManager.TYPE_MOBILE -> return true
				ConnectivityManager.TYPE_VPN    -> return true
			}
		}
		return false
	}
}