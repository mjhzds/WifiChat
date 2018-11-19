package com.elvis.example.wifichat.activity

import org.jetbrains.anko.startActivity
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.Fragment
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.elvis.example.wifichat.R
import com.elvis.example.wifichat.adapter.WiFiPeerListAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.toast

class SearchFragment: Fragment(),WifiP2pManager.PeerListListener,
    View.OnClickListener, WifiP2pManager.ChannelListener {

  companion object {
    val TAG = "SearchFragment"
  }

  var peers:MutableList<WifiP2pDevice>? = null
  var device: WifiP2pDevice? = null
  var adapter: WiFiPeerListAdapter? = null
  var isWifiEnabled: Boolean = false
  var manager: WifiP2pManager? = null
  var channel: WifiP2pManager.Channel? = null
  var selected: MutableList<WifiP2pDevice>? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view: View = inflater.inflate(R.layout.fragment_search, container, false)
    adapter = WiFiPeerListAdapter(context!!,R.layout.list_item,peers)
    manager = context!!.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    channel = manager!!.initialize(context, Looper.getMainLooper(), null)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btn_search.setOnClickListener(this)
    btn_connect.setOnClickListener(this)
    btn_concel.setOnClickListener(this)
    btn_reverse.setOnClickListener(this)
    device_list.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
      val device: WifiP2pDevice? = peers?.get(position)
      val config = WifiP2pConfig()
      config.deviceAddress = device?.deviceAddress
      channel!!.also { channel ->
        manager!!.createGroup(channel, object : WifiP2pManager.ActionListener {
          override fun onSuccess() {
            d(TAG,"created a group")
          }

          override fun onFailure(reason: Int) {
            d(TAG, "failed to created a group:$reason")
          }

        })
        manager!!.connect(channel, config, object : WifiP2pManager.ActionListener {

          override fun onSuccess() {
            activity!!.toast("邀请成功！")
          }

          override fun onFailure(reason: Int) {
            activity!!.toast("邀请失败！$reason")
          }
        })
      }
    }
  }

  override fun onClick(v: View?) {
    when(v!!.id){
      R.id.btn_search -> {
        progressBar.visibility = View.VISIBLE
        if (!isWifiEnabled) {
          activity!!.toast("请打开WIFI！")
          startActivity(Intent(activity, Settings.ACTION_WIRELESS_SETTINGS::class.java))
        } else {
          manager!!.discoverPeers(channel, object : WifiP2pManager.ActionListener{
            override fun onSuccess() {
              context!!.toast("Discovery Initiated")
            }

            override fun onFailure(reason: Int) {
              context!!.toast("Discovery Failed : $reason")
            }
          })
        }
      }

    }
  }

  override fun onPeersAvailable(peerList: WifiP2pDeviceList) {
    if (progressBar != null && progressBar.isShown) {
      progressBar.visibility = View.GONE
    }
    peers?.clear()
    if (peerList.deviceList?.size == 0) {
      context!!.toast("未发现设备！")
      return
    }
    peers?.addAll(peerList.deviceList)
    adapter!!.notifyDataSetChanged()
  }

  override fun onChannelDisconnected() {
    activity!!.toast("WIFI失去连接！")
  }

  fun clearPeers(){
    peers?.clear()
    adapter!!.notifyDataSetChanged()
  }
}