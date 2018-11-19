package com.elvis.example.wifichat.activity

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elvis.example.wifichat.service.MessageTransferService
import com.elvis.example.wifichat.R
import com.elvis.example.wifichat.socket.ServerListening
import kotlinx.android.synthetic.main.fragment_detail.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DetailFragment : Fragment(), WifiP2pManager.ConnectionInfoListener {

  companion object {
    const val TAG = "DetailFragment"
  }

  var device:WifiP2pDevice? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_detail, container, false)
    return view
  }

  override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
    if (info == null) {
      activity!!.toast("连接不可用！")
    } else {
      val groupOwnerAddress: String = info.groupOwnerAddress.hostAddress
      d(TAG, "Group Owner IP - $groupOwnerAddress")
      if (info.groupFormed && info.isGroupOwner) {
        ServerListening().start()
      } else {
        activity!!.startActivity<ChatActivity>(
            Pair(MessageTransferService.EXTRAS_GROUP_OWNER_ADDRESS, info.groupOwnerAddress.hostAddress)
        )
      }
    }
  }

  fun updateThisDevice(device: WifiP2pDevice) {
    this.device = device
    my_name.text = device.deviceName
    my_status.text = getDeviceStatus(device.status)
  }

  private fun getDeviceStatus(deviceStatus: Int): String {
    Log.d(TAG, "Peer status :$deviceStatus")
    when (deviceStatus) {
      WifiP2pDevice.AVAILABLE -> return "Available"
      WifiP2pDevice.INVITED -> return "Invited"
      WifiP2pDevice.CONNECTED -> return "Connected"
      WifiP2pDevice.FAILED -> return "Failed"
      WifiP2pDevice.UNAVAILABLE -> return "Unavailable"
      else -> return "Unknown"
    }
  }
}