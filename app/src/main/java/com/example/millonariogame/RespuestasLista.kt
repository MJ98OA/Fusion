package com.example.millonariogame

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RespuestasLista (val result:List<Respuesta>): Parcelable

