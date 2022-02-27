package com.example.millonariogame

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.millonariogame.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    //companion Object?
    companion object {

        var aciertos = 0
        var totales = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val initialData = intent.getStringExtra("TokenUser")
        initialData?.let {
            llamada(initialData)
        }


    }

    private fun llamada(token: String) {

        binding.pbDownloading.visibility = View.VISIBLE

        reseteoColorBotones()
        val client = OkHttpClient()

        val request = Request.Builder()
        request.url("http://10.0.2.2:8083/todasRespuestas")


        val call = client.newCall(request.build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                println(e.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@MainActivity, "fallo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val body = responseBody.string()
                    println(body)
                    val gson = GsonBuilder().create()

                    val listapreguntas: List<Respuesta> =
                        gson.fromJson(body, Array<Respuesta>::class.java).toList()

                    CoroutineScope(Dispatchers.Main).launch {

                        val client = OkHttpClient()

                        val request = Request.Builder()
                        request.url("http://10.0.2.2:8083/getPreguntaRandom/$token")


                        val call = client.newCall(request.build())
                        call.enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {

                                println(e.toString())
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(this@MainActivity, "fallo", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }


                            override fun onResponse(call: Call, response: Response) {
                                response.body?.let { responseBody ->
                                    val body = responseBody.string()
                                    println(body)
                                    val gson = Gson()

                                    val pregunta = gson.fromJson(body, Pregunta::class.java)
                                    val respuesta = listapreguntas[pregunta.id].solucion
                                    CoroutineScope(Dispatchers.Main).launch {

                                        if (pregunta.id == 10) {
                                            binding.pbDownloading.visibility = View.VISIBLE
                                            binding.texto.text =
                                                "Ya has contestado todas las preguntas disponibles gracias por jugar"
                                            binding.buttonA.visibility = View.GONE
                                            binding.buttonB.visibility = View.GONE
                                            binding.buttonC.visibility = View.GONE
                                            binding.buttonD.visibility = View.GONE
                                        } else {
                                            binding.pbDownloading.visibility = View.GONE

                                            binding.texto.setText(pregunta.pregunta)
                                            binding.buttonA.setText(pregunta.respuesta1)
                                            binding.buttonB.setText(pregunta.respuesta2)
                                            binding.buttonC.setText(pregunta.respuesta3)
                                            binding.buttonD.setText(pregunta.respuesta4)

                                            binding.buttonA.setOnClickListener {
                                                binding.buttonA.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#995FFF"
                                                    )
                                                )
                                                binding.buttonB.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                binding.buttonC.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                binding.buttonD.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                activarEnviar(
                                                    binding.buttonA.text.toString(),
                                                    respuesta,
                                                    token
                                                )

                                            }

                                            binding.buttonB.setOnClickListener {
                                                binding.buttonB.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#995FFF"
                                                    )
                                                )
                                                binding.buttonA.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                binding.buttonC.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                binding.buttonD.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                activarEnviar(
                                                    binding.buttonB.text.toString(),
                                                    respuesta,
                                                    token
                                                )
                                            }

                                            binding.buttonC.setOnClickListener {
                                                binding.buttonC.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#995FFF"
                                                    )
                                                )
                                                binding.buttonA.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                binding.buttonB.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                binding.buttonD.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                activarEnviar(
                                                    binding.buttonC.text.toString(),
                                                    respuesta,
                                                    token
                                                )
                                            }

                                            binding.buttonD.setOnClickListener {
                                                binding.buttonD.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#995FFF"
                                                    )
                                                )
                                                binding.buttonA.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                binding.buttonB.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                binding.buttonC.setBackgroundColor(
                                                    Color.parseColor(
                                                        "#4719B2"
                                                    )
                                                )
                                                activarEnviar(
                                                    binding.buttonD.text.toString(),
                                                    respuesta,
                                                    token
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }
        })

    }

    fun activarEnviar(resSeleccionada: String, solucion: String, token: String) {

        binding.enviar.isVisible = true
        var mirespuesta = resSeleccionada
        var misolucion = solucion

        binding.enviar.setOnClickListener {
            if (mirespuesta == misolucion) {
                Toast.makeText(this@MainActivity, "Has hacertado", Toast.LENGTH_SHORT).show()
                aciertos++
                totales++
                binding.preguntasAciertos.text = aciertos.toString()
                binding.preguntastotales.text = totales.toString()
                binding.enviar.visibility = View.GONE

            } else {

                totales++
                binding.preguntasAciertos.text = aciertos.toString()
                binding.preguntastotales.text = totales.toString()
                Toast.makeText(
                    this@MainActivity,
                    "Has fallado" + resSeleccionada + solucion,
                    Toast.LENGTH_SHORT
                ).show()
                binding.enviar.visibility = View.GONE
            }


            llamada(token)
        }


    }

    fun reseteoColorBotones() {
        binding.buttonA.setBackgroundColor(Color.parseColor("#4719B2"))
        binding.buttonB.setBackgroundColor(Color.parseColor("#4719B2"))
        binding.buttonC.setBackgroundColor(Color.parseColor("#4719B2"))
        binding.buttonD.setBackgroundColor(Color.parseColor("#4719B2"))
    }


}
