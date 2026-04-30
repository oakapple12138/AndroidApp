package com.oakapple.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.oakapple.myapplication.viewmodel.文件管理器控制器

@Composable
fun 文件操作弹窗(控制器: 文件管理器控制器) {
    Dialog(onDismissRequest = { 控制器.显示文件操作弹窗(false) }) {
        Column {
            Row {
                TextButton(onClick = {
                    控制器.文件复制()
                    控制器.显示文件操作弹窗(false)
                }) { Text("复制") }
                TextButton(onClick = {}) { Text("移动") }
            }
            Row {
                TextButton(onClick = {}) { Text("删除") }
                TextButton(onClick = {}) { Text("重命名") }
            }
        }
    }
}