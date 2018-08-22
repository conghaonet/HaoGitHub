package com.app2m.github.network

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(val context: Context, val name: String, val default: T) : ReadWriteProperty<Any?, T> {
    val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    //利用with函数定义临时的命名空间
    @Suppress("UNCHECKED_CAST")
    private fun<T> findPreference(name: String, default: T) : T = with(prefs) {
        val res: Any = when(default) {
            is Long -> getLong(name, default)
            is Int -> getInt(name, default)
            is Float -> getFloat(name, default)
            is String -> getString(name, default)
            is Boolean -> getBoolean(name, default)
            else -> throw IllegalArgumentException("This type can not be saved into Preferences")
        }
        return res as T
    }

    private fun<T> putPreference(name: String, value: T) = with(prefs.edit()) {
        when(value) {
            is Long -> putLong(name, value)
            is Int -> putInt(name, value)
            is Float -> putFloat(name, value)
            is String -> putString(name, value)
            is Boolean -> putBoolean(name, value)
            else -> throw IllegalArgumentException("This type can not be saved into Preferences")
        }
    }.apply() //commit方法和apply方法都表示提交修改

    /**
     * 删除全部数据
     */
    fun clearPreference(){
        prefs.edit().clear().apply()
    }
}