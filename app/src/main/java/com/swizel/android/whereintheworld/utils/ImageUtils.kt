package com.swizel.android.whereintheworld.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object ImageUtils {

    private val textPaint = Paint().apply {
        isAntiAlias = true
        isFakeBoldText = true
        textSize = 18f
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun drawableToBitmap(drawable: Drawable, numberOverlay: Int): Bitmap {
        val bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        // TODO: We should probably measure the text properly here.
        canvas.drawText(
            numberOverlay.toString(),
            (canvas.width / 2).toFloat(),
            (canvas.height / 2).toFloat(),
            textPaint
        )

        return bitmap
    }

}