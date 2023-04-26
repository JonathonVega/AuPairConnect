package com.example.aupairconnect

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aupairconnect.presentation.components.AupairCard
import com.example.aupairconnect.presentation.discover.DiscoverViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiscoverScreen(
    onNavigation: NavHostController,
    viewModel: DiscoverViewModel
) {

    val pagerState = rememberPagerState()

    // TODO: Uncomment once we get the datastore & storage repositories
    var aupairList = viewModel.aupairList.value

    val shape =  RoundedCornerShape(16.dp)
    HorizontalPager(pageCount = aupairList.size,
        state = pagerState,
        userScrollEnabled = true,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) { page ->
        AupairCard(aupair = aupairList[page])

    }

}