package com.example.prm4.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kierowcy")
data class KierowcaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val imie: String,
    val osiagniecia: String,
    val icon: Int
)
