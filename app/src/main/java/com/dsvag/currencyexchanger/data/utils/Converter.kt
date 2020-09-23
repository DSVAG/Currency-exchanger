package com.dsvag.currencyexchanger.data.utils

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converter {
    @TypeConverter
    fun decimalToString(decimal: BigDecimal): String = decimal.toString()

    @TypeConverter
    fun stringToDecimal(string: String): BigDecimal = string.toBigDecimalOrNull() ?: BigDecimal.ZERO

}