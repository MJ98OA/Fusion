package com.example.millonariogame

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
@Parcelize
data class ListaPreguntas (
    @SerialName("Id")
    val id:Int,
    @SerialName("Pregunta")
    val pregunta:String,
    @SerialName("OpcionA")
    val respuesta1:String,
    @SerialName("OpcionB")
    val respuesta2:String,
    @SerialName("OpcionC")
    val respuesta3:String,
    @SerialName("OpcionD")
    val respuesta4:String
    ) : Parcelable