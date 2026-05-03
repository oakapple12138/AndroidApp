package com.oakapple.myapplication.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.oakapple.myapplication.model.焦点
import com.oakapple.myapplication.viewmodel.文件管理器控制器
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun 文件列表(控制器: 文件管理器控制器, 所属面板: 焦点, modifier: Modifier = Modifier) {
    val 上下文 = LocalContext.current
    val 文件夹图标 = remember { BitmapFactory.decodeStream(上下文.assets.open("文件夹.png")) }
    val 文件图标 = remember { BitmapFactory.decodeStream(上下文.assets.open("文件.png")) }
    val 文件时间戳格式 = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }

    val 当前目录 = 控制器.获取目录(所属面板).value
    val 刷新版本 = 控制器.刷新版本.value
    val 显示文件列表 = 控制器.获取显示文件列表(当前目录)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .pointerInteropFilter { 事件 ->
                if (事件.action == MotionEvent.ACTION_DOWN) { 控制器.切换焦点(所属面板) }
                false
            }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (当前目录 != null && 当前目录.absolutePath != "/") {
                item {
                    返回上一级目录(文件夹图标) {
                        控制器.切换焦点(所属面板)
                        控制器.返回上一级目录()
                    }
                }
            }
            items(显示文件列表, key = { it.absolutePath }) { 文件 ->
                val 图标 = if (文件.isFile) 文件图标 else 文件夹图标
                val 内容描述 = if (文件.isFile) "文件图片" else "文件夹图片"
                val 点击回调: () -> Unit = if (文件.isFile) {
                    { 控制器.切换焦点(所属面板) }
                } else {
                    {
                        控制器.切换焦点(所属面板)
                        控制器.打开文件夹(文件)
                    }
                }
                val 长按回调: () -> Unit ={ 控制器.显示文件操作弹窗(true, 文件) }

                文件列表项(
                    文件 = 文件,
                    图标 = 图标,
                    内容描述 = 内容描述,
                    文件时间戳格式 = 文件时间戳格式,
                    点击回调 = 点击回调,
                    长按回调 = 长按回调
                )
            }
        }
    }
}

@Composable
private fun 返回上一级目录(文件夹图标: Bitmap, 点击回调: () -> Unit) {
    ListItem(
        headlineContent = { Text("..") },
        leadingContent = {
            Image(
                bitmap = 文件夹图标.asImageBitmap(),
                contentDescription = "文件夹图片",
                modifier = Modifier.size(40.dp)
            )
        },
        modifier = Modifier.clickable { 点击回调() }
    )
}

@Composable
private fun 文件列表项(
    文件: File,
    图标: Bitmap,
    内容描述: String,
    文件时间戳格式: SimpleDateFormat,
    点击回调: () -> Unit,
    长按回调: () -> Unit
) {
    ListItem(
        headlineContent = { Text(文件.name) },
        supportingContent = { Text(文件时间戳格式.format(Date(文件.lastModified()))) },
        leadingContent = {
            Image(
                bitmap = 图标.asImageBitmap(),
                contentDescription = 内容描述,
                modifier = Modifier.size(40.dp)
            )
        },
        modifier = Modifier.combinedClickable(
            onClick = 点击回调,
            onLongClick = 长按回调
        )
    )
}