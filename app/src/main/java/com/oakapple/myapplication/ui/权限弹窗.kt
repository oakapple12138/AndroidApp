package com.oakapple.myapplication.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun 请求所有文件访问权限弹窗(跳转: () -> Unit, 已知晓: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("需要所有文件访问权限") },
        text = { Text("应用需要“所有文件访问权限”使用所有功能，否则应用功能会受到限制") },
        confirmButton = {
            TextButton(onClick = 跳转) {
                Text("跳转到设置开启权限")
            }
        },
        dismissButton = {
            TextButton(onClick = 已知晓) {
                Text("已知晓")
            }
        }
    )
}