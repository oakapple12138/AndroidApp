package com.oakapple.myapplication.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.oakapple.myapplication.model.焦点
import com.oakapple.myapplication.service.目录预设提供者
import java.io.File

class 文件管理器控制器 : ViewModel() {
    companion object {
        init {
            System.loadLibrary("MainActivity")
        }
    }

    private var _左页面目录 = mutableStateOf<File?>(null)

    private var _右页面目录 = mutableStateOf<File?>(null)

    private var _当前焦点 = mutableStateOf(焦点.左)

    private var _所有文件访问权限 = mutableStateOf<Boolean?>(null)

    private var _文件操作弹窗状态 = mutableStateOf(false)
    private var _当前选中文件: File? = null
    private var _文件操作异常弹窗状态 = mutableStateOf(false)

    private var _异常信息 = mutableStateOf("无异常")
    val 异常信息: MutableState<String> get() = _异常信息

    private var _刷新版本 = mutableStateOf(0)
    val 刷新版本: State<Int> get() = _刷新版本

    val 当前目录: File?
        get() = if (_当前焦点.value == 焦点.左) _左页面目录.value else _右页面目录.value

    fun 更新权限状态(新状态: Boolean) {
        _所有文件访问权限.value = 新状态
    }

    fun 获取显示文件列表(当前目录: File?): Array<File> {
        return 目录预设提供者.获取显示文件列表(当前目录)
    }

    fun 初始化(左页面: File, 右页面: File, 所有文件访问权限: Boolean) {
        _左页面目录.value = 左页面
        _右页面目录.value = 右页面
        _所有文件访问权限.value = 所有文件访问权限
    }

    fun 打开文件夹(文件夹: File) {
        if (_当前焦点.value == 焦点.左) {
            _左页面目录.value = 文件夹
        } else {
            _右页面目录.value = 文件夹
        }
    }

    fun 返回上一级目录() {
        if (_当前焦点.value == 焦点.左) {
            _左页面目录.value = _左页面目录.value?.parentFile ?: _左页面目录.value
        } else {
            _右页面目录.value = _右页面目录.value?.parentFile ?: _右页面目录.value
        }
    }

    fun 切换焦点(焦点值: 焦点) {
        _当前焦点.value = 焦点值
    }

    fun 获取目录(所属面板: 焦点? = null): MutableState<File?> {
        val 面板 = 所属面板 ?: _当前焦点.value
        return if (面板 == 焦点.左) _左页面目录 else _右页面目录
    }

    fun 所有文件访问权限状态(): Boolean {
        return _所有文件访问权限.value == true
    }

    fun 显示文件操作弹窗(状态: Boolean? = null, 文件: File? = null): MutableState<Boolean> {
        if (状态 != null) _文件操作弹窗状态.value = 状态
        if (文件 != null) _当前选中文件 = 文件
        return _文件操作弹窗状态
    }

    fun 显示文件操作异常弹窗(状态: Boolean? = null, 异常: String? = null): MutableState<Boolean> {
        if (状态 != null) _文件操作异常弹窗状态.value = 状态
        if (异常 != null) _异常信息.value = 异常
        return _文件操作异常弹窗状态
    }

    fun 文件复制() {
        try {
            val 源文件 = _当前选中文件!!
            val 目标目录 = if (_当前焦点.value == 焦点.左) _右页面目录.value!! else _左页面目录.value!!

            if (源文件.isDirectory) {
                val 目标文件 = File(目标目录, 源文件.name)
                文件夹复制JNI(源文件.absolutePath, 目标文件.absolutePath)
                _刷新版本.value++
            } else {
                val 目标文件 = File(目标目录, 源文件.name)
                源文件.copyTo(目标文件, overwrite = true)
                _刷新版本.value++
            }
        } catch (e: Exception) {
            显示文件操作异常弹窗(true, "复制失败: ${e.message}")
        }
        _文件操作弹窗状态.value = false
        _当前选中文件 = null
    }

    fun 文件移动(){
        try {
            val 源文件 = _当前选中文件!!
            val 目标目录 = if (_当前焦点.value == 焦点.左) _右页面目录.value!! else _左页面目录.value!!

            if (源文件.isDirectory && 是子目录(源文件, 目标目录)) {
                显示文件操作异常弹窗(true, "禁止将文件夹移动到自身子文件夹")
            } else {
                val 目标文件 = File(目标目录, 源文件.name)
                if (!源文件.renameTo(目标文件)) throw Exception("移动失败")
                _刷新版本.value++
            }
        } catch (e: Exception) {
            显示文件操作异常弹窗(true, "移动失败: ${e.message}")
        }
        _文件操作弹窗状态.value = false
        _当前选中文件 = null
    }

    fun 文件重命名(新名称: String){
        try {
            val 源文件 = _当前选中文件!!
            val 目标 = File(源文件.parentFile, 新名称)
            if (!源文件.renameTo(目标)) throw Exception("重命名失败")
            _刷新版本.value++
        } catch (e: Exception) {
            显示文件操作异常弹窗(true, "重命名失败: ${e.message}")
        }
        _文件操作弹窗状态.value = false
        _当前选中文件 = null
    }

    private fun 是子目录(源: File, 目标: File): Boolean {
        return try {
            val 源路径 = 源.canonicalPath
            val 目标路径 = 目标.canonicalPath
            目标路径 == 源路径 || 目标路径.startsWith("$源路径/")
        } catch (_: Exception) {
            false
        }
    }

    fun 文件删除(){
        try {
            if (!_当前选中文件!!.deleteRecursively()) throw Exception("删除失败")
            _刷新版本.value++
        } catch (e: Exception) {
            显示文件操作异常弹窗(true, "删除失败: ${e.message}")
        }
        _当前选中文件 = null
    }

    fun 文件新建(名称: String, 是文件夹: Boolean) {
        try {
            val 目标 = File(当前目录, 名称)
            if (是文件夹) {
                目标.mkdirs()
            } else {
                目标.createNewFile()
            }
            if (!目标.exists()) throw Exception("创建失败")
            _刷新版本.value++
        } catch (e: Exception) {
            显示文件操作异常弹窗(true, "新建失败: ${e.message}")
        }
    }

    external fun 文件夹复制JNI(当前选中文件夹: String, 目标文件夹: String)
}
