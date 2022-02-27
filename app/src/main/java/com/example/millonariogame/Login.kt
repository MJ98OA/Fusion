package com.example.millonariogame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.doOnAttach
import androidx.core.widget.doAfterTextChanged
import com.example.millonariogame.databinding.LoginMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.random.Random


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

        binding.boton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            val client = OkHttpClient()

            val request = Request.Builder()
            request.url("http://10.0.2.2:8083/creacionUsuario")
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = Usuario(binding.nombre.text.toString(), binding.contrasenia.text.toString()).toString().toRequestBody(mediaType)
            request.post(requestBody)

            val call = client.newCall(request.build())
            call.enqueue( object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@Login, "Algo ha ido mal", Toast.LENGTH_SHORT).show()
                    }

                }
                override fun onResponse(call: Call, response: Response) {

                    response.body?.let { responseBody ->
                        val body = responseBody.string()
                        println(body)


                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(this@Login, body, Toast.LENGTH_SHORT).show()

                        }
                        val intent = Intent(this@Login, MainActivity::class.java)
                        intent.putExtra("TokenUser",body)
                        startActivity(intent)
                        }
                    }

            })


        }

    }

    fun activar(nomb:Boolean,cont: Boolean) {

        if (cont && nomb) {
            binding.boton.visibility = View.VISIBLE
        } else
            binding.boton.visibility = View.GONE
    }



}