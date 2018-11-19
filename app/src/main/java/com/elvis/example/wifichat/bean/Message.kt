package com.elvis.example.wifichat.bean


import java.io.Serializable

import it.unisa.dia.gas.jpbc.Element

class Message : Serializable {
  var pid: String? = null
  var sid: String? = null
  var index: Int = 0
  var hint_first: Element? = null
}
