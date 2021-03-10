package com.example.projetandroid

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


class AddFlux : Fragment()  {

    private lateinit var enregistrer : Button
    private lateinit var source : EditText
    private lateinit var tag : EditText
    private lateinit var url : EditText
    private lateinit var myviewmodel : FluxVIewModel
    private var sourcetext: String = ""
    private var tagtext: String = ""
    private var urltext: String = ""
    private var src: String =""
    private var tg: String = ""
    private var ur: String = ""
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_add_flux, container, false)

        enregistrer = v.findViewById(R.id.enregistrer)
        source = v.findViewById(R.id.sourceedit)
        tag = v.findViewById(R.id.tagedit)
        url = v.findViewById(R.id.urledit)

        myviewmodel = ViewModelProvider(this)
            .get(FluxVIewModel::class.java)

        enregistrer.setOnClickListener{
             sourcetext = (source.getText().toString()).trim()
             tagtext = (tag.getText().toString()).trim()
             urltext = (url.getText().toString()).trim()
            if(sourcetext!="" && tagtext!="" && urltext!="")
            {
                if(URLUtil.isValidUrl(Uri.parse(urltext).toString()))
                {
                    myviewmodel.insertFlux(Flux(url = urltext, source = sourcetext, tag = tagtext))
                    Toast.makeText(
                        activity,
                        "Flux ajouté dans la base avec succée",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else
                {
                    Toast.makeText(activity, "URL saisée n'est pas valide ", Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(activity, "Tous Les Champs sont obligatoires", Toast.LENGTH_LONG).show()
            }
            source.setText("")
            url.setText("")
            tag.setText("")
        }

        if (savedInstanceState != null)
        {
            val src = savedInstanceState.getString("source")
            val tg = savedInstanceState.getString("tag")
            val ul = savedInstanceState.getString("url")
            source.setText(src)
            tag.setText(tg)
            url.setText(ul)

        }

        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        src = source.getText().toString()
        ur = url.getText().toString()
        tg = tag.getText().toString()
        outState.putString("source",src)
        outState.putString("url",ur)
        outState.putString("tag",tg)

    }



}