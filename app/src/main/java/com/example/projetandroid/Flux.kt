package com.example.projetandroid

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="flux",primaryKeys = arrayOf("url"))
data class Flux(
    var url : String,
    var source : String,
    var tag : String,
    var checked : Boolean = false

)
