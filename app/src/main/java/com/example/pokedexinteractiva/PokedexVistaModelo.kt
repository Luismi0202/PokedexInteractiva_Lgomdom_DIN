package com.example.pokedexinteractiva

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class PokedexVistaModelo : ViewModel() {
    val pokemones = mutableStateListOf<PokemonUi>()
    var cargando by mutableStateOf(true)
        private set

    init {
        cargarPokemones()
    }

    // Obtiene flavor_text priorizando ES y luego EN, limpiando saltos y espacios
    private fun flavorText(entries: org.json.JSONArray?, preferidos: List<String> = listOf("es", "en")): String? {
        if (entries == null) return null

        fun limpiar(s: String) = s
            .replace("\n", " ")
            .replace("\r", " ")
            .replace("\u000c", " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        for (lang in preferidos) {
            for (i in 0 until entries.length()) {
                val e = entries.getJSONObject(i)
                if (e.optJSONObject("language")?.optString("name") == lang) {
                    return limpiar(e.optString("flavor_text"))
                }
            }
        }
        // Último recurso: primera entrada
        return limpiar(entries.optJSONObject(0)?.optString("flavor_text") ?: "")
    }

    private fun traducirTipo(tipoIngles: String): String {
        return tiposTraduccion[tipoIngles.lowercase()] ?: tipoIngles.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }

    private fun capitalizarNombre(nombre: String): String {
        return nombre.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }

    private fun cargarPokemones() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val listaJson = URL("https://pokeapi.co/api/v2/pokemon?limit=-1").readText()
                val resultados = JSONObject(listaJson).getJSONArray("results")
                for (i in 0 until resultados.length()) {
                    val r = resultados.getJSONObject(i)
                    val urlDetalle = r.optString("url")
                    try {
                        val detalle = JSONObject(URL(urlDetalle).readText())

                        val nombre = detalle.optString("name", r.optString("name"))
                        val imagen =
                            detalle.optJSONObject("sprites")?.optString("front_default", null)

                        val tiposList = mutableListOf<String>()
                        detalle.optJSONArray("types")?.let { ta ->
                            for (t in 0 until ta.length()) {
                                val typeName =
                                    ta.getJSONObject(t).getJSONObject("type").optString("name")
                                tiposList.add(traducirTipo(typeName))
                            }
                        }

                        val habilidadesList = mutableListOf<String>()
                        detalle.optJSONArray("abilities")?.let { aa ->
                            for (a in 0 until aa.length()) {
                                val abilityName =
                                    aa.getJSONObject(a).getJSONObject("ability").optString("name")
                                habilidadesList.add(abilityName)
                            }
                        }

                        var descripcion: String? = null
                        val speciesUrl = detalle.optJSONObject("species")?.optString("url", null)
                        if (speciesUrl != null) {
                            try {
                                val species = JSONObject(URL(speciesUrl).readText())
                                descripcion = flavorText(species.optJSONArray("flavor_text_entries"))
                            } catch (_: Exception) { /* ignorar */ }
                        }

                        val ui = PokemonUi(
                            nombre = capitalizarNombre(nombre),
                            imagenUrl = imagen,
                            tipos = tiposList,              // Ya traducidos
                            habilidades = habilidadesList,
                            descripcion = descripcion       // En ES si existe, si no EN
                        )

                        withContext(Dispatchers.Main) { pokemones.add(ui) }

                    } catch (_: Exception) {
                        // ignorar este pokemon y continuar
                    }
                }
            } catch (_: Exception) {
                // error global (p. ej. sin red)
            } finally {
                withContext(Dispatchers.Main) { cargando = false }
            }
        }
    }

    companion object {
        val tiposTraduccion = mapOf(
            "normal" to "Normal",
            "fire" to "Fuego",
            "water" to "Agua",
            "electric" to "Eléctrico",
            "grass" to "Planta",
            "ice" to "Hielo",
            "fighting" to "Lucha",
            "poison" to "Veneno",
            "ground" to "Tierra",
            "flying" to "Volador",
            "psychic" to "Psíquico",
            "bug" to "Bicho",
            "rock" to "Roca",
            "ghost" to "Fantasma",
            "dragon" to "Dragón",
            "dark" to "Siniestro",
            "steel" to "Acero",
            "fairy" to "Hada"
        )

        val coloresPorTipo = mapOf(
            "Normal" to Color(0xFFA8A878),
            "Fuego" to Color(0xFFF08030),
            "Agua" to Color(0xFF6890F0),
            "Eléctrico" to Color(0xFFF8D030),
            "Planta" to Color(0xFF78C850),
            "Hielo" to Color(0xFF98D8D8),
            "Lucha" to Color(0xFFC03028),
            "Veneno" to Color(0xFFA040A0),
            "Tierra" to Color(0xFFE0C068),
            "Volador" to Color(0xFFA890F0),
            "Psíquico" to Color(0xFFF85888),
            "Bicho" to Color(0xFFA8B820),
            "Roca" to Color(0xFFB8A038),
            "Fantasma" to Color(0xFF705898),
            "Dragón" to Color(0xFF7038F8),
            "Siniestro" to Color(0xFF705848),
            "Acero" to Color(0xFFB8B8D0),
            "Hada" to Color(0xFFEE99AC)
        )
    }
}
