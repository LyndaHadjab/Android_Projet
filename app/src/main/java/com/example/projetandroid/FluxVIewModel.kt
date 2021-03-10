package com.example.projetandroid

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class FluxVIewModel(application:Application):AndroidViewModel(application) {

    val db:DataBase by lazy { DataBase.getDatabase(application)
    }

    fun insertFlux(flux:Flux)
    {
        Thread{
            db.rss().insertFlux(flux)
        }.start()

    }
    fun selectelement(u:String):LiveData<List<Flux>>
    {
        val maliste : LiveData<List<Flux>> by lazy { db.rss().select(u) }

        return maliste
    }
    fun selectAllFlux():LiveData<List<Flux>>
    {
        return  db.rss().selectAllFlux()
    }
    fun updateFlux(flux: Flux)
    {
        Thread{
            db.rss().updateFlux(flux)
        }.start()
    }

}
