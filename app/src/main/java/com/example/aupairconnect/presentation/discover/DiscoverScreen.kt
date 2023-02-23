package com.example.aupairconnect

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.aupairconnect.presentation.discover.DiscoverViewModel

@Composable
fun DiscoverScreen(
    onNavigation: NavHostController,
    viewModel: DiscoverViewModel
) {

    // TODO: Uncomment once we get the datastore & storage repositories
//    val moviesList = viewModel.movies.value
//
//    LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.background(TMDBColor), content = {
//        itemsIndexed(items = moviesList) {index, movie ->
//            println(movie)
//            viewModel.onChangeMovieScrollPosition(index)
//            if((index + 1) >= (page * PAGE_SIZE) && !loading){
//                viewModel.nextPage()
//            }
//            MovieCard(movie = movie,onNavigation=onNavigation) {
//
//            }
//        }
//    })
}