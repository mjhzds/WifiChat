package com.elvis.example.wifichat.socket

import com.elvis.example.wifichat.utils.ChatManager
import java.net.ServerSocket
import java.net.Socket

class ServerListening: Thread() {
  override fun run() {
    val serverSocket = ServerSocket()
    while (true) {
      val socket: Socket = serverSocket.accept()
      val chatSocket = ChatServerSocket(socket)
      chatSocket.start()
      ChatManager.getInstance().add(chatSocket)
    }
  }
}