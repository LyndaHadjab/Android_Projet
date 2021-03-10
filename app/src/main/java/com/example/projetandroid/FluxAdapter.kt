package com.example.projetandroid

import android.graphics.Color.rgb
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.flux_item.view.*


class FluxAdapter(var flux: List<Flux>) : RecyclerView.Adapter<FluxAdapter.VH>() {


    class VH (item:View) : RecyclerView.ViewHolder(item)

    val listener = View.OnClickListener { View->
        val v = (View.tag as Flux)
        v.checked = (View as CheckBox).isChecked
        val position :Int = flux.indexOf(v)
        flux[position].checked = v.checked

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        var vh = LayoutInflater.from(parent.getContext()).inflate(R.layout.flux_item ,parent,false)
        vh.findViewById<CheckBox>(R.id.check).setOnClickListener(listener)
        return  VH(vh)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.source.text = flux[position].source
        holder.itemView.url.text = flux[position].url
        holder.itemView.Tag.text = flux[position].tag

        if(position % 2 ==0)
        {
            holder.itemView.setBackgroundColor(rgb(255, 189, 0))
        }
        else
        {
            holder.itemView.setBackgroundColor(rgb(255, 170, 225))
        }
        var fluxcheck =  holder.itemView.findViewById<CheckBox>(R.id.check)
        fluxcheck.tag = flux[position]
        fluxcheck.isChecked = flux[position].checked

    }

    override fun getItemCount(): Int {
        return  flux.size
    }
    fun setListFlux(fl:List<Flux>)
    {
        this.flux = fl;
        notifyDataSetChanged()
    }
    fun getListe():List<Flux>
    {
        return this.flux
    }

}