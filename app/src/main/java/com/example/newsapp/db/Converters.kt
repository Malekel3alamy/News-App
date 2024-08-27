package com.example.newsapp.db

import androidx.room.TypeConverter


class Converters {

    @TypeConverter
    fun fromString(source: String) : List<String>{
        return listOf(source)
    }

    @TypeConverter
    fun toString(stringList:List<String>) : String{

        return stringList[0]
    }

    @TypeConverter
    fun fromAnyString(source: String) : Any{
        return Any()
    }

    @TypeConverter
    fun toAnyString(any:Any) : String{

        return ""
    }




}