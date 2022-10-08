package com.paulsavchenko.dotsandcharts.presentation.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel
import com.paulsavchenko.dotsandcharts.presentation.ui.theme.DotsAndchartsTheme
import kotlin.math.absoluteValue

@Composable
fun ChartComposable(
    modifier: Modifier = Modifier,
    points: List<PointModel> = emptyList()
) {
    val axisBrush = SolidColor(MaterialTheme.colors.secondary)
    val chartBrush = SolidColor(MaterialTheme.colors.primary)
    val pointsSorted by remember(points) { mutableStateOf(points) }

    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = modifier.padding(10.dp),) {
        val path = Path()
        Canvas(
            modifier = modifier
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    }
                }
        ) {
            val pointScale =
                size.width / (if (pointsSorted.isEmpty()) 1f else with(pointsSorted) { maxBy { it.pointX.absoluteValue }.pointX.absoluteValue * 2f })
            path.reset()
            path.apply {
                pointsSorted.forEachIndexed { index, pointModel ->
                    val pX = pointModel.pointX * pointScale + center.x
                    val pY =  center.y - pointModel.pointY * pointScale

                    if (index == 0) moveTo(pX, pY)
                    else lineTo(pX, pY)
                }
            }

            clipRect(left = 0f, top = 0f, right = size.width, bottom = size.height) {
                drawAxis(axisBrush)
                drawPath(brush = chartBrush, path = path, style = Stroke(5f))
                pointsSorted.forEachIndexed { index, pointModel ->
                    val pX = pointModel.pointX * pointScale + center.x
                    val pY = center.y - pointModel.pointY * pointScale
                    drawPoint(chartBrush, Offset(pX, pY))
                }
            }
        }
    }
}

private fun DrawScope.drawPoint(brush: Brush, center: Offset, radius: Dp = 5.dp) {
    val pxRadius = radius.toPx()
    drawCircle(
        brush = brush,
        center = center,
        radius = pxRadius,
        style = Stroke(width = pxRadius / 5f),
    )
    drawCircle(
        brush = brush,
        center = center,
        radius = pxRadius - (pxRadius / 3f),
        style = Fill,
    )
}

private fun DrawScope.drawAxis(axisBrush: Brush) {
    drawLine(
        brush = axisBrush,
        start = Offset(0f, center.y),
        end = Offset(size.width, center.y),
    )
    drawLine(
        brush = axisBrush,
        start = Offset(center.x, 0f),
        end = Offset(center.x, size.height),
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChartPreviewEmpty() {
    DotsAndchartsTheme {
        ChartComposable(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChartPreviewData() {
    DotsAndchartsTheme {
        ChartComposable(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f),
            points = listOf(
                PointModel(0f, 0f),
                PointModel(-50f, 50f),
                PointModel(-240f, 20f),
                PointModel(100f, 50f),
                PointModel(50f, -150f),
            )
        )
    }
}