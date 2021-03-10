package com.example.projetandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Show : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:InfoAdapter
    private lateinit var infoViewModel: InfoViewModel
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var myvalue = arguments?.getInt("value")
      val  v = inflater.inflate(R.layout.fragment_show, container, false)
        webView =v.findViewById(R.id.webview)
        infoViewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = InfoAdapter(context!!,infoViewModel,webView)
        recyclerView.adapter = adapter

        if (myvalue ==1)
        {
            infoViewModel.updateInfo(false)
            infoViewModel.selectAllInfo().observe(viewLifecycleOwner, Observer {
                adapter.setinfo(it)
            }

            )
        }
        else
        {
            if (myvalue ==0)
            {
                //afficher les nouveau
                infoViewModel.selectNouveau(true).observe(viewLifecycleOwner, Observer { adapter.setinfo(it) })

            }
        }
        return v
    }


}