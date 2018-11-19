package com.elvis.example.wifichat.utils

import android.app.Activity
import android.content.pm.PackageManager

class PermissionHelper(var mActivity: Activity, var mPermissionInterface: PermissionInterface) {
  /**
   * 开始请求权限。
   * 方法内部已经对Android M 或以上版本进行了判断，外部使用不再需要重复判断。
   * 如果设备还不是M或以上版本，则也会回调到requestPermissionsSuccess方法。
   */
  fun requestPermissions() {
    val deniedPermissions = PermissionUtil.getDeniedPermissions(mActivity, mPermissionInterface.getPermissions())
    if (deniedPermissions != null && deniedPermissions.isNotEmpty()) {
      PermissionUtil.requestPermissions(mActivity, deniedPermissions, mPermissionInterface.getPermissionsRequestCode())
    } else {
      mPermissionInterface.requestPermissionsSuccess()
    }
  }

  /**
   * 在Activity中的onRequestPermissionsResult中调用
   * @param requestCode
   * @param permissions
   * @param
   * @return true 代表对该requestCode感兴趣，并已经处理掉了。false 对该requestCode不感兴趣，不处理。
   */
  fun requestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResult: IntArray): Boolean{
    if (requestCode == mPermissionInterface.getPermissionsRequestCode()) {
      var isAllGranted = true
      grantResult.forEach {
        if (it == PackageManager.PERMISSION_DENIED) {
          isAllGranted = false
          return@forEach
        }
      }
      if (isAllGranted) {
        mPermissionInterface.requestPermissionsSuccess()
      } else {
        mPermissionInterface.requestPermissionsFail()
      }
      return true
    }
    return false
  }
}