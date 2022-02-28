package com.example.millonariogame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random


class Login : AppCompatActivity() {
    private var nombre = false
    private var contra = false
    var llavecifrado=""




    lateinit var binding: LoginMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.contrasenia.doAfterTextChanged {

            it?.let {
                contra = it.contains("(?=.*[a-zA-Z])(?=.*[0-9])".toRegex()) && it.length >= 8
                activar(nombre, contra)
            }
        }

        binding.nombre.doAfterTextChanged {

            it?.let {
                nombre = it.length >= 3
                activar(nombre, contra)
            }
        }

        binding.boton.setOnClickListener {

            cargarPreferencias()?.let { llavecifrado=it }
            if(llavecifrado==""){
                llavecifrado=obtenerLlaveDeCifrado()
                guardarPreferencias(llavecifrado)
            }else{
                cargarPreferencias()?.let { llavecifrado=it }
            }

            var contraseniacifrada=cifrar(binding.contrasenia.text.toString(), llavecifrado)


            Toast.makeText(this, llavecifrado+"venga", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)

            val client = OkHttpClient()

            val request = Request.Builder()
            request.url("http://10.0.2.2:8083/creacionUsuario")
            val mediaType = "application/json; charset=utf-8".toMediaType()

            val requestBody = Usuario(
                binding.nombre.text.toString(),contraseniacifrada
            ).toString().toRequestBody(mediaType)
            request.post(requestBody)

            val call = client.newCall(request.build())
            call.enqueue(object : Callback {
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
                            //Toast.makeText(this@Login, body, Toast.LENGTH_SHORT).show()
                        }

                        val intent = Intent(this@Login, MainActivity::class.java)
                        intent.putExtra("TokenUser", body)
                        startActivity(intent)
                    }
                }

            })


        }

    }

    override fun onStop() {
        guardarPreferencias(llavecifrado)
        super.onStop()
    }



    private fun guardarPreferencias(string: String) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putString("llave", llavecifrado)
            commit()
        }
    }

    fun activar(nomb: Boolean, cont: Boolean) {

        if (cont && nomb) {
            binding.boton.visibility = View.VISIBLE
        } else
            binding.boton.visibility = View.GONE
    }





    fun cargarPreferencias(): String? {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString("llave", "")
    }

    fun obtenerLlaveDeCifrado(): String {
        var resultado = ""
        repeat(9) {
            val num = Random.nextInt(10)
            resultado += num
        }
        return resultado
    }

    fun cifrar(textoEnString: String, llaveEnString: String): String {
        println("Voy a cifrar: $textoEnString")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getKey(llaveEnString))
        val textCifrado = android.util.Base64.encodeToString(
            cipher.doFinal(textoEnString.toByteArray(Charsets.UTF_8)),
            android.util.Base64.URL_SAFE
        )

        println("He obtenido $textCifrado")
        return textCifrado
    }


    fun getKey(llaveEnString: String): SecretKeySpec {
        var llaveUtf8 = llaveEnString.toByteArray(Charsets.UTF_8)
        val sha = MessageDigest.getInstance("SHA-1")
        llaveUtf8 = sha.digest(llaveUtf8)
        llaveUtf8 = llaveUtf8.copyOf(16)
        return SecretKeySpec(llaveUtf8, "AES")
    }



}