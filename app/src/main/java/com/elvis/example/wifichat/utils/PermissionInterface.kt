package com.elvis.example.wifichat.utils

interface PermissionInterface {
  /**
   * 可设置请求权限请求码
   */
  fun getPermissionsRequestCode(): Int

  /**
   * 设置需要请求的权限
   */
  fun getPermissions(): Array<String>

  /**
   * 请求权限成功回调
   */
  fun requestPermissionsSuccess()

  /**
   * 请求权限失败回调
   */
  fun requestPermissionsFail()
}