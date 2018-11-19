package com.elvis.example.wifichat.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log.d
import com.elvis.example.wifichat.Constant
import com.elvis.example.wifichat.R
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.IOException

class RegisterActivity: AppCompatActivity() {

  companion object {
    const val TAG = "RegisterActivity"
  }
  var userName: String? = null
  var userPwd: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)
  }

  fun register(){
    userName = username.text.toString().trim()
    userPwd = password.text.toString().trim()
    val confirmPwd = confirm_password.text.trim()
    if (TextUtils.isEmpty(userName)) {
      toast(R.string.username_cannot_be_empty)
      username.requestFocus()
      return
    } else if (TextUtils.isEmpty(userPwd)) {
      toast(R.string.password_cannot_be_empty)
      password.requestFocus()
      return
    } else if (TextUtils.isEmpty(confirmPwd)) {
      toast(R.string.confirm_password_cannot_be_empty)
      confirm_password.requestFocus()
      return
    } else if (!userPwd!!.equals(confirmPwd)) {
      toast(R.string.password_not_equal)
      return
    }
    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPwd)) {
      getValue()
    }
  }

  private fun getValue() {
    doAsync {
      val urlString = Constant.URL_REGISTER
      val client = OkHttpClient()
      val formBody = FormBody.Builder()
              .add("account",userName!!)
              .add("password",userPwd!!)
              .build()
      val request = Request.Builder()
              .url(urlString)
              .post(formBody)
              .build()
      val call = client.newCall(request)
      call.enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
          runOnUiThread(object : Runnable {
            override fun run() {
              toast(getString(R.string.connect_failed) + "because: " + e)
            }
          })
        }
        override fun onResponse(call: Call, response: Response) {
          val resp = response.body()!!.string()
          val jsonObject = JSONObject(resp)
          val msg = jsonObject.getInt("msg")
          if (msg == Constant.SUCCESSCODE_REGISTER) {
            d(TAG,"---------------------注册成功!------------------------")
            runOnUiThread(object : Runnable {
              override fun run() {
                if (!this@RegisterActivity.isFinishing) {
                  toast(R.string.registered_successfully)
                  finish()
                }
              }
            })
          }else {
            toast(getString(R.string.registered_failed) + "错误代码:" + msg)
          }
        }
      })
    }
  }
}