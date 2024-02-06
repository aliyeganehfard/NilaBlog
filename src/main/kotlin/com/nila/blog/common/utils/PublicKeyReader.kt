package com.nila.blog.common.utils

import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import java.util.*

object PublicKeyReader {

    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPublicKey(filename: String?): RSAPublicKey {
        val resource = ClassPathResource(filename!!)
        val reader = BufferedReader(InputStreamReader(resource.inputStream, StandardCharsets.UTF_8))
        val builder = StringBuilder()
        var line: String
        while (reader.readLine().also { line = it } != null) {
            if (!line.contains("BEGIN RSA PUBLIC KEY") && !line.contains("END RSA PUBLIC KEY")) {
                builder.append(line.trim { it <= ' ' })
            }
        }
        val publicKeyBytes = Base64.getDecoder().decode(builder.toString())
        val keySpec = X509EncodedKeySpec(publicKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }
}