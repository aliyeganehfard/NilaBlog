package com.nila.blog.common.aop

import org.springframework.http.HttpStatus

enum class ErrorCode(var code: Int?, var message: String?, var httpStatus: HttpStatus?) {
    SUCCESSFUL(1000, "operation was successful", HttpStatus.OK),
    INTERNAL_SERVER_ERROR(10001, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_DENIED(1002, "access denied", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(1003, "token has expired earlier", HttpStatus.FORBIDDEN),
    TOKEN_INVALID(1004, "token is not valid", HttpStatus.FORBIDDEN),
    RSA_TROUBLE_READ_PUBLIC_KEY(1005, "trouble in read public key file", HttpStatus.INTERNAL_SERVER_ERROR),
    RSA_TROUBLE_READ_PRIVATE_KEY(1005, "trouble in read private key file", HttpStatus.INTERNAL_SERVER_ERROR),


    USER_NOT_FOUND(1050, "user not found", HttpStatus.BAD_REQUEST),
    PASSWORD_CONFIRMATION_MISMATCH(1051, "password and verification password do not match", HttpStatus.BAD_REQUEST),
    DUPLICATE_USERNAME(1052, "the username or email entered is duplicate", HttpStatus.BAD_REQUEST),
    AUTH_INCORRECT_PASSWORD(1053,"password is incorrect",HttpStatus.BAD_REQUEST),

    POST_NOT_FOUND(1065, "post not found", HttpStatus.BAD_REQUEST),


    METHOD_ARGUMENT_NOT_VALID(1100, "request method argument not valid", HttpStatus.BAD_REQUEST),
    MISSING_REQUEST_PARAMETER(1101,"some required parameter is missing", HttpStatus.BAD_REQUEST),
    MISSING_REQUEST_AUTHORIZATION(1102,"request authorization header is missing", HttpStatus.FORBIDDEN),
    METHOD_ARGUMENT_TYPE_MISMATCH(1103,"Make sure the inputs value is correct", HttpStatus.BAD_REQUEST),


    USER_NOT_FOUND2(1050, "user not found", HttpStatus.BAD_REQUEST),

    ;

}