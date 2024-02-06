package com.nila.blog.common.aop.exeptions

import com.nila.blog.common.aop.ErrorCode

class BlogException : RuntimeException {

    var errorCode: ErrorCode? = null

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode, cause: Throwable?) : super(errorCode.message, cause) {
        this.errorCode = errorCode
    }


}