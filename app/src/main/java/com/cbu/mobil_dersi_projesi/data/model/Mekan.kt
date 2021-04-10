package com.cbu.mobil_dersi_projesi.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mekan",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Mekan(
    var name: String,
    var description: String,
    var latitude: Double,
    var longitude: Double,
    // img 1,2,3 burada olmaması gerektiğini biliyorum.
    // Vakit kısıtlı olduğundan uğraşmamak için bu tablo içerisinde tutuyorum.
    // Normal şartlarda veritabanının normalize edilip resim için farklı tablo oluşturup foreign key ile bağlanması gerekli.
    var _img1: Bitmap? = null,
    var _img2: Bitmap? = null,
    var _img3: Bitmap? = null,
    var userId: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var mekanId: Int = 0
}
