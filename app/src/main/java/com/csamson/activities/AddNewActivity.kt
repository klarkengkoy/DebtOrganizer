package com.csamson.activities

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.csamson.debtorganizer.R
import com.csamson.debtorganizer.objects.*

import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class AddNewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new)
        var debtorOrCreditor: TextView = findViewById(R.id.debtorOrCreditor)
        var editTextName: EditText = findViewById(R.id.editTextName)
        var editTextAmount: EditText = findViewById(R.id.editTextAmount)
        var editTextCurrency: EditText = findViewById(R.id.editTextCurrency)
        var addButton: Button = findViewById(R.id.addButton)
        var arraylistDebtorEntries: ArrayList<EntryDetails> = ArrayList()
        var arraylistCreditorEntries: ArrayList<EntryDetails> = ArrayList()
        var arraylistDebtorCurrencyCodes: ArrayList<String> = ArrayList()
        var arraylistCreditorCurrencyCodes: ArrayList<String> = ArrayList()
        var gson = Gson()

        debtorOrCreditor.text = intent.getStringExtra("debtorOrCreditor")
        addButton.setOnClickListener {

            if (editTextAmount.text.toString() == "" || editTextCurrency.text.toString() == "" || editTextName.text.toString() == "") {
                Toast.makeText(
                    applicationContext,
                    "Cannot continue with null values.",
                    Toast.LENGTH_SHORT
                ).show()
                val colorStateList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
                if (editTextAmount.text.toString() == "") {
                    editTextAmount.backgroundTintList = colorStateList
                }
                if (editTextCurrency.text.toString() == "") {
                    editTextCurrency.backgroundTintList = colorStateList
                }
                if (editTextName.text.toString() == "") {
                    editTextName.backgroundTintList = colorStateList
                }
            } else {
                val file = File(getExternalFilesDir(null), "data.txt")
                var jsonString: String
                var data: Data

                if (file.exists()) {
                    val inputStream: InputStream = file.inputStream()
                    jsonString = inputStream.bufferedReader().use { it.readText() }
                    data = gson.fromJson(jsonString, Data::class.java)
                    arraylistDebtorEntries = data.debtor
                    arraylistCreditorEntries = data.creditor
                    arraylistDebtorCurrencyCodes = data.debtorCurrencies
                    arraylistCreditorCurrencyCodes = data.creditorCurrencies
                }

                var entryDetails = EntryDetails(
                    editTextName.text.toString(),
                    editTextAmount.text.toString(),
                    editTextCurrency.text.toString().toUpperCase()
                )
                if (debtorOrCreditor.text == "Add New Debtor") {
                    arraylistDebtorEntries.add(entryDetails)
                    arraylistDebtorCurrencyCodes.add(entryDetails.currency!!)
                } else {
                    arraylistCreditorEntries.add(entryDetails)
                    arraylistCreditorCurrencyCodes.add(entryDetails.currency!!)
                }
                var fullJson = Data(
                    arraylistDebtorEntries,
                    arraylistDebtorCurrencyCodes,
                    arraylistCreditorEntries,
                    arraylistCreditorCurrencyCodes
                )
                jsonString = gson.toJson(fullJson)
                Log.d("asd", jsonString)
                

                val fos = FileOutputStream(file)
                fos.write(jsonString.toByteArray())
                finish()
            }
        }
    }


}
