package com.oakapple.myapplication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import com.oakapple.myapplication.viewmodel.文件管理器控制器

@Composable
fun 文件操作弹窗(控制器: 文件管理器控制器) {
    var 重命名弹窗状态 by remember { mutableStateOf(false) }
    var 新名称 by remember { mutableStateOf("") }

    if (重命名弹窗状态) {
        AlertDialog(
            onDismissRequest = { 重命名弹窗状态 = false },
            title = { Text("重命名") },
            text = {
                TextField(
                    value = 新名称,
                    onValueChange = { 新名称 = it }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    控制器.文件重命名(新名称)
                    新名称 = ""
                    重命名弹窗状态 = false
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    新名称 = ""
                    重命名弹窗状态 = false
                }) {
                    Text("取消")
                }
            }
        )
    }

    Dialog(onDismissRequest = { 控制器.显示文件操作弹窗(false) }) {
        Column {
            Row {
                TextButton(onClick = {
                    控制器.文件复制()
                    控制器.显示文件操作弹窗(false)
                }) { Text("复制") }
                TextButton(onClick = {
                    控制器.文件移动()
                    控制器.显示文件操作弹窗(false)
                }) { Text("移动") }
            }
            Row {
                TextButton(onClick = {
                    控制器.文件删除()
                    控制器.显示文件操作弹窗(false)
                }) { Text("删除") }
                TextButton(onClick = { 重命名弹窗状态 = true }) { Text("重命名") }
            }
        }
    }
}
