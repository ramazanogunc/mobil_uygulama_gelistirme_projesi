package com.cbu.mobil_dersi_projesi.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
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
    var img1: Bitmap?,
    var img2: Bitmap?,
    var img3: Bitmap?,
    var userId: Int = 0
){
    @PrimaryKey(autoGenerate = true) var mekanId: Int = 0
}
