package com.example.pokedexinteractiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedexinteractiva.ui.theme.PokedexInteractivaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexInteractivaTheme {
                //TODO
            }
        }
    }
}

@Composable
fun PokedexPantalla(modifier:Modifier = Modifier){
    val vistas = listOf("Lista vertical","Vista en cuadrícula","Vista agrupada por tipos")
    var vistaSeleccionada by rememberSaveable { mutableStateOf(vistas[0]) }
    val viewModel = remember { PokedexVistaModelo() }
    val pokemones = viewModel.pokemones
    var abierto by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = vistaSeleccionada,
            onValueChange = { },
            readOnly = true,
            label = { Text("Selección de vistas") },
            trailingIcon = {
                IconButton(onClick = { abierto = !abierto }) {
                    Icon(
                        imageVector = if (abierto) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { abierto = !abierto }
        )

        DropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false },
            modifier = Modifier
                .fillMaxWidth()
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
            "Lista vertical" -> listaVerticalPokemones(pokemones, modifier = Modifier.weight(1f))
            "Vista en cuadrícula" -> listaVistaCuadricula(pokemones, modifier = Modifier.weight(1f))
        }
    }
}




@Composable
fun listaVerticalPokemones(pokemones: List<PokemonUi>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pokemones.size) { index ->
            val pokemon = pokemones[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
fun listaVistaCuadricula(pokemones: List<PokemonUi>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pokemones.size) { index ->
            val pokemon = pokemones[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pokemon.nombre,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
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

@Composable
fun listaAgrupadaPorTipos(pokemones: List<PokemonUi>, modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun PokedexPreview() {
    PokedexInteractivaTheme {
        PokedexPantalla()
    }
}

data class PokemonUi(
    val nombre: String,
    val imagenUrl: String?,
    val tipos: List<String>,
    val habilidades: List<String>,
    val descripcion: String?
)