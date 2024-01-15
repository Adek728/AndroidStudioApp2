package com.example.prm4.photo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.prm4.photo.Model.PathWithSettings
import com.example.prm4.photo.Model.Settings

class PaintView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var color: Int = Color.BLACK
    var size: Float = 30F
    var background: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }
    private val paths = mutableListOf<PathWithSettings>()
    private val defaultPaint = Paint()
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    override fun onDraw(canvas: Canvas){
        drawBackground(canvas)
        drawPath(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        background?.let{
            val rect = Rect(0,0, width, height)
            canvas.drawBitmap(it, null, rect, defaultPaint)
        }
    }

    private fun drawPath(canvas: Canvas){
        paths.forEach {
            paint.apply {
                strokeWidth = it.settings.size
                color = it.settings.color
            }
            canvas.drawPath(it.path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return true


        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                PathWithSettings(
                    settings = Settings(color = color, size = size)
                ).let {
                    it.path.moveTo(event.x, event.y)
                    it.path.lineTo(event.x, event.y)
                    paths.add(it)

                }
            }
            MotionEvent.ACTION_MOVE -> {
                paths.last().path.lineTo(event.x, event.y)
            }
        }

        invalidate()

        return true
    }

    fun generateBitMap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawBackground(canvas)
        drawPath(canvas)

        return bitmap
    }

}