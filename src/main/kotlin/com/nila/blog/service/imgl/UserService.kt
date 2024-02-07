package com.nila.blog.service.imgl

import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.common.dto.user.req.UserEditProfileReq
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
class UserService : IUserService {

    @Autowired
    lateinit var userRepository: UserRepository

    private val log = LoggerFactory.getLogger(UserService::class.java)

    @Transactional
    override fun save(user: User) {
        userRepository.save(user)
        log.info("saved user with username {}", user.username)
    }

    @Transactional
    override fun uploadUserProfile(image: MultipartFile, userId: String) {
        val user = findById(UUID.fromString(userId))
        user.profilePictures = image.bytes
        userRepository.save(user)
        log.info("upload profile picture for user with username {}", user.username)
    }

    @Transactional
    override fun editProfile(userId: String, req: UserEditProfileReq) {
        val id = UUID.fromString(userId)

        if (!userRepository.isValidUsernameAndEmailForUpdate(id, req.username, req.email)) {
            log.warn(ErrorCode.DUPLICATE_USERNAME.message)
            throw BlogException(ErrorCode.DUPLICATE_USERNAME)
        }

        val user = findById(id)
        user.username = req.username
        user.email = req.email

        userRepository.save(user)
        log.info("updated user {} profile ", user.username)
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