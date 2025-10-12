package com.swizel.android.whereintheworld.extensions

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import java.io.Serializable

fun <V> Map<String, V>.toBundle(
    bundle: Bundle = Bundle(),
): Bundle = bundle.apply {
    forEach {
        val k = it.key
        when (val v = it.value) {
            is IBinder -> putBinder(k, v)
            is Bundle -> putBundle(k, v)
            is Byte -> putByte(k, v)
            is ByteArray -> putByteArray(k, v)
            is Char -> putChar(k, v)
            is CharArray -> putCharArray(k, v)
            is CharSequence -> putCharSequence(k, v)
            is Float -> putFloat(k, v)
            is FloatArray -> putFloatArray(k, v)
            is Short -> putShort(k, v)
            is ShortArray -> putShortArray(k, v)
            is Size -> putSize(k, v)
            is SizeF -> putSizeF(k, v)
            is Parcelable -> putParcelable(k, v)
            is Serializable -> putSerializable(k, v)

            else -> throw IllegalArgumentException("$v is of a type that is not currently supported")
        }
    }
}