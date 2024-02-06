package com.nila.blog.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.nila.blog.common.aop.ErrorCode
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
class GeneralResponse<T : Any> : Serializable {
    var rsCode: Int? = null
    var message: String? = null
    var isSuccess: Boolean? = null
    var resultData: T? = null

    companion object {
        fun <T : Any> successfulResponse(resultData: T?, errorCode: ErrorCode): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = errorCode.code
                this.message = errorCode.message
                this.isSuccess = true
                this.resultData = resultData
            }
        }

        fun <T : Any> successfulResponse(errorCode: ErrorCode): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = errorCode.code
                this.message = errorCode.message
                this.isSuccess = true
            }
        }

        fun <T : Any> successfulResponse(resultData: T?): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = ErrorCode.SUCCESSFUL.code
                this.message = ErrorCode.SUCCESSFUL.message
                this.isSuccess = true
                this.resultData = resultData
            }
        }

        fun <T : Any> successfulResponse(): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = ErrorCode.SUCCESSFUL.code
                this.message = ErrorCode.SUCCESSFUL.message
                this.isSuccess = true
            }
        }

        fun <T : Any> unsuccessfulResponse(errorCode: ErrorCode): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = errorCode.code
                this.message = errorCode.message
                this.isSuccess = false
            }
        }

        fun <T : Any> unsuccessfulResponse(errorCode: ErrorCode, message: String): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = errorCode.code
                this.message = message
                this.isSuccess = false
            }
        }

        fun <T : Any> unsuccessfulResponse(): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = ErrorCode.INTERNAL_SERVER_ERROR.code
                this.message = ErrorCode.INTERNAL_SERVER_ERROR.message
                this.isSuccess = false
            }
        }
    }
}
