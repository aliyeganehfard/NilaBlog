package com.nila.blog.common.filter

import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.nila.blog.common.aop.ErrorCode
import com.nila.blog.common.aop.exeptions.BlogException
import com.nila.blog.common.config.JWTVerificationService
import com.nila.blog.common.config.JWTVerificationService.Companion.CLAIM_ROLES
import com.nila.blog.common.config.UserSecurityService
import com.nila.blog.common.dto.GeneralResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*


@Component
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtVerificationService: JWTVerificationService

    @Autowired
    lateinit var userSecurityService: UserSecurityService

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            if (request.servletPath == "/v1/auth/signIn" || request.servletPath == "/v1/auth/signUp") {
                filterChain.doFilter(request, response)
                return
            }
            val authHeader = request.getHeader("Authorization")
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response)
                return
            }
            val token = authHeader.substring("Bearer ".length)
            val decodedJWT: DecodedJWT = jwtVerificationService.getDecodedJWT(token)
            val username = decodedJWT.subject
            val roles = decodedJWT.getClaim(CLAIM_ROLES).asArray(String::class.java)
            val authorities: MutableList<SimpleGrantedAuthority> = ArrayList()
            Arrays.stream(roles).forEach { role: String? ->
                authorities.add(
                    SimpleGrantedAuthority(role)
                )
            }
            val authToken = UsernamePasswordAuthenticationToken(username, null, authorities)
            userSecurityService.setCurrentUser(authToken)
            filterChain.doFilter(request, response)
        } catch (blogException: BlogException) {
            val resMessage = GeneralResponse.unsuccessfulResponse<Any>(blogException.errorCode!!)
            setResponse(response, resMessage, blogException.errorCode!!.httpStatus!!)
        } catch (e: Exception) {
            val resMessage = GeneralResponse.unsuccessfulResponse<Any>(ErrorCode.INTERNAL_SERVER_ERROR)
            setResponse(response, resMessage)
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun setResponse(response: HttpServletResponse, resMessage: GeneralResponse<Any>) {
      setResponse(response, resMessage, HttpStatus.BAD_REQUEST)
    }

    @Throws(IOException::class)
    private fun setResponse(response: HttpServletResponse, resMessage: GeneralResponse<Any>, status : HttpStatus) {
        val mapper = ObjectMapper()
        response.status = status.value()
        response.characterEncoding = "utf-8"
        response.contentType = "application/json"
        response.writer.write(mapper.writeValueAsString(resMessage))
    }
}
