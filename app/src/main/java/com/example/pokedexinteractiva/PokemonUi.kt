package com.example.pokedexinteractiva

data class PokemonUi(
    val nombre: String,
    val imagenUrl: String?,
    val tipos: List<String>,
    val habilidades: List<String>,
    val descripcion: String?,
    val soundUrl: String? = null
)
