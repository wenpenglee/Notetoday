package com.wenpenglee.notetoday.data

import androidx.room3.ColumnTypeConverter
import kotlin.uuid.Uuid

class Converters {
    @ColumnTypeConverter
    fun fromUuid(uuid: Uuid): String {
        return uuid.toString()
    }

    @ColumnTypeConverter
    fun toUuid(value: String): Uuid {
        return Uuid.parse(value)
    }
}