package com.example.millonariogame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.example.millonariogame.databinding.LoginMainBinding



class Login : AppCompatActivity() {
    private var nombre = false
    private var contra = false


    lateinit var binding: LoginMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contrasenia.doAfterTextChanged {

            it?.let {
                contra = it.contains("(?=.*[a-zA-Z])(?=.*[0-9])".toRegex()) && it.length >= 8
                activar(nombre,contra)
            }
        }

        binding.nombre.doAfterTextChanged {

            it?.let { nombre = it.length >= 3
                activar(nombre,contra)}
        }

        binding.boton

    }

    fun activar(nomb:Boolean,cont: Boolean) {

        if (cont && nomb) {
            binding.boton.visibility = View.VISIBLE
        } else
            binding.boton.visibility = View.GONE
    }



}