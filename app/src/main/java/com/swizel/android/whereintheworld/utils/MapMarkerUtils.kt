package com.swizel.android.whereintheworld.utils

import android.content.Context
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

internal fun bitmapDescriptorFromVectorDrawable(
    context: Context,
    @DrawableRes drawableId: Int,
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, drawableId) ?: return null
    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return runCatching {
        MapsInitializer.initialize(context.applicationContext)
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }.getOrNull()
}
