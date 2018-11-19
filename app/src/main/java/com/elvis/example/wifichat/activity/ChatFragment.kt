package com.elvis.example.wifichat.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elvis.example.wifichat.R

class ChatFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view: View = inflater.inflate(R.layout.fragment_chat, container, false)

    return view
  }
}