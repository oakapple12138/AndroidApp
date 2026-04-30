package com.oakapple.myapplication.service

import java.io.File

object 目录预设提供者 {

    private val _根目录预设列表 = listOf(
        "data",
        "etc",
        "mnt",
        "proc",
        "product",
        "sdcard",
        "storage",
        "system",
        "system_ext",
        "vendor"
    )
    private val _预设列表= mapOf(
        "/" to _根目录预设列表,
        "/storage" to listOf("emulated"),
        "/storage/emulated" to listOf("0")
    )

    fun 获取显示文件列表(当前目录: File?): Array<File> {
        val 原始文件列表 = 当前目录?.listFiles() ?: emptyArray()

        val 路径 = when (val 绝对路径 = 当前目录?.absolutePath) {
            null -> return 原始文件列表
            "/" -> "/"
            else -> 绝对路径.trimEnd('/')
        }

        if (原始文件列表.isNotEmpty()) return 原始文件列表

        val 预设列表 = _预设列表[路径]

        return 预设列表
            ?.map { File(当前目录, it) }
            ?.filter { it.exists() }
            ?.toTypedArray()
            ?: emptyArray()
    }
}
