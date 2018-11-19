package com.elvis.example.wifichat.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.View
import com.elvis.example.wifichat.R
import com.elvis.example.wifichat.receiver.WiFiDirectBroadcastReceiver
import com.elvis.example.wifichat.utils.PermissionHelper
import com.elvis.example.wifichat.utils.PermissionInterface
import com.github.dfqin.grantor.PermissionListener
import com.github.dfqin.grantor.PermissionsUtil

class MainActivity : AppCompatActivity(), PermissionInterface {

  companion object {
    const val TAG = "MainActivity"
  }

  val fragments = arrayOf(ChatFragment(), SearchFragment(), DetailFragment())
  val REQUEST_EXTERNAL_STORAGE = 1
  private var manager: WifiP2pManager? = null
  private val intentFilter = IntentFilter()
  private var channel: WifiP2pManager.Channel? = null
  private var receiver: BroadcastReceiver? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
    intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
    intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
    intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    channel = manager!!.initialize(this, mainLooper, null)
    supportFragmentManager.beginTransaction()
        .add(R.id.container,fragments[0], "f1")
        .add(R.id.container,fragments[1], "f2")
        .add(R.id.container,fragments[2], "f3").commit()

    chatFragment(null)
    val mPermissionsHelper = PermissionHelper(this,this)
    mPermissionsHelper.requestPermissions()
  }

  override fun onResume() {
    super.onResume()
    receiver = WiFiDirectBroadcastReceiver(manager, channel, this)
    registerReceiver(receiver,intentFilter)
  }

  override fun onPause() {
    super.onPause()
    unregisterReceiver(receiver)
  }

  fun chatFragment(view: View?){
    supportFragmentManager.beginTransaction()
        .hide(fragments[1])
        .hide(fragments[2])
        .show(fragments[0]).commit()
  }

  fun searchFragment(view: View?){
    supportFragmentManager.beginTransaction()
        .hide(fragments[0])
        .hide(fragments[2])
        .show(fragments[1]).commit()
  }

  fun otherFragment(view: View?){
    supportFragmentManager.beginTransaction()
        .hide(fragments[0])
        .hide(fragments[1])
        .show(fragments[2]).commit()
  }

  fun resetData() {
    val search: SearchFragment = fragments[1] as SearchFragment
    search.clearPeers()
  }

  override fun getPermissionsRequestCode(): Int {
    //设置权限代码，只需要不冲突即可
    return 10000
  }

  override fun getPermissions(): Array<String> {
    return arrayOf(
        "android.permission.READ_PHONE_STATE",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.ACCESS_COARSE_LOCATION")
  }

  override fun requestPermissionsSuccess() {
    d(TAG,"permission granted")
  }

  override fun requestPermissionsFail() {
    d(TAG,"failed to get permission granted")
  }
}
