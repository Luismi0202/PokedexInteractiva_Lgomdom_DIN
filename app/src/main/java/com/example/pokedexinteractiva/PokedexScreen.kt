package com.example.pokedexinteractiva.ui

import android.media.MediaPlayer
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokedexinteractiva.PokedexVistaModelo
import com.example.pokedexinteractiva.PokemonUi

@Composable
fun PokedexScreen() {
    val vistas = listOf("Lista vertical", "Vista en cuadrícula", "Vista agrupada por tipos")
    var vistaSeleccionada by rememberSaveable { mutableStateOf(vistas[0]) }
    val viewModel: PokedexVistaModelo = viewModel()
    val pokemones = viewModel.pokemones

    var abierto by rememberSaveable { mutableStateOf(false) }
    var abrirFiltro by remember { mutableStateOf(false) }
    var selectedPokemon by remember { mutableStateOf<PokemonUi?>(null) }

    val opcionesGen = listOf(
        "Todas" to -1,
        "Gen 1 (1–151)" to 151,
        "Gen 2 (1–251)" to 251,
        "Gen 3 (1–386)" to 386,
        "Gen 4 (1–493)" to 493,
        "Gen 5 (1–649)" to 649,
        "Gen 6 (1–721)" to 721,
        "Gen 7 (1–809)" to 809,
        "Gen 8 (1–905)" to 905,
        "Gen 9 (1–1025)" to 1025
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                OutlinedTextField(
                    value = vistaSeleccionada,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Selección de vistas") },
                    trailingIcon = {
                        IconButton(onClick = { abierto = !abierto }) {
                            Icon(
                                imageVector = if (abierto) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { abierto = !abierto }
                )
                IconButton(onClick = { abrirFiltro = true }) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Filtrar por generación"
                    )
                }
            }

            DropdownMenu(
                expanded = abierto,
                onDismissRequest = { abierto = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                vistas.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            vistaSeleccionada = opcion
                            abierto = false
                        }
                    )
                }
            }

            when (vistaSeleccionada) {
                "Lista vertical" -> ListaVerticalPokemones(
                    pokemones = pokemones,
                    modifier = Modifier.weight(1f),
                    onPokemonClick = { selectedPokemon = it }
                )
                "Vista en cuadrícula" -> ListaVistaCuadricula(
                    pokemones = pokemones,
                    modifier = Modifier.weight(1f),
                    onPokemonClick = { selectedPokemon = it }
                )
                "Vista agrupada por tipos" -> ListaAgrupadaPorTipos(
                    pokemones = pokemones,
                    cargando = viewModel.cargando,
                    modifier = Modifier.weight(1f),
                    onPokemonClick = { selectedPokemon = it }
                )
            }
        }
    }

    if (abrirFiltro) {
        val idxInicial = opcionesGen.indexOfFirst { it.second == viewModel.limite }.let { if (it >= 0) it else 0 }
        FiltroGenDialog(
            opciones = opcionesGen,
            indiceInicial = idxInicial,
            onDismiss = { abrirFiltro = false },
            onApply = { indice ->
                viewModel.aplicarFiltroGen(opcionesGen[indice].second)
                abrirFiltro = false
            }
        )
    }

    selectedPokemon?.let { pokemon ->
        PokemonDialog(
            pokemon = pokemon,
            onDismiss = { selectedPokemon = null }
        )
    }
}

@Composable
fun ListaVerticalPokemones(
    pokemones: List<PokemonUi>,
    modifier: Modifier = Modifier,
    onPokemonClick: (PokemonUi) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = pokemones,
            key = { it.nombre }
        ) { pokemon ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onPokemonClick(pokemon) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = pokemon.nombre)
                    Text(text = "Tipos: ${pokemon.tipos.joinToString(", ")}")
                    AsyncImage(
                        model = pokemon.imagenUrl,
                        contentDescription = pokemon.nombre,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ListaVistaCuadricula(
    pokemones: List<PokemonUi>,
    modifier: Modifier = Modifier,
    onPokemonClick: (PokemonUi) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = pokemones,
            key = { it.nombre }
        ) { pokemon ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable { onPokemonClick(pokemon) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pokemon.nombre,
                        fontSize = 12.sp
                    )
                    AsyncImage(
                        model = pokemon.imagenUrl,
                        contentDescription = pokemon.nombre,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListaAgrupadaPorTipos(
    pokemones: List<PokemonUi>,
    cargando: Boolean,
    modifier: Modifier = Modifier,
    onPokemonClick: (PokemonUi) -> Unit
) {
    val listState = rememberLazyListState()

    val pokemonesPorTipo = remember(pokemones.size, cargando) {
        if (cargando || pokemones.isEmpty()) {
            emptyList()
        } else {
            val tipos = pokemones.flatMap { it.tipos }.distinct().sorted()
            tipos.map { tipo ->
                tipo to pokemones.filter { it.tipos.contains(tipo) }
            }
        }
    }

    if (cargando) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Cargando pokémones...")
        }
    } else {
        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pokemonesPorTipo.forEach { (tipo, pokemonesDelTipo) ->
                stickyHeader(key = "header-$tipo") {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = PokedexVistaModelo.coloresPorTipo[tipo] ?: Color.Gray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = tipo,
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                items(
                    items = pokemonesDelTipo,
                    key = { it.nombre }
                ) { pokemon ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { onPokemonClick(pokemon) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = pokemon.nombre)
                            Text(text = "Tipos: ${pokemon.tipos.joinToString(", ")}")
                            AsyncImage(
                                model = pokemon.imagenUrl,
                                contentDescription = pokemon.nombre,
                                modifier = Modifier
                                    .size(150.dp)
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FiltroGenDialog(
    opciones: List<Pair<String, Int>>,
    indiceInicial: Int,
    onDismiss: () -> Unit,
    onApply: (Int) -> Unit
) {
    var seleccionado by remember { mutableStateOf(indiceInicial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar por generación") },
        text = {
            Column {
                opciones.forEachIndexed { index, (titulo, _) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { seleccionado = index }
                            .padding(vertical = 6.dp)
                    ) {
                        RadioButton(
                            selected = seleccionado == index,
                            onClick = { seleccionado = index }
                        )
                        Text(text = titulo, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onApply(seleccionado) }) { Text("Aplicar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun PokemonDialog(pokemon: PokemonUi, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    AlertDialog(
        onDismissRequest = {
            mediaPlayer?.release()
            onDismiss()
        },
        title = { Text(text = pokemon.nombre) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = pokemon.imagenUrl,
                    contentDescription = pokemon.nombre,
                    modifier = Modifier.size(180.dp)
                )
                Text(
                    text = "Tipo: ${pokemon.tipos.joinToString(", ")}",
                    modifier = Modifier.padding(top = 8.dp)
                )
                val detalle = pokemon.descripcion
                    ?: "Habilidades: ${pokemon.habilidades.joinToString(", ")}"
                Text(
                    text = detalle,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                mediaPlayer?.release()
                onDismiss()
            }) {
                Text("Cerrar")
            }
        },
        dismissButton = {
            if (pokemon.soundUrl != null) {
                TextButton(onClick = {
                    try {
                        mediaPlayer?.release()
                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(pokemon.soundUrl)
                            prepareAsync()
                            setOnPreparedListener { it.start() }
                            setOnCompletionListener { it.release() }
                        }
                    } catch (e: Exception) {
                        // Manejar error silenciosamente
                    }
                }) {
                    Text("Reproducir sonido")
                }
            }
        }
    )
}
