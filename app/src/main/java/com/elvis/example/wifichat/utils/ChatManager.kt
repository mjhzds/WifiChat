package com.elvis.example.wifichat.utils

import com.elvis.example.wifichat.socket.ChatServerSocket
import java.util.*

class ChatManager private constructor(){
  companion object {
    fun getInstance() : ChatManager = Holder.instance
  }

  private object Holder{
    val instance = ChatManager()
  }

  val sockets: Vector<ChatServerSocket> = Vector()

  fun add(cs: ChatServerSocket) {
    sockets.add(cs)
  }

  fun remove(cs: ChatServerSocket) {
    sockets.remove(cs)
  }

  fun publish(cs: ChatServerSocket, message: String) {
    sockets.forEach{
      if (!it.equals(cs)) {
        it.out(message)
      }
    }
  }
}