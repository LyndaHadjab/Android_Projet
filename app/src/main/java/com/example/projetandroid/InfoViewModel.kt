package com.example.projetandroid

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class InfoViewModel(application: Application):AndroidViewModel(application) {

    val db:DataBase by lazy { DataBase.getDatabase(application) }
    fun insertInfo(info:Info)
    {
        Thread{
            db.rss().insertInfo(info)
        }.start()
    }
    fun  selectAllInfo():LiveData<List<Info>>
    {
        return  db.rss().selectAllInfo()
    }
    fun updateInfo(value:Boolean)
    {
        Thread{
            db.rss().updateInfo(value)
        }.start()
    }

    fun deleteInfo(info: Info)
    {
        Thread{
            db.rss().deleteInfo(info)
        }.start()
    }
    fun selectNouveau(value: Boolean):LiveData<List<Info>>
    {
        return db.rss().selectNouveau(value)
    }
    fun selectFromKey(chaine:String):LiveData<List<Info>>
    {
        return  db.rss().selectFromKey(chaine)
    }
    fun selectFluxUrl(url:String):LiveData<List<Info>>
    {
        return  db.rss().selectFluxUrl(url)

    }
}