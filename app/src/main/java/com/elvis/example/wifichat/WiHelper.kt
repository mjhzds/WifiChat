package com.elvis.example.wifichat

import com.elvis.example.wifichat.bean.KeyPair
import com.elvis.example.wifichat.bean.Msk
import com.elvis.example.wifichat.bean.Params

object WiHelper {
  var keyPair: KeyPair? = null
  var params: Params? = null
  var msk: Msk? = null
  var currentUser: String? = null
}