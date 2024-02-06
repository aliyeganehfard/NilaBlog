package com.nila.blog.common.dto

import com.nila.blog.common.aop.ErrorCode
import java.io.Serializable

class GeneralResponse<T> : Serializable {
    private var rsCode: Int? = null
    private var message: String? = null
    private var isSuccess: Boolean? = null
    private var resultData: T? = null

    companion object {

        fun <T> successfulResponse(resultData: T?, errorCode: ErrorCode): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = errorCode.code
                this.message = errorCode.message
                this.isSuccess = true
                this.resultData = resultData
            }
        }

        fun <T> successfulResponse(errorCode: ErrorCode): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = errorCode.code
                this.message = errorCode.message
                this.isSuccess = true
            }
        }

        fun <T> successfulResponse(resultData: T?): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = ErrorCode.SUCCESSFUL.code
                this.message = ErrorCode.SUCCESSFUL.message
                this.isSuccess = true
                this.resultData = resultData
            }
        }

        fun <T> successfulResponse(): GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = ErrorCode.SUCCESSFUL.code
                this.message = ErrorCode.SUCCESSFUL.message
                this.isSuccess = true
            }
        }

        fun <T>  unsuccessfulResponse(errorCode: ErrorCode): GeneralResponse<T>  {
            return GeneralResponse<T>().apply {
                this.rsCode = errorCode.code
                this.message = errorCode.message
                this.isSuccess = false
            }
        }

        fun <T>  unsuccessfulResponse():  GeneralResponse<T> {
            return GeneralResponse<T>().apply {
                this.rsCode = ErrorCode.INTERNAL_SERVER_ERROR.code
                this.message = ErrorCode.INTERNAL_SERVER_ERROR.message
                this.isSuccess = false
            }
        }
    }
}
