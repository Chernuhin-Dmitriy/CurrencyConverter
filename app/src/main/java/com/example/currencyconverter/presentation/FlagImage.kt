package com.example.currencyconverter.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.currencyconverter.utils.CurrencyUtils

@Composable
fun FlagImage(
    currencyCode: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val flagUrl = CurrencyUtils.getCurrencyFlagUrl(currencyCode)

    Box(
        modifier = modifier
            .size(size, 50.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(size / 2.5f)
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(flagUrl)
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
                .build(),
            contentDescription = "Flag of $currencyCode",
            modifier = Modifier
                .size(size, 50.dp)
                .clip(RoundedCornerShape(size / 6.5f)),
            contentScale = ContentScale.Crop,
        )
    }
}