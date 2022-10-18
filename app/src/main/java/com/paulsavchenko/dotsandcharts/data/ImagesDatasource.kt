package com.paulsavchenko.dotsandcharts.data

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.*
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.paulsavchenko.dotsandcharts.domain.dto.PointDto
import java.util.*
import kotlin.math.*

interface ImagesDatasource {

    suspend fun storeChart(
        points: List<PointDto>, pointColor: Int, lineColor: Int
    )

    class Impl(
        private val contentResolver: ContentResolver,
    ): ImagesDatasource {

        override suspend fun storeChart(points: List<PointDto>, pointColor: Int, lineColor: Int) {
            val bitmap = generateBitmap(points, pointColor = pointColor, lineColor = lineColor,)
            val gallery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            contentResolver.insert(
                gallery,
                ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "chart_${Calendar.getInstance().timeInMillis}.png")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.WIDTH, bitmap.width)
                    put(MediaStore.Images.Media.HEIGHT, bitmap.height)
                }
            )?.also { uri ->
                runCatching {
                    contentResolver.openOutputStream(uri).use {
                        bitmap.compress(
                            Bitmap.CompressFormat.PNG,
                            100,
                            it,
                        )
                    }.also {
                        println("QWEQWEWEWE $it")
                    }
                }.onFailure {
                    Log.e("IMAGE_DATASOURCE", it.stackTraceToString())
                }
            }
        }

        private fun cosineInterpolate(
            y1: Float,
            y2: Float,
            mu: Float,
        ): Float {
            val mu2: Float = (1f - cos(mu * PI.toFloat())) / 2f
            return y1 * (1f - mu2) + y2 * mu2
        }

        private fun Canvas.drawCirclePoint(
            point: PointDto,
            pointStroke: Paint,
            pointFilled: Paint,
            radius: Float = 5f,
            scale: Float,
        ) {
            drawCircle(
                point.pointX * scale,
                -point.pointY * scale,
                radius,
                pointStroke
            )
            drawCircle(
                point.pointX * scale,
                -point.pointY * scale,
                radius - radius / 3f,
                pointFilled
            )
        }

        private fun Path.buildCosine(
            list: List<PointDto>,
            scale: Float,
        ) {
            list
                .zipWithNext()
                .forEachIndexed { index, (p0, p1) ->
                    val pX = p0.pointX * scale
                    val pY = -p0.pointY * scale
                    if (index == 0) moveTo(pX, pY)
                    else {
                        var mu = 0f
                        val pX1 = p1.pointX * scale
                        val pY1 = -p1.pointY * scale
                        val dx = pX1 - pX
                        repeat(100) {
                            lineTo(
                                pX + dx * mu,
                                cosineInterpolate(
                                    y1 = pY,
                                    y2 = pY1,
                                    mu = mu,
                                ),
                            )
                            mu += 0.01f
                        }
                    }
                }
        }

        private fun generateBitmap(
            points: List<PointDto>,
            pointColor: Int,
            lineColor: Int,
        ): Bitmap {
            val size = RectF(0f, 0f, 1000f, 1000f)
            val pointsRect = RectF(
                points.minOf { it.pointX },
                points.maxOf { -(it.pointY) },
                points.maxOf { it.pointX },
                points.minOf { -(it.pointY) },
            )
            val scale = min(size.width(), size.height()) / ((max(pointsRect.width(), pointsRect.height()) * 2f))

            val translateX = (size.width() / 2f) - pointsRect.centerX() * scale
            val translateY = (size.height() / 2f) - pointsRect.centerY() * scale

            val bitmap = Bitmap.createBitmap(
                size.width().toInt(),
                size.height().toInt(),
                Bitmap.Config.ARGB_8888,
            ).apply {
                setHasAlpha(false)
            }
            val pointStroke = Paint().apply {
                color = pointColor
                strokeWidth = 2f
                style = Paint.Style.STROKE
            }
            val pointFilled = Paint().apply {
                color = pointColor
                style = Paint.Style.FILL
            }

            val linePaint = Paint().apply {
                color = lineColor
                strokeWidth = 3f
                style = Paint.Style.STROKE
            }

            val chart = Path().apply { buildCosine(points, scale) }


            Canvas(bitmap).apply {
                drawRect(size, Paint().apply { color = Color.WHITE })
                translate(translateX, translateY)

                // add axis
                val halfSizeX = size.width() + translateX.absoluteValue
                val halfSizeY = size.height() + translateY.absoluteValue
                Paint().apply { color = Color.BLACK }.also {
                    drawLine(
                        -halfSizeX,
                        0f,
                        halfSizeX,
                        0f,
                        it,
                    )
                    drawLine(
                        0f,
                        -halfSizeY,
                        0f,
                        halfSizeY,
                        it,
                    )
                }
                // add chart
                drawPath(chart, linePaint)

                // add points
                points.forEach {
                    drawCirclePoint(
                        point = it,
                        pointStroke = pointStroke,
                        pointFilled = pointFilled,
                        scale = scale,
                    )
                }
            }

            return bitmap
        }
    }
}