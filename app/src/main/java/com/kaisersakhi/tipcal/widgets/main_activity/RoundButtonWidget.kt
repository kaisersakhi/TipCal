package com.kaisersakhi.tipcal.widgets.main_activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RoundButton(
    modifier: Modifier = Modifier.size(30.dp),
    imageVector: ImageBitmap,
//    imagePainter: ImageBitmap,
    backgroundColor: Color = MaterialTheme.colors.background,
    elevation: Dp = 4.dp,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = { onButtonClick() },
        shape = CircleShape,
        backgroundColor = backgroundColor,
        elevation = elevation
    ) {
        //card body
        Image(
            bitmap = imageVector,
            contentDescription = "",
            modifier = Modifier.padding(8.dp)
        )

    }

}