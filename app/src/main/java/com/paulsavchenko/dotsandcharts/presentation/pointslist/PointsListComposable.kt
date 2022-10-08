package com.paulsavchenko.dotsandcharts.presentation.pointslist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paulsavchenko.dotsandcharts.R
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel
import com.paulsavchenko.dotsandcharts.presentation.ui.theme.DotsAndchartsTheme

@Composable
fun PointsListComposable(
    points: List<PointModel>,
) {
    LazyColumn(

    ) {
        itemsIndexed(items = points) { idx, point ->
            PointItem(pointModel = point)
            if (idx != points.lastIndex) Divider(thickness = 2.dp)
        }
    }

}

@Composable
fun PointItem(
    pointModel: PointModel,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(stringResource(id = R.string.item_coord_x)) }
                append(pointModel.pointX.toString())
            }
        )
        Divider(modifier = Modifier
            .width(2.dp)
            .fillMaxHeight())
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(stringResource(id = R.string.item_coord_y)) }
                append(pointModel.pointY.toString())
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PointsListPreview() {
    DotsAndchartsTheme {
        PointsListComposable(
            points = listOf(
                PointModel(0.2f, 100.4f),
                PointModel(-0.422f, 50.2f),
                PointModel(-98.2f, 1.5f),
                PointModel(9.2f, 55.5f),
                PointModel(5.2f, 9.8f),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PointsListItemPreview() {
    DotsAndchartsTheme {
        PointItem(pointModel = PointModel(0.2f, 100.4f))
    }
}