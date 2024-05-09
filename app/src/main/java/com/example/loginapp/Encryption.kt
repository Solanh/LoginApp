package com.example.loginapp

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object KeystoreHelper {
    private const val KEY_ALIAS = "your_key_alias"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_SIZE = 256
    private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

    fun getSecretKey(): SecretKey {
        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        // Check if the key exists
        keyStore.getKey(KEY_ALIAS, null)?.let {
            return it as SecretKey
        }

        // If not, generate a new one
        val keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, ANDROID_KEYSTORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            setKeySize(KEY_SIZE)
        }.build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }
}

object CryptoUtils {
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BIT = 128
    private const val IV_LENGTH_BYTE = 12

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptData(data: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encryption = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        val ivAndCipherText = ByteArray(iv.size + encryption.size)
        iv.copyInto(ivAndCipherText, 0, 0, iv.size)
        encryption.copyInto(ivAndCipherText, iv.size, 0, encryption.size)
        return Base64.getEncoder().encodeToString(ivAndCipherText)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptData(encryptedData: String, secretKey: SecretKey): String {
        val ivAndCipherText = Base64.getDecoder().decode(encryptedData)
        val iv = ivAndCipherText.copyOfRange(0, IV_LENGTH_BYTE)
        val cipherText = ivAndCipherText.copyOfRange(IV_LENGTH_BYTE, ivAndCipherText.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val parameterSpec = GCMParameterSpec(TAG_LENGTH_BIT, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec)
        val plaintext = cipher.doFinal(cipherText)
        return String(plaintext, Charsets.UTF_8)
    }
}
