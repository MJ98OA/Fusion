package com.example.millonariogame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.millonariogame.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {



    lateinit var binding:ActivityMainBinding


    lateinit var sol:String

    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        llamada()



    }

    private fun llamada() {
        val client = OkHttpClient()

        val request = Request.Builder()
        request.url("http://10.0.2.2:8081/getPreguntaRandom")


        val call = client.newCall(request.build())
        call.enqueue( object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                println(e.toString())
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(this@MainActivity, "fallo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val body = responseBody.string()
                    println(body)
                    val gson = Gson()

                    val planet = gson.fromJson(body, ListaPreguntas::class.java)

                    CoroutineScope(Dispatchers.Main).launch {

                        Toast.makeText(this@MainActivity, "$planet", Toast.LENGTH_SHORT).show()
                        binding.buttonA.setText(planet.respuesta1)
                        binding.buttonB.setText(planet.respuesta2)
                        binding.buttonC.setText(planet.respuesta3)
                        binding.buttonD.setText(planet.respuesta4)
                        binding.texto.setText(planet.pregunta)
                        binding.buttonA.setOnFocusChangeListener { view, hasfocus ->
                            if(hasfocus){
                                binding.buttonA.setBackgroundColor(resources.getColor(R.color.black))
                            }
                        }

                    }

                }

            }

        })
    }


}
