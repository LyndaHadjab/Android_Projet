package com.example.projetandroid

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.projetandroid.Flux

@Dao
interface MyDao {

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertFlux(vararg flux: Flux)

    @Query("SELECT * FROM Flux WHERE url = :u")
    fun select(u:String): LiveData<List<Flux>>

    @Query("SELECT * FROM Flux")
    fun selectAllFlux():LiveData<List<Flux>>

    @Update
    fun updateFlux(vararg  flux:Flux)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInfo(vararg  info:Info)

    @Query("SELECT * FROM info")
    fun selectAllInfo():LiveData<List<Info>>

    @Query("UPDATE info set NOUVEAU=:newvalue")
    fun updateInfo(vararg newvalue:Boolean)

    @Query("SELECT * FROM info where fluxUrl  LIKE:value")
    fun  selectFluxUrl(value:String):LiveData<List<Info>>

    @Delete
    fun deleteInfo(info:Info)

    @Query ("SELECT * FROM info WHERE NOUVEAU =:value")
    fun selectNouveau(vararg value:Boolean):LiveData<List<Info>>

    @Query("SELECT * FROM info WHERE TITLE LIKE:chaine OR DESCRIPTION like :chaine")
    fun selectFromKey(vararg  chaine:String):LiveData<List<Info>>




}