package com.example.projetandroid

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "info",
    foreignKeys= arrayOf(
        ForeignKey(
            entity = Flux::class,
            parentColumns = ["url"],
            childColumns = ["fluxUrl"],
            deferred = true,
            onDelete = ForeignKey.CASCADE
        )),indices = arrayOf(Index(value = ["TITLE","DESCRIPTION","LINK"],unique = true))
)

data class Info (
    @PrimaryKey(autoGenerate = true)
    var id : Int =0,
    var TITLE : String,
    var DESCRIPTION : String,
    var LINK : String,
    var NOUVEAU : Boolean,
    var fluxUrl : String,
    var check : Boolean = false


)