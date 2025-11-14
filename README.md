# PokedexInteractiva_Lgomdom_DIN

Dejo en el Read me donde se encuentran desarrolladas las partes del ejercicio para que sea más fácil de encontrar ;)

>"LA APLICACIÓN INCLUIRÁ 3 VISTAS DIFERENTES"

**VISTA 1** Lista Vertical (RecyclerView- Lazy Column)
https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L221-L244

**VISTA 2** Vista en cuadrícula (LazyVerticalGrid)
https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L246-L272

**VISTA 3** Vista agrupada por tipo
https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L274-L360

¿Porque está vista tiene un buscador? Pues es que he aprovechado que he hecho que carguen todos los pokemons de antemano (ya que si dejaba que arrastrases para abajo y se fueran cargando con el lazy column el sticky header daba muchos problemas debido a la cantidad de pokemons que carga la API), puse un buscador en esta pestaña para que puedas buscar el pokemon correspondiente

>"Al hacer clic en un Pokémon en cualquiera de las vistas, se debe abrir un Dialog con su información detallada mostrando nombre,tipo, descripción o habilidades e imagen ampliada. Opcionalmente incluye botones como ver en pokédex o cerrar"

 https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokemonCard.kt#L19-L56

 Esta es la clase reciclable que es una card del pokemon

 https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L70

 Declaramos el pokemon seleccionado como null primero

 https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L179-L198

 En el selector de vista, llamamos a cada una de las funciones de vista y le decimos que si alguien pulsa a un pokemon de la lista que le estamos pasando por parámetro, entonces la variable de "selectedpokemon" pasa a ser ese pokemon.
 
 https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L213-L219

Si el selected pokemon no es nulo, llama a la función de Pokemon Dialog que hará que se abra un cuadro con la imagen ampliada, nombre, descripción y abajo puse un botón de cerrar y otro de escuchar el sonido (ya que el de ver en pokédex que decía el ejercicio no le veo tanto el sentido y creo que aprovechando que la api tenía el sonidito de cada uno de los pokémons pues podía aprovecharlo para que quedará mas guay)

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L404-L481

Hice también algo similar pero con una variable que se llama abrir filtro, cuando está en true, abre el dialog del selector de filtro, esto lo que hará es que puedas seleccionar con unos radio buttons hasta que generación quieres que te ponga los pokémons. Esto lo realicé ya que ví que cuando haces el fetch a la api podías ponerle un limite de número de pokemons entonces lo inicialicé con el máximo para que llegará hasta la generación del escarlata (creo, no jugué mucho pokémon pero diría que sí) y luego si en el filtro seleccionar por ejemplo "Gen 1", el número de ese límite cambiaría al del número de pokemons que hay en la gen 1. Aquí dejo los fragmentos del código que creo que se entiende mejor así:

**VARIABLE DE FILTRO ABIERTO**
https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L69
https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L126-L131

CUANDO LE DAS AL BOTÓN DEL FILTRO SE PASA LA VARIABLE A TRUE

ESTO HACE QUE LLAME A LA FUNCIÓN DEL FILTRO GEN

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L200-L211

**LISTA DE FILTROS**
https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L85-L96

**FUNCIÓN DE FILTRO GEN**
https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L362-L402

ESTO LLAMA A LA FUNCIÓN DE APLICAR EL FILTRO EN EL VIEW MODEL YA QUE AHÍ ESTA LA VARIABLE DEL LÍMITE Y CAMBIARÁ EN EL FETCH DE LA API (LLAMADA A LA API)

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexVistaModelo.kt#L31-L38
https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexVistaModelo.kt#L77

Se ve en el segundo fragmento de código esa variable del límite.

**RESULTADOS DE APRENDIZAJES EVALUADOS**

>RA3: Crea componentes visuales valorando y empleando herramientas especificas

-**CE b) Creación de componentes reutilizables**

Como ya dejé claro anteriormente en este readme, hay funciones reutilizables y también componentes como las funciones de los dialogs, y las cards de los pokemons que es un componente completamente aparte que todas las vistas llaman para hacer las cards de los pokemons

-**CE e) Realización de pruebas básicas sobre los componentes**

Ya con el simple hecho de ejecutar la aplicación, ves como las tarjetas se integran perfectamente en la app.

-**CE h) Programación de una aplicación que integra los componentes desarrollados**

La aplicación integra el componente creado de PokemonCard cada vez que cambias de vista ya que cada vista tiene que hacer que cada pokemon que le de la api vaya en un pokemon card y además estas cards van variando dependiendo la vista ya que por ejemplo en la vista 2, se quiere que se vea todo en celdas.

>RA4: Diseña interfaces gráficas aplicando criterios de usabilidad y accesibilidad

-**CE g) Diseño visual atendiendo a legibilidad, colores y tipografía**

En la vista 3 (ordenar por tipos), los sticky header tienen un color de fondo que están relacionados con el tipo, siendo agua azul por ejemplo, identificándolos de una forma más sencilla y haciendo que sea más reconfortante a la vista.

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexVistaModelo.kt#L158-L176

Las tarjetas tienen un color gris claro para mejorar el contraste con el texto y las imágenes.

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokemonCard.kt#L27-L28

También se tiene en cuenta una jerarquía tipográfica, haciendo que el nombre del pokémon sea más grande que el tipo.

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokemonCard.kt#L44-L52

Por último, comentar también que en las cabeceras sticky, se usan colores del tipo con texto blanco para que haya un contraste y legibilidad.

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L326-L340

**CE h) Claridad en los mensajes y textos mostrados al usuario**

Este se ve claramente cuando entras en la vista 3 y te dice que se están cargando los pokemons, ya que me encargué personalmente de poner un circular progress indicator para que saliese un círculo de que se están cargando y el usuario supiese que tiene que esperar a que carguen todos los pokémons antes de verlos.

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L298-L306

También hay por todo el código mensajes de error (a pesar de que dificilmente puedan ocurrir) como cuando abres la aplicación sin internet y no cargan los pokemons o si buscamos un pokemon en el buscador que no existe en la lista.

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L308-L317

**CE i) Pruebas de usabilidad y accesibilidad visual**

Se usan content descriptions para que los lectores de pantalla lean la imágen

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokemonCard.kt#L39-L41

Se indican también con content description las funciones que tienen los botones

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L116-L129

Y por último, también comentar que el buscador también tiene un content description para que se sepa donde está

https://github.com/Luismi0202/PokedexInteractiva_Lgomdom_DIN/blob/f1f44deb0bdf888b94a9959d114c1b213efe4481/app/src/main/java/com/example/pokedexinteractiva/PokedexScreen.kt#L151-L167
