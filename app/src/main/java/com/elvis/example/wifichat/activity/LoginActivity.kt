package com.elvis.example.wifichat.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log.d
import com.elvis.example.wifichat.Constant
import com.elvis.example.wifichat.R
import com.elvis.example.wifichat.WiHelper
import com.elvis.example.wifichat.bean.KeyPair
import com.elvis.example.wifichat.bean.Msk
import com.elvis.example.wifichat.bean.Params
import com.elvis.example.wifichat.bean.Pid
import com.google.gson.Gson
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

  companion object {
    const val TAG = "LoginActivity"
  }

  var userName: String? = null
  var userPwd: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    username.addTextChangedListener(object: TextWatcher{
      override fun afterTextChanged(s: Editable?) {
        password.text = null
      }
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      }
    })
  }

  fun login(){
    userName = username.text.toString().trim()
    userPwd = password.text.toString().trim()

    if (TextUtils.isEmpty(userName)){
      toast(R.string.username_cannot_be_empty)
      return
    }
    if (TextUtils.isEmpty(userPwd)) {
      toast(R.string.password_cannot_be_empty)
      return
    }

    getValue()
  }

  fun register(){
    startActivity(Intent(this,RegisterActivity::class.java))
  }

  private fun getValue() {
    doAsync {
      val urlString = Constant.URL_LOGIN
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
      call.enqueue(object: Callback{
        override fun onFailure(call: Call, e: IOException) {
          runOnUiThread(object : Runnable {
            override fun run() {
              toast(getString(R.string.connect_failed) + "because: " + e)
            }
          })

        }

        override fun onResponse(call: Call, response: Response) {
          val resp = response.body()!!.string()
          val gson = Gson()
          val jsonObject = JSONObject(resp)
          val g_field = jsonObject.getInt("g_field")
          val h_field = jsonObject.getInt("h_field")
          val alpha_field = jsonObject.getInt("alpha_field")
          val pub_field = jsonObject.getInt("pub_field")
          val prv_field = jsonObject.getInt("prv_field")
          val msk_field = jsonObject.getInt("msk_field")
          val msg = jsonObject.getInt("msg")
          val pid = gson.fromJson<Pid>(jsonObject.getString("pid"), Pid::class.java)
          val pidList = jsonObject.getString("pids")
          val pairing_desc = jsonObject.getString("pairing_desc")
          val g = jsonObject.getString("g").toByteArray(charset("ISO-8859-1"))
          val h = jsonObject.getString("h").toByteArray(charset("ISO-8859-1"))
          val alpha = jsonObject.getString("alpha").toByteArray(charset("ISO-8859-1"))
          val pub = jsonObject.getString("key_pub").toByteArray(charset("ISO-8859-1"))
          val prv = jsonObject.getString("key_prv").toByteArray(charset("ISO-8859-1"))
          val mskString = jsonObject.getString("msk").toByteArray(charset("ISO-8859-1"))
          val params = Params()
          val pairing = PairingFactory.getPairing("assets/a.properties")
          PairingFactory.getInstance().isUsePBCWhenPossible = true
          params.pairing_desc = pairing_desc
          params.g = pairing.getFieldAt(g_field).newElementFromBytes(g)
          params.h = pairing.getFieldAt(h_field).newElementFromBytes(h)
          params.hat_alpha = pairing.getFieldAt(alpha_field).newElementFromBytes(alpha)
          params.e = pairing
          val msk = Msk()
          msk.alpha = pairing.getFieldAt(msk_field).newElementFromBytes(mskString)
          val keyPair = KeyPair()
          keyPair.pub = pairing.getFieldAt(pub_field).newElementFromBytes(pub)
          keyPair.prv = pairing.getFieldAt(prv_field).newElementFromBytes(prv)
          WiHelper.msk = msk
          WiHelper.keyPair = keyPair
          WiHelper.params = params
          d(TAG,"*****************************************************************************")
          d(TAG,"setup--output: pairing desc is " + params.pairing_desc)
          d(TAG,"setup--output: pairing g is " + params.g)
          d(TAG,"setup--output: pairing h is " + params.h)
          d(TAG,"setup--output: g hat alpha is " + params.hat_alpha)
          d(TAG,"setup--output: msk key is " + msk.alpha)
          d(TAG, "*****************************************************************************")
          if (msg.equals(Constant.SUCCESSCODE_LOGIN)) {
            d(TAG, "---------------------登录成功!------------------------")
            WiHelper.currentUser = pid.pid
            val bundle = Bundle()
            bundle.putString("pids", pidList)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java).putExtras(bundle))
            finish()
          } else {
            runOnUiThread(object: Runnable {
              override fun run() {
                toast(getString(R.string.login_failed) + "错误代码:" + msg)
              }
            })
          }
        }
      })
    }
  }
}