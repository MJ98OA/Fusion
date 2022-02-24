package com.example.millonariogame
import com.google.gson.Gson

data class Usuario(var nombre:String, var contrasenia:String){

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

}