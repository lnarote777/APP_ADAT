package com.es.aplicacion.service

import com.es.aplicacion.dto.UsuarioDTO
import com.es.aplicacion.dto.UsuarioRegisterDTO
import com.es.aplicacion.error.exception.BadRequestException
import com.es.aplicacion.error.exception.UnauthorizedException
import com.es.aplicacion.model.Usuario
import com.es.aplicacion.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder


    override fun loadUserByUsername(username: String?): UserDetails {
        var usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow {
                UnauthorizedException("$username no existente")
            }

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.roles)
            .build()
    }

    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : UsuarioDTO? {

        if (usuarioInsertadoDTO.username .isBlank()
            || usuarioInsertadoDTO.password.isBlank()
            || usuarioInsertadoDTO.email.isBlank()) {
            throw BadRequestException("Rellene los campos en blanco.")
        }

        if (usuarioInsertadoDTO.password != usuarioInsertadoDTO.passwordRepeat) {
            throw BadRequestException("Las contraseñas no coinciden.")
        }

        if (!usuarioInsertadoDTO.email.contains("@")){
            throw BadRequestException("Ingrese un email válido.")
        }

        val userExist = usuarioRepository.findByUsername(usuarioInsertadoDTO.username)

        if (userExist.isPresent) {
            throw BadRequestException("Usuario existente.")
        }

        val passEncode = passwordEncoder.encode(usuarioInsertadoDTO.password)

        val usuario = Usuario(
            username = usuarioInsertadoDTO.username,
            password = passEncode,
            roles = usuarioInsertadoDTO.rol ?: "USER",
            email = usuarioInsertadoDTO.email,
            _id = null
        )

        val usuarioIsert = usuarioRepository.insert(usuario)

        val usuarioDTO = UsuarioDTO(
            username = usuarioIsert.username,
            rol = usuarioIsert.roles,
            email = usuarioIsert.email,
        )

        return usuarioDTO

    }
}