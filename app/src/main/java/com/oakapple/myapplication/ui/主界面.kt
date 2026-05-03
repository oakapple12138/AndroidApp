package com.oakapple.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.oakapple.myapplication.model.焦点
import com.oakapple.myapplication.viewmodel.文件管理器控制器

@Composable
fun 主界面(控制器: 文件管理器控制器, 请求权限回调: () -> Unit) {

    if (!控制器.所有文件访问权限状态()) {
        请求所有文件访问权限弹窗(
            跳转 = 请求权限回调,
            已知晓 = { 控制器.更新权限状态(true) }
        )
    }

    if (控制器.显示文件操作弹窗().value) {
        文件操作弹窗(控制器)
    }

    if (控制器.显示文件操作异常弹窗().value) {
        AlertDialog(
            onDismissRequest = { 控制器.显示文件操作异常弹窗(false) },
            title = { Text("文件操作失败") },
            text = { Text(控制器.异常信息.value) },
            confirmButton = {
                TextButton(onClick = { 控制器.显示文件操作异常弹窗(false) }) {
                    Text("确定")
                }
            }
        )
    }

    Column(Modifier.fillMaxSize().navigationBarsPadding()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.08f)
                .background(Color(0, 0, 0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = 控制器.当前目录.toString(),
                color = Color(255, 255, 255),
                fontSize = 34.sp
            )
        }

        Row(Modifier.fillMaxWidth().weight(1f)) {
            文件列表(控制器, 焦点.左, Modifier.weight(1f))
            文件列表(控制器, 焦点.右, Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.05f)
                .background(Color(0.5f, 0.5f, 0.5f)),
            contentAlignment = Alignment.Center
        ){
            Row (
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ){
                新建文件(控制器)
            }
        }

    }
}