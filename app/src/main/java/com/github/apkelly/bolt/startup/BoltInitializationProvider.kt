package com.github.apkelly.bolt.startup

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class BoltInitializationProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        val ctx = context
        if (ctx != null) {
            BoltAppInitializer.getInstance(ctx).discoverAndInitialize()
        } else {
            throw BoltException("Context cannot be null")
        }

        return true
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        return 0
    }

    override fun getType(
        uri: Uri,
    ): String? {
        return null
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        return null
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        return 0
    }
}
