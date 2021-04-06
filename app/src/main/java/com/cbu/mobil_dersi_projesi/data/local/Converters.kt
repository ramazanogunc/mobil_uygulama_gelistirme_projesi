package com.cbu.mobil_dersi_projesi.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toBitmap(string: String?): Bitmap? {
            if (string == null) return null
            return try {
                val encodeByte: ByteArray = Base64.decode(string, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
                bitmap
            } catch (e: Exception) {
                e.message
                null
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromBitmap(bmp: Bitmap?): String? {
            if (bmp == null) return null
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            val temp: String = Base64.encodeToString(b, Base64.DEFAULT)
            return if (temp == null) {
                null
            } else temp
        }
    }
}