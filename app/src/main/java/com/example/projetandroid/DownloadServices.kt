package com.example.projetandroid

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.sax.StartElementListener
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import java.security.AccessController.getContext
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.random.Random

class DownloadServices(): JobIntentService() {


    companion object {

        fun enqueueWork(context: Context, fileInputStream: InputStream, infoViewModel: InfoViewModel, url: String, work: Intent) {
            var itemHashMap: HashMap<String, String>
            var itemHashMapList: ArrayList<HashMap<String, String>> = ArrayList()
            val builderFactory = DocumentBuilderFactory.newInstance()
            val docBuilder = builderFactory.newDocumentBuilder()
            val doc = docBuilder.parse(fileInputStream)
            val nList: NodeList = doc.getElementsByTagName("item")
            for (i in 0 until nList.length) {
                if (nList.item(0).nodeType.equals(Node.ELEMENT_NODE)) {
                    itemHashMap = HashMap()
                    val element = nList.item(i) as Element
                    itemHashMap.put("title", getNodeValue("title", element))
                    itemHashMap.put("description", getNodeValue("description", element))
                    itemHashMap.put("link", getNodeValue("link", element))
                    itemHashMapList.add(itemHashMap)
                }
            }
            for (i in 0 until itemHashMapList.size) {

                var titre: String = itemHashMapList[i]["title"]!!
                var description: String = itemHashMapList[i]["description"]!!
                var link: String = itemHashMapList[i]["link"]!!
                infoViewModel.insertInfo(Info(TITLE = titre, DESCRIPTION = description, LINK = link, NOUVEAU = true, fluxUrl = url))
                enqueueWork(context,DownloadServices::class.java, 1,work)

            }
        }

        fun getNodeValue(tag: String, element: Element): String {
            val nodeList = element.getElementsByTagName(tag)
            val node = nodeList.item(0)
            if (node != null) {
                if (node.hasChildNodes()) {
                    val child = node.firstChild
                    while (child != null) {
                        return child.nodeValue
                    }
                }
            }
            return ""
        }

    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 26) {

            val id = Random.nextInt(1,200000).toString()
            val description = Random.nextInt(1,200000).toString()
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, description, importance)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
             manager.createNotificationChannel(channel)
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("show",0)
            val pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            val notification: Notification = Notification.Builder(this, id)
                    .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                    .setContentTitle("Notification")
                    .setContentText("Show Items ")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

            startForeground(123,notification)


        }
    }
    override fun onHandleWork(intent: Intent) {

    }


}





