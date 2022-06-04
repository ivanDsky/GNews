package ua.zloydi.gnews.data.error

import okhttp3.ResponseBody

sealed class NetworkError(override val message: String, val code: Int) : Throwable() {
	object BadRequest : NetworkError(message = "Your request is not valid.", code = 400)
	object Unauthorized : NetworkError(message = "Your API key is incorrect.", code = 401)
	object Forbidden : NetworkError(
		message = "You have reached your daily request limit, the next reset is at 00:00 UTC.",
		code = 403
	)
	
	object TooManyRequests : NetworkError(
		message = "You have made more requests per second than you are allowed.", code = 429
	)
	
	object InternalServerError :
		NetworkError(message = "We had a problem with our server. Try again later.", code = 500)
	
	class Unknown(message: String, code: Int) : NetworkError(message, code)
	
	companion object {
		fun getErrorByCode(code: Int, error: ResponseBody? = null) = when (code) {
			BadRequest.code          -> BadRequest
			Unauthorized.code        -> Unauthorized
			Forbidden.code           -> Forbidden
			TooManyRequests.code     -> TooManyRequests
			InternalServerError.code -> InternalServerError
			else                     -> Unknown(error?.string() ?: "Unknown", code)
		}
	}
}

