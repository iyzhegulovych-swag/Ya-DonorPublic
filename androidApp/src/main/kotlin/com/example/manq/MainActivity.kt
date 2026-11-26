package com.example.manq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.manq.storage.AndroidStorage

/**
 * Точка входу Android-версії. Логіка й інтерфейс лежать у модулі shared,
 * тут лишається Activity і передача Context у сховище.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidStorage.context = applicationContext
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}
