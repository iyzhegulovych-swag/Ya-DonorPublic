package com.example.manq.network

/**
 * Завантажує текстовий файл за посиланням. Повертає null, якщо мережі
 * немає або сервер відповів помилкою.
 */
expect suspend fun httpGetText(url: String): String?
