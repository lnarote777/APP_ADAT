package com.es.aplicacion.dto

data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    var password: String,
    val passwordRepeat: String,
    val rol: String?
)
