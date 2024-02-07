package com.nila.blog.service.imgl

import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.database.model.User
import com.nila.blog.database.repository.UserRepository
import com.nila.blog.service.IUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class UserService: IUserService {

    @Autowired
    lateinit var userRepository: UserRepository

    private val log = LoggerFactory.getLogger(JWTVerificationService::class.java)

    @Transactional
    override fun save(user: User) {
        userRepository.save(user)
        log.info("saved user with username {}", user.username)
    }

    @Transactional
    override fun uploadUserProfile(image: MultipartFile, userId: String) {
        val user = findById(UUID.fromString(userId))
        val imageBase64 = Base64.getEncoder().encodeToString(image.bytes)
        user.profilePictures = image.bytes
        userRepository.save(user)
        log.info("upload profile picture for user with username {}", user.username)
    }

    override fun existByUsernameOrEmail(username: String, email: String): Boolean {
        return userRepository.existsByUsernameOrEmail(username, email)
    }

    override fun findByUsername(username: String): User {
        return userRepository.findByUsername(username)
            .orElseThrow {
                log.error(ErrorCode.USER_NOT_FOUND.message)
                BlogException(ErrorCode.USER_NOT_FOUND)
            }
    }

    override fun findById(userId: UUID): User {
        return userRepository.findById(userId)
            .orElseThrow {
                log.error(ErrorCode.USER_NOT_FOUND.message)
                BlogException(ErrorCode.USER_NOT_FOUND)
            }
    }
}