package com.cbu.mobil_dersi_projesi.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    var userId: Int = 0
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var mekanId: Int = 0
}
