package com.example.projetandroid

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.*
import android.content.Context.ALARM_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


class DownloadFlux : Fragment() {
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : FluxAdapter
    private lateinit var viewModel : FluxVIewModel
    private lateinit var telecharger : Button
    private lateinit var chose : Button
    private  var flux : List<Flux> = listOf()
    private lateinit var dm : DownloadManager
    private  var idDownload : MutableList<Long> = mutableListOf()
    private  var urlDownload : MutableList<String> = mutableListOf()
    private  lateinit var input : String
    private lateinit var alarmView : View
    private lateinit var button : Button
    private  lateinit var heure : EditText
    private lateinit var minute : EditText
    private lateinit var seconde : EditText
    private  var k : Int = 0
    private lateinit var infovViewModel : InfoViewModel
    private lateinit var receiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_download_flux, container, false)
        recycler = v.findViewById(R.id.recyclerView)
        telecharger = v.findViewById(R.id.telechargement)
        chose = v.findViewById(R.id.chose)
        dm = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        viewModel = ViewModelProvider(this).get(FluxVIewModel::class.java)
        adapter = FluxAdapter(flux)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter
        viewModel.selectAllFlux().observe(viewLifecycleOwner, Observer { adapter.setListFlux(it) })
         infovViewModel  = ViewModelProvider(this).get(InfoViewModel::class.java)

         receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val reference:Long? = intent?.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID,-1
                )
                if(reference==null || reference !in idDownload) {
                    return
                }
                else{
                    val index = idDownload.indexOf(reference)
                    val myurl:String = urlDownload.get(index)
                    val  pdesc: ParcelFileDescriptor = dm.openDownloadedFile(reference)
                    val desc: FileDescriptor = pdesc.fileDescriptor
                    val fileInputStream : InputStream = FileInputStream(desc)
                    val intent = Intent(activity,DownloadServices::class.java)
                    if (context != null) {
                        Intent(context,DownloadServices::class.java).apply {
                            DownloadServices.enqueueWork(context,fileInputStream,infovViewModel,myurl,intent)
                        }

                    }

                }
            }
        }
       val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)


        activity!!.registerReceiver(receiver, intentFilter)
        chose.setOnClickListener{
            val content = adapter.getListe()
            var atelecharger:ArrayList<String> = ArrayList()
            for (i in content)
            {
                if (i.checked)
                {
                    atelecharger.add(i.url)
                }
            }
            alarmView = layoutInflater.inflate(R.layout.time_of_download,null,false)
            AlertDialog.Builder(requireContext())
                .setMessage("Veuillez choisir une durée  ")
                .setTitle("Time")
                .setView(alarmView)
                .setNeutralButton("Cancel"){dialog,_->
                    dialog.cancel()
                }
                .show()
            button =  alarmView.findViewById(R.id.save)
            heure = alarmView.findViewById(R.id.heure)
            minute = alarmView.findViewById(R.id.min)
            seconde = alarmView.findViewById(R.id.sec)

            button.setOnClickListener{
                var heur = Integer.parseInt(heure.getText().toString())
                var min = Integer.parseInt(minute.getText().toString())
                var sec = Integer.parseInt(seconde.getText().toString())
                val calender = Calendar.getInstance()
                calender.add(Calendar.HOUR_OF_DAY,heur)
                calender.add(Calendar.MINUTE,min)
                calender.add(Calendar.SECOND,sec)
                val alarmManager:AlarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
                val intent = Intent(requireContext(),AlarmService::class.java)
                intent.putStringArrayListExtra("element",atelecharger)
                val pendingIntent = PendingIntent.getService(requireContext(),0,intent,0)
                alarmManager.set(AlarmManager.RTC_WAKEUP,calender.timeInMillis,pendingIntent)
            }
        }
        telecharger.setOnClickListener{

            val atelecharger = recupereAtelecharger()
            AlertDialog.Builder(requireContext())
                .setMessage("Veuillez choisir le mode de téléchargemnt !!")
                .setTitle("Type téléchargement ")
                .setCancelable(true)
                .setPositiveButton("Via Wifi "){
                        dialog:DialogInterface,t:Int->
                    k = 0
                    modeDownload(k,atelecharger)
                    dialog.dismiss()
                }
                .setNegativeButton("Via mobile "){_,_->
                    k = 1
                    modeDownload(k,atelecharger)
                }
                .show()
        }

        return  v
    }

    fun modeDownload(k:Int, atelecharger:MutableList<Flux>)
    {
        for (i in atelecharger)
        {
            val uri = Uri.parse(i.url)
            input = i.url
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"essaye.xml")
            if (k==0)
            {
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI )
            }
            if (k==1)
            {
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE )
            }
            request.setTitle("Download")
            request.allowScanningByMediaScanner()
            val id:Long = dm.enqueue(request)
            idDownload.add(id) //lancement du téléchargement
            urlDownload.add(i.url)
        }
    }

    fun recupereAtelecharger():MutableList<Flux>
    {
        val maliste = adapter.getListe()
        val atelecharger : MutableList<Flux> = mutableListOf()
        for (i in maliste)
        {
            if (i.checked)
                atelecharger.add(i)
        }
        return  atelecharger
    }





}