package com.nila.blog.common.utils

import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

object PrivateKeyReader {

    @Throws(IOException::class, NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPrivateKey(filename: String?): RSAPrivateKey {
        val resource = ClassPathResource(filename!!)
        val reader = BufferedReader(InputStreamReader(resource.inputStream, StandardCharsets.UTF_8))
        val builder = StringBuilder()
        var line: String
        while (reader.readLine().also { line = it } != null) {
            if (!line.contains("BEGIN RSA PRIVATE KEY") && !line.contains("END RSA PRIVATE KEY")) {
                builder.append(line.trim { it <= ' ' })
            }
        }
        val privateKeyBytes = Base64.getDecoder().decode(builder.toString())
        val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }
}