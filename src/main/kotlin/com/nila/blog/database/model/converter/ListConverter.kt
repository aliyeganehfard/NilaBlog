package com.nila.blog.database.model.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter


@Converter
class ListConverter : AttributeConverter<List<String?>?, String?> {
    override fun convertToDatabaseColumn(list: List<String?>?): String? {
        return if (list != null) java.lang.String.join(SPLIT_CHAR, list.toString()) else null
    }

    override fun convertToEntityAttribute(string: String?): List<String?>? {
        var string = string
        return if (string != null) {
            string = string.substring(1, string.length - 1)
            val strings = string.split(SPLIT_CHAR).toTypedArray()
            val res: MutableList<String?> = ArrayList()
            for (str in strings) {
                res.add(str.trim { it <= ' ' })
            }
            res
        } else {
            ArrayList()
        }
    }

    companion object {
        private const val SPLIT_CHAR = ","
    }
}