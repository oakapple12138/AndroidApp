#include <string>
#include <jni.h>
#include <android/log.h>
#include "includes/oak.hpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_oakapple_myapplication_viewmodel__06587_04ef6_07ba1_07406_05668_063a7_05236_05668__06587_04ef6_05939_0590d_05236JNI(
        JNIEnv *env, jobject thiz, jstring 当前选中文件夹, jstring 目标文件夹)
{
    const char *源路径 = env->GetStringUTFChars(当前选中文件夹, nullptr);
    const char *目标路径 = env->GetStringUTFChars(目标文件夹, nullptr);

    oak::文件夹复制(源路径, 目标路径);

    env->ReleaseStringUTFChars(当前选中文件夹, 源路径);
    env->ReleaseStringUTFChars(目标文件夹, 目标路径);
}