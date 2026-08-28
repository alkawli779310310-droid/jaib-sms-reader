package com.jaibreader.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var senderInput: EditText
    private lateinit var cutoffInput: EditText
    private lateinit var webhookInput: EditText
    private lateinit var testInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        senderInput = findViewById(R.id.senderInput)
        cutoffInput = findViewById(R.id.cutoffInput)
        webhookInput = findViewById(R.id.webhookInput)
        testInput = findViewById(R.id.testInput)
        val resultView: TextView = findViewById(R.id.resultView)
        val saveButton: Button = findViewById(R.id.saveButton)
        val testButton: Button = findViewById(R.id.testButton)
        val permissionButton: Button = findViewById(R.id.permissionButton)

        senderInput.setText(SettingsStore.getSenderFilter(this))
        cutoffInput.setText(SettingsStore.getCutoffWord(this))
        webhookInput.setText(SettingsStore.getSheetWebhookUrl(this))

        saveButton.setOnClickListener {
            SettingsStore.setSenderFilter(this, senderInput.text.toString().trim())
            SettingsStore.setCutoffWord(this, cutoffInput.text.toString().trim())
            SettingsStore.setSheetWebhookUrl(this, webhookInput.text.toString().trim())
            Toast.makeText(this, "تم الحفظ", Toast.LENGTH_SHORT).show()
        }

        testButton.setOnClickListener {
            val sample = testInput.text.toString()
            val parsed = AmountParser.parse(sample, cutoffInput.text.toString().trim())
            resultView.text = if (parsed != null) {
                "المبلغ: ${parsed.amount}\nالعملة: ${parsed.currency}"
            } else {
                "لم يتم العثور على مبلغ في هذا النص"
            }
        }

        permissionButton.setOnClickListener {
            requestSmsPermissions()
        }

        requestSmsPermissions()
    }

    private fun requestSmsPermissions() {
        val needed = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) needed.add(Manifest.permission.RECEIVE_SMS)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) needed.add(Manifest.permission.READ_SMS)

        if (needed.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, needed.toTypedArray(), 100)
        }
    }
}
