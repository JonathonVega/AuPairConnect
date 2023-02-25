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

@Composable
fun AupairCard(
    aupair: Aupair
) {
    val shape =  RoundedCornerShape(24.dp)
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 70.dp, top = 20.dp)
            .fillMaxSize()
            .clip(shape)
            .background(Color.White, shape = shape)
            .border(10.dp, Color.Black, shape).fillMaxSize()

    ) {
        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(painter = painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto4),
                    contentDescription = "Aupair Photo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth())
                Text(text = aupair.name.toString(), fontSize = 20.sp)
                Text(text = aupair.age.toString(), fontSize = 20.sp)
                Image(painter = painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto2),
                    contentDescription = "Aupair Photo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth())
                Image(painter = painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto3),
                    contentDescription = "Aupair Photo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth())
                Image(painter = painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto5),
                    contentDescription = "Aupair Photo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth())
                Image(painter = painterResource(id = com.example.aupairconnect.R.drawable.aupairphoto1),
                    contentDescription = "Aupair Photo",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
@Composable
fun ImageAndText(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String,
    aupair: Aupair
) {
    val shape =  RoundedCornerShape(8.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(White, shape = shape),
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
        )

        Column() {
            Text(
                text = aupair.name.toString(),
                modifier = Modifier.padding(start = 10.dp),
                color = White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = aupair.originLocation.toString(),
                modifier = Modifier.padding(start = 10.dp),
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
