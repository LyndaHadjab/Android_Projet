package com.example.projetandroid

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color.rgb
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CheckBox
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.info_item.view.*
import java.security.AccessController.getContext


class InfoAdapter(context: Context, infoViewModel: InfoViewModel, webView: WebView):RecyclerView.Adapter<InfoAdapter.VH>(), LifecycleOwner {
    var info : List<Info> = emptyList()
    var monweb = webView

    class VH(item:View):RecyclerView.ViewHolder(item)
    val listener = View.OnClickListener { View->
        val element = (View.tag as Info)
        element.check = (View as CheckBox).isChecked
        if (element.check)
        {
            AlertDialog.Builder(context)
                .setMessage("Désirez vous supprimer l'item "+info.get(info.indexOf(element)).TITLE+' '+info.get(info.indexOf(element)).DESCRIPTION)
                .setTitle("Alert")
                .setCancelable(false)
                .setPositiveButton("Oui ")
                {
                    dialog:DialogInterface,_, ->
                    infoViewModel.deleteInfo(info.get(info.indexOf(element)))
                    infoViewModel.selectAllInfo().observe(this, Observer { this.setinfo(it) })
                    dialog.dismiss()
                }
                .setNegativeButton("Non"){dialogue,_->
                      dialogue.dismiss()
                }
                    .show()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vh = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item,parent,false)
        vh.findViewById<CheckBox>(R.id.check).setOnClickListener(listener)
        return  VH(vh)

    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.title.text=info[position].TITLE
        holder.itemView.description.text=info[position].DESCRIPTION
        var checker=holder.itemView.findViewById<CheckBox>(R.id.check)
        checker.tag=info[position]
        checker.isChecked=info[position].check

        if (position%2==0)
        {
            holder.itemView.setBackgroundColor(rgb(153,204,255))
        }
        else
        {
            holder.itemView.setBackgroundColor(rgb(204,204,255))
        }
        holder.itemView.setOnClickListener {
            monweb.webViewClient= WebViewClient()
            monweb.loadUrl(info[position].LINK)
        }
    }
    override fun getItemCount(): Int {
        return  info.size
    }
    fun setinfo(newInfo:List<Info>)
    {
        info = newInfo
        notifyDataSetChanged()
    }
    var lifecycleOwner: LifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle {
        return lifecycleOwner
    }
}