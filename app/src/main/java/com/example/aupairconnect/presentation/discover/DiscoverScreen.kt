package com.example.aupairconnect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aupairconnect.presentation.components.AupairCard
import com.example.aupairconnect.presentation.discover.DiscoverViewModel
import com.example.aupairconnect.presentation.ui.theme.ACTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class) //For Accompanist HorizontalPager
@Composable
fun DiscoverScreen(
    onNavigation: NavHostController,
    viewModel: DiscoverViewModel
) {

    val pagerState = rememberPagerState()

    // TODO: Uncomment once we get the datastore & storage repositories
    var aupairList = viewModel.aupairList.value

//    LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.background(Color.White).padding(bottom = 55.dp), content = {
//        itemsIndexed(items = aupairList) {index, aupair ->
////            viewModel.onChangeMovieScrollPosition(index)
////            if((index + 1) >= (page * PAGE_SIZE) && !loading){
////                viewModel.nextPage()
////            }
////            MovieCard(movie = movie,onNavigation=onNavigation) {
//
////            }
//            AupairCard(aupair)
//        }
//    })

//    val shape =  RoundedCornerShape(8.dp)
    HorizontalPager(count = aupairList.size,
        state = pagerState,
        userScrollEnabled = true,
        modifier = Modifier
            .fillMaxSize()
//            .clip(shape)
//            .background(Color.White, shape = shape)
    ) { page ->
        AupairCard(aupair = aupairList[page])
    }

}