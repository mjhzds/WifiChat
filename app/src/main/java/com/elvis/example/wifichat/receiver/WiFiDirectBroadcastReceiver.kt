package com.elvis.example.wifichat.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log.d
import com.elvis.example.wifichat.activity.MainActivity
import com.elvis.example.wifichat.activity.DetailFragment
import com.elvis.example.wifichat.activity.SearchFragment

class WiFiDirectBroadcastReceiver(var manager:WifiP2pManager?, var channel: WifiP2pManager.Channel?, var activity: MainActivity):
    BroadcastReceiver() {

  companion object {
    const val TAG = "WiFiBroadcastReceiver"
  }
  override fun onReceive(context: Context?, intent: Intent?) {
    val action = intent!!.action
    when(action){
      WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
        // Determine if Wifi P2P mode is enabled or not, alert
        // the Activity.
        val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
        val fragment = activity.supportFragmentManager.findFragmentByTag("f2") as SearchFragment
        fragment.isWifiEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
        d(TAG,"P2P state changed - " + state)
      }
      WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
        if (manager != null) {
          val listener = activity.supportFragmentManager.findFragmentByTag("f2") as WifiP2pManager.PeerListListener
          manager!!.requestPeers(channel, listener)
        }
        d(TAG, "P2P peers changed")
      }
      WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
        if (manager == null) {
          return
        }
        val networkInfo: NetworkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO)
        val fragment = activity.supportFragmentManager.findFragmentByTag("f3") as DetailFragment
        if (networkInfo.isConnected) {
          manager!!.requestConnectionInfo(channel, fragment)
        } else {
          activity.resetData()
        }
      }
      WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
        val fragment = activity.supportFragmentManager.findFragmentByTag("f3") as DetailFragment
        fragment.updateThisDevice(intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice)
      }
    }
  }
}