package com.elvis.example.wifichat.adapter

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.elvis.example.wifichat.R
import org.w3c.dom.Text

class WiFiPeerListAdapter (var activity: Context, var resourceId: Int, var items: List<WifiP2pDevice>?) : ArrayAdapter<WifiP2pDevice>(activity,resourceId) {

  companion object {
    const val TAG = "WiFiPeerListAdapter"
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    super.getView(position, convertView, parent)
    val view: View
    val viewHolder: ViewHolder
    if (convertView == null) {
      viewHolder = ViewHolder()
      val vi = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      view = vi.inflate(resourceId, null)
      view.tag = viewHolder
    } else {
      view = convertView
      viewHolder = view.tag as ViewHolder
    }
    val device = items?.get(position)
    if (device != null) {
      viewHolder.name = view.findViewById<View>(R.id.device_name) as TextView
      viewHolder.status = view.findViewById<View>(R.id.device_details) as TextView
      viewHolder.name.text = device.deviceName
      viewHolder.status.text = getDeviceStatus(device.status)
    }
    return view
  }

  fun getDeviceStatus(deviceStatus: Int): String {
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

  inner class ViewHolder{
    lateinit var name: TextView
    lateinit var status: TextView
  }
}