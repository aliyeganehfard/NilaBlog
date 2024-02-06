package com.nila.blog.common.utils

import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

object RSAUtils {

    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPublicKey(filename: String?): RSAPublicKey {
        val publicKeyBytes = getKeyBytes(filename)
        val keySpec = X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPrivateKey(filename: String?): RSAPrivateKey {
        val privateKeyBytes = getKeyBytes(filename)
        val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

    private fun getKeyBytes(filename: String?): ByteArray? {
        val resource = ClassPathResource(filename!!)
        val reader = BufferedReader(InputStreamReader(resource.inputStream, StandardCharsets.UTF_8))
        val builder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            if (!line!!.contains("BEGIN RSA PRIVATE KEY") && !line!!.contains("END RSA PRIVATE KEY") &&
                !line!!.contains("BEGIN RSA PUBLIC KEY") && !line!!.contains("END RSA PUBLIC KEY")) {
                builder.append(line!!.trim { it <= ' ' })
            }
        }
        return Base64.getDecoder().decode(builder.toString())
    }
}