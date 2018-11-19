package com.elvis.example.wifichat

class Constant {
  companion object {
    const val URL = "http://mjhzds.vicp.io/chat/"
    const val URL_REGISTER = URL + "register"
    const val URL_LOGIN = URL + "login"
    const val SUCCESSCODE_LOGIN = 100
    const val SUCCESSCODE_REGISTER = 101
    const val ERRORCODE_NULL = 200
    const val ERRORCODE_PWD_WRONG = 201
    const val ERRORCODE_ACCOUNT_NOT_EXIST = 202
    const val ERRORCODE_ACCOUNT_ALREADY_EXIST = 203
  }
}