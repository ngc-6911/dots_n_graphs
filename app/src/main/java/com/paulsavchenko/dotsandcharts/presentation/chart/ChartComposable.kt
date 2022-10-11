package com.paulsavchenko.dotsandcharts.presentation.chart

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paulsavchenko.dotsandcharts.R
import com.paulsavchenko.dotsandcharts.presentation.ui.model.BezierSplineModel
import com.paulsavchenko.dotsandcharts.presentation.ui.model.PointModel
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min


@Composable
fun ChartControlsComposable(
    modifier: Modifier = Modifier,
    onCenterChartCallback: () -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        IconButton(onClick = onCenterChartCallback) {
            Icon(
                painter = painterResource(id = R.drawable.ic_center_chart_24),
                contentDescription = "Center chart",
                tint = MaterialTheme.colors.primary,
            )
        }
    }
}

@Composable
fun ChartComposable(
    modifier: Modifier = Modifier,
    chartState: ChartState,
) {
    val axisBrush = SolidColor(MaterialTheme.colors.onSurface)
    val chartBrush = SolidColor(MaterialTheme.colors.primary)


    val pointsSquareLT by remember(chartState.rawPoints) { mutableStateOf(chartState.bezierUnpacked.leftTop) }
    val pointsMaxOffset by remember(chartState.rawPoints) { mutableStateOf(chartState.bezierUnpacked.maxOffset) }
    val pointsRectSize by remember(chartState.rawPoints) { mutableStateOf(chartState.bezierUnpacked.rectSize) }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    var canvasSize by remember { mutableStateOf(Size.Zero) }

    var scaleFactor by remember { mutableStateOf(1f) }
    val pointScale by remember(canvasSize, pointsMaxOffset, scaleFactor) {
        mutableStateOf( min(canvasSize.width, canvasSize.height) /
                (if (pointsMaxOffset == Offset.Unspecified) 1f else max(pointsMaxOffset.x, pointsMaxOffset.y) * 2f) * scaleFactor
        )
    }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var translationOffset by remember(canvasSize, isLandscape, pointsSquareLT, pointsRectSize, pointScale, dragOffset) {
        mutableStateOf(
            value = makeOffset(
                pointsRect = pointsRectSize,
                canvasSize = canvasSize,
                pointsLeftTop = pointsSquareLT,
                scale = pointScale,
            ) + dragOffset
        )
    }

    Column {
        ChartControlsComposable(
            onCenterChartCallback = {
                dragOffset = Offset.Zero
                translationOffset = makeOffset(
                    pointsRect = pointsRectSize,
                    canvasSize = canvasSize,
                    pointsLeftTop = pointsSquareLT,
                    scale = pointScale,
                )
            },
        )
        Box(modifier = modifier.padding(10.dp),) {
            val path = Path()
            Canvas(
                modifier = modifier
                    .fillMaxSize()
                    .onPlaced {
                        canvasSize = it.boundsInParent().size
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures(panZoomLock = true) { centroid, pan, zoom, _ ->
                            dragOffset += pan
                            scaleFactor *= zoom
                        }
                    }
            ) {
                path.reset()

                path.apply {
                    if (chartState.useBezier) buildBezier(
                        list = chartState.bezierPoints,
                        scale = pointScale,
                    ) else buildRaw(
                        list = chartState.rawPoints,
                        scale = pointScale,
                    )
                }

                clipRect(
                    left = canvasSize.width.takeIf { isLandscape } ?: 0f,
                    top = canvasSize.height.takeIf { isLandscape } ?: .0f,
                    right = 0f.takeIf { isLandscape } ?: canvasSize.width,
                    bottom = 0f.takeIf { isLandscape } ?: canvasSize.height
                ) {
                    translate(left = translationOffset.x, top = translationOffset.y,) {

                        if (chartState.bezierUnpacked.isNotEmpty()) {
                            drawRect(
                                style = Stroke(5f),
                                color = Color.Red,
                                topLeft = pointsSquareLT * pointScale,
                                size = pointsRectSize * pointScale,
                            )
                        }
                        drawAxis(canvasSize, axisBrush, translationOffset)
                        drawPath(brush = chartBrush, path = path, style = Stroke(5f))
                        chartState.rawPoints.forEachIndexed { _, pointModel ->
                            val pX = pointModel.pointX * pointScale
                            val pY = -pointModel.pointY * pointScale
                            drawPoint(chartBrush, Offset(pX, pY))
                        }
                    }
                }
            }
        }
    }
}


private fun Path.buildRaw(list: List<PointModel>, scale: Float) {
    list.forEachIndexed { index, pointModel ->
        val pX = pointModel.pointX * scale
        val pY = -pointModel.pointY * scale
        if (index == 0) moveTo(pX, pY)
        else lineTo(pX, pY)
    }
}

private fun Path.buildBezier(list: List<BezierSplineModel>, scale: Float) {
    list.forEachIndexed { index, bezierModel ->
        val pX = bezierModel.pointA.pointX * scale
        val pY = -bezierModel.pointA.pointY * scale
        if (index == 0) moveTo(pX, pY)
        with(bezierModel) {
            cubicTo(
                x1 = q1.pointX * scale, y1 = -q1.pointY * scale,
                x2 = q2.pointX * scale, y2 = -q2.pointY * scale,
                x3 = pointB.pointX * scale, y3 = -pointB.pointY * scale,
            )
        }
    }
}

private fun makeOffset(
    pointsRect: Size,
    canvasSize: Size,
    pointsLeftTop: Offset,
    scale: Float,
): Offset = if (pointsRect.isSpecified) canvasSize.center - ((pointsLeftTop + pointsRect.center) * scale)
            else Offset.Zero


private val Collection<PointModel>.leftTop: Offset get() {
    return if (isEmpty()) Offset.Unspecified else Offset(x = minOf { it.pointX }, y = minOf { -(it.pointY) })
}

private val Collection<PointModel>.rightBottom: Offset get() {
    return if (isEmpty()) Offset.Unspecified else Offset(x = maxOf { it.pointX }, y = maxOf { -(it.pointY) })
}

private val Collection<PointModel>.maxOffset: Offset get() {
    return if (isEmpty()) Offset.Unspecified else Offset(
        x = maxOf { it.pointX.absoluteValue },
        y = maxOf { it.pointY.absoluteValue },
    )
}

private val Collection<PointModel>.rectSize: Size get() {
    return if (isEmpty()) Size.Unspecified else Size(
        width = rightBottom.x - leftTop.x,
        height = rightBottom.y - leftTop.y,
    )
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

private fun DrawScope.drawAxis(canvasSize: Size, axisBrush: Brush, translation: Offset) {
    val halfSizeX = canvasSize.width + translation.x.absoluteValue
    val halfSizeY = canvasSize.height + translation.y.absoluteValue
    drawLine(
        brush = axisBrush,
        strokeWidth = 5f,
        start = Offset(
            x = -halfSizeX,
            y = 0f,
        ),
        end = Offset(
            x = halfSizeX,
            y = 0f,
        ),
    )
    drawLine(
        brush = axisBrush,
        strokeWidth = 5f,
        start = Offset(
            x = 0f,
            y = -halfSizeY,
        ),
        end = Offset(
            x = 0f,
            y = halfSizeY,
        ),
    )
}