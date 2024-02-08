package com.nila.blog.common.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.dto.authentication.res.AuthenticationResponse
import com.nila.blog.common.utils.RSAUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*


@Service
class JwtService {

    @Autowired
    lateinit var jwtVerificationService: JWTVerificationService

    @Autowired
    lateinit var env: Environment

    private val log = LoggerFactory.getLogger(JwtService::class.java)

    private final var expirationTokenTime: Long? = null
    private final var expirationRefreshTokenTime: Long? = null
    private final var privateKey: RSAPrivateKey? = null
    private final var publicKey: RSAPublicKey? = null

    @Autowired
    fun init() {
        try {
            expirationTokenTime =
                env.getProperty("auth.service.expiration.token.time", Long::class.java, 30L) * 60L * 1000L
            expirationRefreshTokenTime = env.getProperty(
                "auth.service.expiration.refresh.token.time",
                Long::class.java, 30L
            ) * 60L * 1000L
            privateKey = RSAUtils.getPrivateKey("private_key")
            publicKey = jwtVerificationService.getRSAPublicKey()
        } catch (e: Exception) {
            log.error(ErrorCode.RSA_TROUBLE_READ_PRIVATE_KEY.message)
            throw BlogException(ErrorCode.RSA_TROUBLE_READ_PRIVATE_KEY, e)
        }
    }

    fun getToken(payload: Map<String, List<String?>>, userDetails: UserDetails): AuthenticationResponse {
        val jwt = AuthenticationResponse()
        val accessToken = generateAccessToken(payload, userDetails)
        jwt.accessToken = accessToken
        jwt.tokenType = TOKEN_TYPE
        return jwt
    }

    fun getDecodedJWT(token: String?): DecodedJWT {
        return jwtVerificationService.getDecodedJWT(token)
    }

    private fun generateAccessToken(payload: Map<String, List<String?>>, userDetails: UserDetails): String {
        val builder = JWT.create()
            .withSubject(userDetails.username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTokenTime!!))
        payload.forEach { (name: String?, list: List<String?>?) ->
            builder.withClaim(
                name,
                list
            )
        }
        return builder.sign(signingAlgorithm)
    }

    private val signingAlgorithm: Algorithm
        get() = Algorithm.RSA256(publicKey, privateKey)

    companion object {
        var TOKEN_TYPE = "Bearer"
    }
}