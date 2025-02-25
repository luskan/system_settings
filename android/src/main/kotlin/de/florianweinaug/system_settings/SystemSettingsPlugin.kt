package de.florianweinaug.system_settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import java.lang.Exception

class SystemSettingsPlugin : FlutterPlugin, MethodCallHandler {

  private lateinit var channel: MethodChannel
  private lateinit var applicationContext: Context

  override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    applicationContext = binding.applicationContext
    channel = MethodChannel(binding.binaryMessenger, "system_settings")
    channel.setMethodCallHandler(this)
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
    when (call.method) {
      "app"                 -> openAppSettings()
      "app-notifications"   -> openAppNotificationSettings()
      "system"              -> openSystemSettings()
      "location"            -> openSetting(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
      "wifi"                -> openSetting(Settings.ACTION_WIFI_SETTINGS)
      "bluetooth"           -> openSetting(Settings.ACTION_BLUETOOTH_SETTINGS)
      "nfc"                 -> openSetting(Settings.ACTION_NFC_SETTINGS)
      "security"            -> openSetting(Settings.ACTION_SECURITY_SETTINGS)
      "display"             -> openSetting(Settings.ACTION_DISPLAY_SETTINGS)
      "date"                -> openSetting(Settings.ACTION_DATE_SETTINGS)
      "sound"               -> openSetting(Settings.ACTION_SOUND_SETTINGS)
      "apps"                -> openSetting(Settings.ACTION_APPLICATION_SETTINGS)
      "wireless"            -> openSetting(Settings.ACTION_WIRELESS_SETTINGS)
      "device-info"         -> openSetting(Settings.ACTION_DEVICE_INFO_SETTINGS)
      "data-usage"          -> openSetting(Settings.ACTION_DATA_USAGE_SETTINGS)
      "data-roaming"        -> openSetting(Settings.ACTION_DATA_ROAMING_SETTINGS)
      "locale"              -> openSetting(Settings.ACTION_LOCALE_SETTINGS)
      "default-apps"        -> openSetting(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
      "airplane-mode"       -> openSetting(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
      "privacy"             -> openSetting(Settings.ACTION_PRIVACY_SETTINGS)
      "accessibility"       -> openSetting(Settings.ACTION_ACCESSIBILITY_SETTINGS)
      "internal-storage"    -> openSetting(Settings.ACTION_INTERNAL_STORAGE_SETTINGS)
      "notification-policy" -> openSetting(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
      "power-options"       -> openSetting(Settings.ACTION_BATTERY_SAVER_SETTINGS)
      else                  -> result.notImplemented()
    }
  }

  private fun openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", applicationContext.packageName, null)
    intent.data = uri
    applicationContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
  }

  private fun openAppNotificationSettings() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, applicationContext.packageName)
    } else {
      Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .setData(Uri.parse("package:${applicationContext.packageName}"))
    }
    applicationContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
  }

  private fun openSetting(action: String) {
    try {
      applicationContext.startActivity(Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (e: Exception) {
      openSystemSettings()
    }
  }

  private fun openSystemSettings() {
    applicationContext.startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
  }
}
