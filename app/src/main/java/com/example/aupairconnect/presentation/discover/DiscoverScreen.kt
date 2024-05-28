package com.example.aupairconnect

import android.media.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aupairconnect.domain.model.User
import com.example.aupairconnect.presentation.components.AupairCard
import com.example.aupairconnect.presentation.discover.DiscoverViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val PAGE_SIZE = 20

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DiscoverScreen(
    onNavigation: NavHostController,
    viewModel: DiscoverViewModel
) {

    val pagerState = rememberPagerState(pageCount = {
        4
    })

    fun refresh() {
        CoroutineScope(Dispatchers.IO).launch {
//            refreshing.value = true
            println("Loading")

        }

        //...do something
//        refreshing.value = false
    }

    val refreshingState = rememberPullRefreshState(viewModel.refreshing.value, :: refresh)

    // TODO: Uncomment once we get the datastore & storage repositories
    var aupairList = viewModel.aupairList.value

    val shape =  RoundedCornerShape(16.dp)
//    HorizontalPager(pageCount = aupairList.size,
//        state = pagerState,
//        userScrollEnabled = true,
//        verticalAlignment = Alignment.Top,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.LightGray)
//    ) { page ->
//        AupairCard(aupair = aupairList[page])
//
//    }

    Box(modifier = Modifier.pullRefresh(refreshingState)) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp), content = {
            itemsIndexed(items = viewModel.aupairList.value) {index, aupair ->
                println(aupair)
//            viewModel.onChangeMovieScrollPosition(index)
//            if((index + 1) >= (page * PAGE_SIZE) && !loading){
//                viewModel.nextPage()
//            }
//            Box(){
//                aupair.
//            }
//            AupairCard(aupair)
                Box (modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                    .border(0.dp, Color.White, shape)
//                .fillMaxSize()
                    .height(260.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Blue)
//                .paint(painterResource(id = R.drawable.aupairphoto4))
                ){
                    androidx.compose.foundation.Image(painter = painterResource(id = R.drawable.aupairphoto4), contentDescription = "User Image", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
//                com.example.aupairconnect.presentation.components.RoundedImage(painterResource(id = R.drawable.aupairphoto4))
                    Column(modifier = Modifier
                        .align(Alignment.BottomStart)) {
                        Text(text = aupair.name.toString(), modifier = Modifier.padding(start = 8.dp), color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text(text = aupair.originLocation.toString(), modifier = Modifier.padding(start = 8.dp), color = Color.White, fontSize = 25.sp)
//                    Text(text = aupair.age.toString(), fontSize = 25.sp)
//                    if(aupair.isExAupair == true){
//                        Text(text = "Ex Au pair", fontSize = 25.sp)
//                    }
                    }


                }
            }
        })

        PullRefreshIndicator(viewModel.refreshing.value, refreshingState, Modifier.align(Alignment.TopCenter) )
    }


}