package com.example.aupairconnect.presentation.discover

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.aupairconnect.R
import com.example.aupairconnect.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AupairCardScreen(
    onNavigation: NavHostController,
    viewModel: DiscoverViewModel
) {

    val scrollState = rememberScrollState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Box(modifier = Modifier.fillMaxWidth()) {


        Column(modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {

            // Name
            Box(){
                Text("${viewModel.selectedAupair.name}",
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(top = 24.dp))
            }


            // Main Photo
            if (viewModel.selectedAupair.profileImages.size >= 1){
                setProfileImage(viewModel.selectedAupair.profileImages[0])
            }

            //Bio
            if (!viewModel.selectedAupair.bio.isNullOrBlank()) {
                Box(modifier = Modifier
                    .background(color = Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    ){

                    Text("${viewModel.selectedAupair.bio} ")
                }

            } else {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                    Text("${viewModel.selectedAupair.name} does not have a bio yet.",
                        modifier = Modifier.background(color = Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp))
                }
            }

            //Other Photos
            if (viewModel.selectedAupair.profileImages.size >= 2){
                setProfileImage(viewModel.selectedAupair.profileImages[1])

            }
            if (viewModel.selectedAupair.profileImages.size >= 3){
                setProfileImage(viewModel.selectedAupair.profileImages[2])
            }
            if (viewModel.selectedAupair.profileImages.size >= 4){
                setProfileImage(viewModel.selectedAupair.profileImages[3])
            }
            if (viewModel.selectedAupair.profileImages.size >= 5){
                setProfileImage(viewModel.selectedAupair.profileImages[4])
            }
            if (viewModel.selectedAupair.profileImages.size == 6){
                setProfileImage(viewModel.selectedAupair.profileImages[5])
            }
        }

        Box(modifier = Modifier.fillMaxWidth()
            .background(Color.White)
            .align(alignment = Alignment.TopCenter)
            .height(62.dp)){
            Text("${viewModel.selectedAupair.name}",
                modifier = Modifier.align(alignment = Alignment.Center),
                fontSize = 24.sp,)
        }

        //Chat Button
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                bitmap = viewModel.selectedAupair.profileImages[0].asImageBitmap(),
//                painter = painterResource(id = viewModel.selectedAupair.profileImages[0]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(72.dp)
                    .padding(bottom = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
fun setProfileImage(image: Bitmap){

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)){
        androidx.compose.foundation.Image(bitmap = image.asImageBitmap(),
            contentDescription = "cool",
            modifier = Modifier
                .size(screenWidth - 32.dp)
                .border(2.dp, colorResource(id = R.color.ACTheme), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop)
    }
}


