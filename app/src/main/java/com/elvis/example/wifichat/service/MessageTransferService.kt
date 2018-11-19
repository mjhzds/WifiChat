package com.elvis.example.wifichat.service

import android.app.IntentService
import android.content.Intent
import android.util.Log.d
import java.net.InetSocketAddress
import java.net.Socket


class MessageTransferService(val name: String): IntentService(name) {

  constructor(): this("MessageTransferService")

  companion object {
    val ACTION_SEND_MESSAGE = "com.example.android.wifidirect.SEND_MESSAGE"
    val EXTRAS_GROUP_OWNER_ADDRESS = "go_host"
    val EXTRAS_GROUP_OWNER_PORT = "go_port"
    val MESSAGE = "message"
  }

  val TAG = "MessageTransferService"
  private val SOCKET_TIMEOUT = 5000

  override fun onHandleIntent(intent: Intent?) {
    val context = applicationContext
    if (intent?.action.equals(ACTION_SEND_MESSAGE)) {
      val message = intent?.extras!!.getString(MESSAGE)
      val host = intent.extras!!.getString(EXTRAS_GROUP_OWNER_ADDRESS)
      val port = intent.extras!!.getInt(EXTRAS_GROUP_OWNER_PORT)
      val socket = Socket()
      d(TAG,"Opening client socket - ")
      socket.bind(null)
      socket.connect(InetSocketAddress(host, port), SOCKET_TIMEOUT)
      d(TAG, "Client socket - ${socket.isConnected}")
      val stream = socket.getOutputStream()
      stream.write(message!!.toByteArray())
      d(TAG,"Client: message written")
      socket.close()
    }
  }

}