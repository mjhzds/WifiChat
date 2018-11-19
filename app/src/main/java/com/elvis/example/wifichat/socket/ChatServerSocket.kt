package com.elvis.example.wifichat.socket

import com.elvis.example.wifichat.utils.ChatManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class ChatServerSocket(var socket: Socket) : Thread() {

  var messages: MutableList<String>? = mutableListOf()

  override fun run() {
    out("已连接到服务器！")
    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    var line: String? = reader.readLine()
    while (line != null) {
      ChatManager.getInstance().publish(this, line)
      messages?.add(line)
      line = reader.readLine()
    }
    reader.close()
    ChatManager.getInstance().remove(this)
  }

  fun out(message: String) {
    socket.getOutputStream().write((message + "\n").toByteArray())
  }
}