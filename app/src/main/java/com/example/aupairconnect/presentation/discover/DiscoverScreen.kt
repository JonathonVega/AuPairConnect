package com.example.aupairconnect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val shape =  RoundedCornerShape(16.dp)
    HorizontalPager(count = aupairList.size,
        state = pagerState,
        userScrollEnabled = true,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxSize().background(Color.LightGray)
    ) { page ->

        AupairCard(aupair = aupairList[page])

    }

}