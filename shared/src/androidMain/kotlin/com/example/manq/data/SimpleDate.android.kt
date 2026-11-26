package com.example.manq.data

import java.util.Calendar

actual fun today(): SimpleDate {
    val c = Calendar.getInstance()
    return SimpleDate(
        year = c.get(Calendar.YEAR),
        month = c.get(Calendar.MONTH) + 1,
        day = c.get(Calendar.DAY_OF_MONTH),
    )
}
