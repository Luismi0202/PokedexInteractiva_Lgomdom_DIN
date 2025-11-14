package com.example.pokedexinteractiva

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun PokemonCard(
    pokemon: PokemonUi,
    onClick: (PokemonUi) -> Unit,
    imageSize: Int = 100,
    textSize: Int = 16,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(pokemon) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.imagenUrl,
                contentDescription = "Imagen de ${pokemon.nombre}",
                modifier = Modifier.size(imageSize.dp)
            )
            Text(
                text = pokemon.nombre,
                fontSize = textSize.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = pokemon.tipos.joinToString(", "),
                fontSize = (textSize - 2).sp,
                color = Color.Gray
            )
        }
    }
}
