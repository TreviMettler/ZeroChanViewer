package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.infectedbyte.zerochanviewer.R
import com.infectedbyte.zerochanviewer.domain.model.ZeroImage
import com.valentinilk.shimmer.shimmer

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ZeroImageItem(
    modifier: Modifier = Modifier,
    zeroImage: ZeroImage,
    onClick: (ZeroImage) -> Unit,
    onDownloadClick: () -> Unit
) {
    var imageBorder by remember { mutableStateOf(BorderStroke(1.dp, color = Color.Gray)) }
    if (zeroImage.tags.contains("Ecchi")){
        imageBorder = BorderStroke(1.dp, color = Color.Magenta)
        Log.i("iByte", "found ecchi tag")
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .border(imageBorder, shape = RoundedCornerShape(8.dp)),
        onClick = { onClick(zeroImage) },
        shape = RoundedCornerShape(8.dp)
    ) {


            SubcomposeAsyncImage(
                model = zeroImage.thumbnail,
                contentDescription = zeroImage.tag,
                Modifier
                    .fillMaxSize()
                    .aspectRatio(zeroImage.width.toFloat() / zeroImage.height.toFloat()),
                loading = {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().shimmer().background(Color.Gray)
                        ) {
                            Text("")
                        }
                    }
                }
            )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onDownloadClick()
                }
            ) {
                Icon(painterResource(R.drawable.arrow_circle_down), contentDescription = null)
            }
            Text(
                text = zeroImage.tag,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(8.dp)


            )
        }


    }

}