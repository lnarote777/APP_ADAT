package com.es.aplicacion.domain

import com.example.mongospring.domain.Provincia

class DatosMunicipios (
    val update_date: String,
    val size: Int,
    val data: List<Municipio>?,
    val warning: String?,
    val error: String?
)