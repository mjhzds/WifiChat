package com.elvis.example.wifichat.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.security.Permission

class PermissionUtil {

  companion object {
    /**
     * 判断是否有某个权限
     * @param context
     * @param permission
     * @return
     */
    fun hasPermission(context: Context, permission: String): Boolean {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if(context.checkSelfPermission(permission)  != PackageManager.PERMISSION_GRANTED){
          return false
        }
      }
      return true
    }

    /**
     * 弹出对话框请求权限
     * @param activity
     * @param permissions
     * @param requestCode
     */
    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.requestPermissions(permissions, requestCode)
      }
    }
    /**
     * 返回缺失的权限
     * @param context
     * @param permissions
     * @return 返回缺少的权限，null 意味着没有缺少权限
     */
    fun getDeniedPermissions(context: Context, permissions: Array<String>): Array<String>? {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val deniedPermissionList: MutableList<String> = ArrayList()
        permissions.forEach {
          if(context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED){
            deniedPermissionList.add(it)
          }
        }
        val size = deniedPermissionList.size
        if(size > 0){
          return deniedPermissionList.toTypedArray()
        }
      }
      return null
    }
  }
}