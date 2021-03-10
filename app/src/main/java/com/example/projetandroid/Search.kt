package com.example.projetandroid

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Search : Fragment() {

    private lateinit var edit : EditText
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : InfoAdapter
    private lateinit var viewModel : InfoViewModel
    private lateinit var webView : WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search,container,false)
        edit = v.findViewById(R.id.text)
        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        webView = v.findViewById(R.id.webview)
        viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        adapter = InfoAdapter(requireContext(), viewModel,webView )
        recyclerView.adapter=adapter
        viewModel.selectAllInfo().observe(viewLifecycleOwner){
            adapter.setinfo(it)
        }
        if (savedInstanceState != null)
        {
            val text = savedInstanceState.getString("edit")
            edit.setText(text)
        }
        edit.addTextChangedListener(listener)
        return v
    }
    var listener = object  : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val chaine = s.toString()
            if(chaine !="")
            {
                viewModel.selectFromKey("%"+chaine+"%").observe(this@Search){
                    adapter.setinfo(it)
                }
            }
            else
            {
                viewModel.selectAllInfo().observe(this@Search){
                    adapter.setinfo(it)
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       val sauvegardText = edit.getText().toString()
        outState.putString("edit",sauvegardText)


    }


}