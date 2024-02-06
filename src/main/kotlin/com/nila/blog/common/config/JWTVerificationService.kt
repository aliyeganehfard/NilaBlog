package com.nila.blog.common.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.utils.RSAUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPublicKey
import java.util.*


@Service
class JWTVerificationService {

    @Autowired
    lateinit var resourceLoader: ResourceLoader

    private final var publicKey: RSAPublicKey? = null

    private val log = LoggerFactory.getLogger(JWTVerificationService::class.java)

    @Autowired
    fun init() {
        try {
            publicKey = RSAUtils.getPublicKey("public_key.pub")
            log.info("reading public key was successful!")
        } catch (e: Exception) {
            log.error(ErrorCode.RSA_TROUBLE_READ_PUBLIC_KEY.message)
            throw BlogException(ErrorCode.RSA_TROUBLE_READ_PUBLIC_KEY,e)
        }
    }

    fun getRSAPublicKey(): RSAPublicKey? {
        return publicKey
    }

    fun getDecodedJWT(token: String?): DecodedJWT {
        var token = token
        if (token!!.startsWith("Bearer")) {
            token = token.substring("Bearer ".length)
        }
        return try {
            val verifier = JWT.require(verificationAlgorithm).build()
            val decodedJWT = verifier.verify(token)
            log.info("token verified with subject {}", decodedJWT.subject)
            decodedJWT
        } catch (tokenExpiredException: TokenExpiredException) {
            log.error(ErrorCode.TOKEN_EXPIRED.message)
            throw BlogException(ErrorCode.TOKEN_EXPIRED)
        } catch (e: Exception) {
            log.error(ErrorCode.TOKEN_INVALID.message)
            throw BlogException(ErrorCode.TOKEN_INVALID)
        }
    }

    fun getUuid(token: String): String {
        val decodedJWT = getDecodedJWT(token)
        return decodedJWT.getClaim(UUID)
            .asList(String::class.java)[0]
    }

    fun getClaims(token: String): Map<String, Claim> {
        val decodedJWT = getDecodedJWT(token)
        return decodedJWT.claims
    }

    fun isJWTExpired(token: String): Boolean {
        return try {
            val decodedJWT = getDecodedJWT(token)
            val expiresAt = decodedJWT.expiresAt
            expiresAt.before(Date())
        } catch (e: Exception) {
            true
        }
    }

    private val verificationAlgorithm: Algorithm
        get() = Algorithm.RSA256(publicKey, null)

    companion object {
        const val CLAIM_ROLES = "roles"
        const val UUID = "UUID"
    }
}