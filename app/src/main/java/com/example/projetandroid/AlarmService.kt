package com.example.projetandroid

import android.app.Application
import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.lifecycle.*
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.InputStream

class AlarmService : Service() {
    private  var idDownload:MutableList<Long> = mutableListOf()
    private var urlDownload:MutableList<String> = mutableListOf()
    private lateinit var dm:DownloadManager
    private lateinit  var infovViewModel:InfoViewModel

    var receiver=object :BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            val reference:Long?=intent?.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID,-1
            )
            if (reference==null || reference !in idDownload)
                return
            else
            {

                /*infovViewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
                 val index=idDownload.indexOf(reference)
                 val myurl:String=urlDownload.get(index)
                 val  pdesc: ParcelFileDescriptor =dm.openDownloadedFile(reference)
                 val desc: FileDescriptor =pdesc.fileDescriptor
                 val fileInputStream : InputStream = FileInputStream(desc)
                 val intent = Intent(this@AlarmService,DownloadServices::class.java)
                 if (context != null) {
                    Intent(context,DownloadServices::class.java).apply {
                        DownloadServices.enqueueWork(context,fileInputStream,infovViewModel,myurl,intent)
                    }
                }*/
            }

        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return  null
    }

    override fun onCreate() {
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val atelecharger  = intent?.getStringArrayListExtra("element")
        dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        for (i in atelecharger!!)
        {
            val uri = Uri.parse(i)
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"essaye.xml")
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI )
            request.setTitle("Download")
            request.allowScanningByMediaScanner()
            val id:Long = dm.enqueue(request)
            idDownload.add(id)
            urlDownload.add(i)
        }
        return  START_NOT_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
    }



}