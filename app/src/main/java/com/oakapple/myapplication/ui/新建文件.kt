package com.oakapple.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import com.oakapple.myapplication.viewmodel.文件管理器控制器

@Composable
fun 新建文件(控制器: 文件管理器控制器) {
    val 弹窗状态 = remember { mutableStateOf(false) }

    var 名称 by remember { mutableStateOf("") }

    if (弹窗状态.value) {
        Dialog(onDismissRequest = { 弹窗状态.value = false }) {
            Column {
                Text("新建")
                TextField(
                    value = 名称,
                    onValueChange = { 名称 = it }
                )
                Row {
                    TextButton(onClick = {
                        弹窗状态.value = false
                        名称 = ""
                    }) {
                        Text("取消")
                    }
                    TextButton(onClick = {
                        控制器.文件新建(名称, false)
                        弹窗状态.value = false
                        名称 = ""
                    }) {
                        Text("文件")
                    }
                    TextButton(onClick = {
                        控制器.文件新建(名称, true)
                        弹窗状态.value = false
                        名称 = ""
                    }) {
                        Text("文件夹")
                    }
                }
            }
        }
    }

    TextButton(onClick = { 弹窗状态.value = true }) {
        Text(text = "+", color = Color(255, 255, 255))
    }
}
