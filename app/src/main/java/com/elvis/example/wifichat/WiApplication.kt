package com.elvis.example.wifichat

import android.app.Application
import android.content.Context
import com.elvis.example.wifichat.bean.KeyPair
import com.elvis.example.wifichat.bean.Msk
import com.elvis.example.wifichat.bean.Params

class WiApplication : Application() {

  private var context: Context? = null

  override fun onCreate() {
    super.onCreate()
    context = applicationContext
  }

  fun getContext(): Context? {
    return context
  }
}