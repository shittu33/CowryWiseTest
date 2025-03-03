package com.example.cowrywisetest.utils
sealed class NetworkResult<out R> {

    data class Success<out T>(val data: T) : NetworkResult<T>()
    data object Loading: NetworkResult<Nothing>()
    sealed class Error : NetworkResult<Nothing>() {
        data class NetworkError(val code: Int? = null,val message: String="please check your internet connection") : Error(){
            override fun toString(): String {
                return message
            }
        }

        data class TimeoutError(val code: Int? = null,val message: String="Network timeout!") : Error(){
            override fun toString(): String {
                return message
            }
        }

        data class NoDataError(val code: Int? = null,val message: String="something went wrong") : Error(){
            override fun toString(): String {
                return message
            }
        }

        /**
         * Error gotten from the response body
         * It sends the entire result unlike other errors so the receiver will know exactly
         * how to handle it
         * It is very similar to Success, but the fact that it is an error completely changes its usage
         * */
        data class ApiError(val message: String?,val error: Map<Any, Any>?=null, val code: Int? = null) : Error(){
            override fun toString(): String {
                return message ?: "An error occurred"
            }
        }
    }


}