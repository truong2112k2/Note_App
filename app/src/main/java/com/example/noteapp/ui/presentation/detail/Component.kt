package com.example.noteapp.ui.presentation.detail

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.noteapp.R
import com.example.noteapp.common.Constants
import java.io.File

@Composable
fun LoadImageFromFile(context: Context, fileName: String) {
    val file = File(File(context.filesDir, "image"), fileName)

    if (file.exists()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(file),
                contentDescription = null,
                contentScale = ContentScale.Crop, // 👉 cắt ảnh vừa khung, đẹp
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun DisplayEmptyListMessage(navController: NavController) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_language), // thay bằng icon của bạn
                contentDescription = "Note Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.width(12.dp)) // khoảng cách giữa image và text

            Text(
                text = "Create your first note!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary

            )
        }

        Text(
            stringResource(R.string.welcome_message),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.note_feature_description),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.no_notes_yet),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.create_note_now),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.inspiration_message),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape = RoundedCornerShape(16.dp)) // 👈 thêm bóng đổ ở đây
                .background(color = MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
                .clickable {
                    navController.navigate(Constants.ADD_NOTE_ROUTE)
                },
        ) {


            Text(
                text = "Create your first note!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(1f)) // khoảng cách giữa image và text

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight, // thay bằng icon của bạn
                contentDescription = "Note Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

}

//@Composable
//fun FadeInImageOnLoad( context: Context,imageFile: String) {
//    var visible by remember { mutableStateOf(false) }
//
//    // Trigger fade-in on first composition
//    LaunchedEffect(Unit) {
//        visible = true
//    }
//
//    val alpha by animateFloatAsState(
//        targetValue = if (visible) 1f else 0f,
//        animationSpec = tween(durationMillis = 1000), // 1s fade-in
//        label = "fadeInAlpha"
//    )
//
//    Box(modifier = Modifier.alpha(alpha)) {
//        LoadImageFromFile(context, imageFile)
//    }
//}