package com.example.aupairconnect.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aupairconnect.domain.model.Aupair
import com.example.aupairconnect.presentation.ui.theme.ACTheme

@Composable
fun AupairCard(
    aupair: Aupair
) {
    val shape =  RoundedCornerShape(24.dp)
    Box(modifier = Modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center) {
        Text(
            text = aupair.name.toString(),
            modifier = Modifier
                .padding(horizontal = 10.dp),
            color = ACTheme,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 70.dp, top = 50.dp)
            .fillMaxSize()
//            .requiredHeight(600.dp)
            .clip(shape)
            .background(Color.White, shape = shape)
//            .border(5.dp, Color.Black, shape)

    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .verticalScroll(rememberScrollState())
        ) {
//            ImageAndText(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto4), "First Photo", aupair)
            RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto4))

            Box(modifier = Modifier
                .border(0.dp, Color.White, shape)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(all = 10.dp)) {
                Column() {
//                    Text(text = aupair.name.toString(), fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(text = aupair.originLocation.toString(), fontSize = 25.sp)
                    Text(text = aupair.age.toString(), fontSize = 25.sp)
                    if(aupair.isExAupair == true){
                        Text(text = "Ex Au pair", fontSize = 25.sp)
                    }
                }
            }

            RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto2))

            Box(modifier = Modifier
                .border(0.dp, Color.White, shape)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .background(Color.White)
                .padding(all = 10.dp)) {
                Column() {
                    Text(text = aupair.bio.toString(), fontSize = 20.sp)
                }
            }

            RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto3))
            RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto5))
            RoundedImage(painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto1))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Send ${aupair.name.toString()} a Message")
            }
        }
    }
}

@Composable
fun RoundedImage(painter: Painter){
    val shape =  RoundedCornerShape(24.dp)
    Image(painter = painter,
        contentDescription = "Aupair Photo",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White, shape)
            .border(0.dp, Color.White, shape))
}

@Composable
fun ImageAndText(
    painter: Painter,
    contentDescription: String,
    aupair: Aupair
) {
    val shape =  RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, shape = shape)
            .clip(shape),
        contentAlignment = Alignment.TopStart
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
        )

        Box(modifier = Modifier
            .padding(start = 10.dp, bottom = 10.dp)
            .background(ACTheme, shape = shape)) {
            Text(
                text = aupair.name.toString(),
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                color = White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}