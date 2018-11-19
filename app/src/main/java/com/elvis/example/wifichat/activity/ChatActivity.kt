package com.elvis.example.wifichat.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.elvis.example.wifichat.R
import com.elvis.example.wifichat.service.MessageTransferService
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity(), View.OnClickListener {

  var host: String? = null
  val port: Int = 8988

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat)
    host = intent.extras!!.getString(MessageTransferService.EXTRAS_GROUP_OWNER_ADDRESS)
  }

  override fun onClick(v: View?) {
    when (v!!.id) {
      R.id.btn_send -> {
        val intent = Intent(this, MessageTransferService::class.java)
        intent.putExtra(MessageTransferService.EXTRAS_GROUP_OWNER_ADDRESS, host)
        intent.putExtra(MessageTransferService.EXTRAS_GROUP_OWNER_PORT, port)
        intent.putExtra(MessageTransferService.MESSAGE, et_content.text.toString())
        intent.action = MessageTransferService.ACTION_SEND_MESSAGE
        startService(intent)
      }
    }
  }
}