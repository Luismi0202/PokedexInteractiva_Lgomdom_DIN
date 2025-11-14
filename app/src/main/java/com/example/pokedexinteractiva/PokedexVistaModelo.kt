package com.example.pokedexinteractiva

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private fun textoInglesDe(entries: org.json.JSONArray?): String? {
        if (entries == null) return null
        for (i in 0 until entries.length()) {
            val entry = entries.getJSONObject(i)
            val lang = entry.getJSONObject("language").optString("name")
            if (lang == "en") {
                return entry.optString("flavor_text").replace("\n", " ").replace("\r", " ")
            }
        }
        return null
    }

    private fun cargarPokemones() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val listaJson = URL("https://pokeapi.co/api/v2/pokemon?limit=151").readText()
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
                                tiposList.add(typeName)
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
                                descripcion =
                                    textoInglesDe(species.optJSONArray("flavor_text_entries"))
                            } catch (_: Exception) { /* ignorar */
                            }
                        }

                        val ui = PokemonUi(
                            nombre = nombre,
                            imagenUrl = imagen,
                            tipos = tiposList,
                            habilidades = habilidadesList,
                            descripcion = descripcion
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
}