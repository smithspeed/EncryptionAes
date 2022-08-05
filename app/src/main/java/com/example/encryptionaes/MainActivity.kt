package com.example.encryptionaes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import org.json.JSONObject
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    var secretKey = "30d8b68783c2e25a6e9d4f3b54518745"
    val tagName = "mySys*"

    val newIv = "1234567891234567";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //encrypt("{this is my name amit kumar}")

        decrypt("8vfqLgyW+4JsHuwkoQh+cT9GsoHGWXEmAIzyHka+hi4miGVdCxOegww85EVdJY5O")
    }

    private fun encrypt(strToEncrypt: String){

        val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)

        val keygen = KeyGenerator.getInstance("AES")

        keygen.init(256)

        val keySpec = SecretKeySpec(secretKey.toByteArray(), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")

        cipher.init(Cipher.ENCRYPT_MODE, keySpec)

        val cipherText = cipher.doFinal(plainText)

        val finalOutput = Base64.encodeToString((cipher.iv+cipherText), Base64.NO_WRAP)

        val encyptedItems = JSONObject()

        encyptedItems.put("iv",cipher.iv)
        encyptedItems.put("cipherText", cipherText)
        encyptedItems.put("ivSUMcipherText", (cipher.iv+cipherText))
        encyptedItems.put("afterBase64",finalOutput)

        Log.d(tagName, "encrypt: $encyptedItems")

        //cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(newIv.toByteArray()))

        //val decrypt = cipher.doFinal(cipherText)

        //val decodeOutput = decrypt.toString(Charsets.UTF_8)

        //Log.d(tagName, "decryptFinal: $decodeOutput")

    }

    private fun decrypt(encryptedText:String){

        val decodeText = Base64.decode(encryptedText, Base64.NO_WRAP)

        val keygen = KeyGenerator.getInstance("AES")

        keygen.init(256)

        val keySpec = SecretKeySpec(secretKey.toByteArray(), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")

        val lengthOfIv = cipher.blockSize

        val getIvs = IvParameterSpec(decodeText.copyOfRange(0,16))
        //val getIvs = IvParameterSpec(newIv.toByteArray())

        //Log.d(tagName, "decryptIv: ${getIvs.iv}")

        cipher.init(Cipher.DECRYPT_MODE, keySpec, getIvs)

        val remaingText = decodeText.copyOfRange(16,decodeText.size - 16)

        //Log.d(tagName, "remaingText: $remaingText")

        val decrypt = cipher.doFinal(decodeText)

        val decodeOutput = decrypt.toString(Charsets.US_ASCII).removeRange(0,lengthOfIv)

        Log.d(tagName, "output: $decodeOutput")



    }
}