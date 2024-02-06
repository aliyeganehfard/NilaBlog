package com.nila.blog.service.imgl

import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.database.model.User
import com.nila.blog.database.repository.UserRepository
import com.nila.blog.service.IUserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService: IUserService {

    @Autowired
    lateinit var userRepository: UserRepository

    private val log = LoggerFactory.getLogger(JWTVerificationService::class.java)

    @Transactional
    override fun save(user: User) {
        userRepository.save(user)
        log.info("saved user with id {}", user.id)
    }

    override fun existByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }
}