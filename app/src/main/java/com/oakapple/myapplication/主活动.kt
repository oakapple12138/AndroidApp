package com.oakapple.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.oakapple.myapplication.ui.主界面
import com.oakapple.myapplication.viewmodel.文件管理器控制器

class 主活动 : ComponentActivity() {
    private lateinit var 控制器: 文件管理器控制器

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }
        控制器 = ViewModelProvider(this)[文件管理器控制器::class.java]
        控制器.初始化(filesDir, filesDir, 所有文件访问权限状态())
        setContent {
            主界面(控制器 = 控制器, 请求权限回调 = { 请求所有文件访问权限() })
        }
    }

    override fun onResume() {
        super.onResume()
        if (::控制器.isInitialized) {
            控制器.更新权限状态(所有文件访问权限状态())
        }
    }

    fun 所有文件访问权限状态(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun 请求所有文件访问权限() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val 意图 = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                意图.data = "package:$packageName".toUri()
                startActivity(意图)
            } catch (_: Exception) {
                try {
                    val 意图 = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(意图)
                } catch (_: Exception) {
                    val 意图 = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    意图.data = "package:$packageName".toUri()
                    startActivity(意图)
                }
            }
        }
    }
}
