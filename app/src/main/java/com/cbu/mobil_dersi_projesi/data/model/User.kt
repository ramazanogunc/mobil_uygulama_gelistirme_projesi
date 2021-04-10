package com.cbu.mobil_dersi_projesi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    val nameSurname: String,
    val email: String,
    val password: String
){
    @PrimaryKey(autoGenerate = true) var userId: Int = 0
}