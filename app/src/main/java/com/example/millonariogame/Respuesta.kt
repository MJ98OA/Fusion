package com.example.millonariogame

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
class Respuesta(
    @SerialName("id")
    val id: Int,
    @SerialName("solucion")
    val solucion: String
) : Parcelable