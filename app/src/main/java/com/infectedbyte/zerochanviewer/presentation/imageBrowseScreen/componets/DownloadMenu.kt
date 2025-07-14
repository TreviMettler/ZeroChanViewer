package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.infectedbyte.zerochanviewer.R
import com.infectedbyte.zerochanviewer.domain.model.DownloadInfo
import com.infectedbyte.zerochanviewer.domain.model.DownloadStatus
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.ImageBrowseViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun DownloadMenu(
    modifier: Modifier,
    downloadInfo: DownloadInfo,
    viewModel: ImageBrowseViewModel
) {
    DropdownMenuItem(
        text = {
            Column {
                Card(modifier = Modifier.padding(4.dp)) {
                    Column(
                        modifier.padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (downloadInfo.status == DownloadStatus.IN_PROGRESS) Icon(
                                painterResource(R.drawable.download_arrow_cool),
                                contentDescription = null
                            )
                            if (downloadInfo.status == DownloadStatus.FAILED) Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.Red
                            )
                            if (downloadInfo.status == DownloadStatus.COMPLETED) Icon(
                                painterResource(R.drawable.download_done),
                                contentDescription = null,
                                tint = Color.Green
                            )
                            Text(
                                "Image: ${downloadInfo.imageName}",
                                modifier = Modifier.padding(4.dp)
                            )

                        }
                        if (downloadInfo.status == DownloadStatus.IN_PROGRESS) {
                            Text(
                                "Progress: ${downloadInfo.progress}%",
                                modifier = Modifier.padding(4.dp)
                            )
                            LinearProgressIndicator(
                                progress = { downloadInfo.progress / 100f },
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        },
        onClick = {
            if (downloadInfo.status != DownloadStatus.IN_PROGRESS) {
                viewModel.onEvent(BrowserEvent.RemoveDownloadItem(downloadInfo.imageId))
            }
        },
    )
}
