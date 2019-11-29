package com.csamson.activities

import android.content.res.ColorStateList
import android.os.Bundle
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
        val debtorOrCreditor: TextView = findViewById(R.id.debtorOrCreditor)
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextAmount: EditText = findViewById(R.id.editTextAmount)
        val editTextCurrency: EditText = findViewById(R.id.editTextCurrency)
        val addButton: Button = findViewById(R.id.addButton)
        var arraylistDebtorEntries: ArrayList<EntryDetails> = ArrayList()
        var arraylistCreditorEntries: ArrayList<EntryDetails> = ArrayList()
        var arraylistDebtorCurrencyCodes: ArrayList<String> = ArrayList()
        var arraylistCreditorCurrencyCodes: ArrayList<String> = ArrayList()
        val gson = Gson()
        val stringIntent = intent.getStringExtra("debtorOrCreditor")

        debtorOrCreditor.text = stringIntent
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
                var data = Data()

                var indexOfDuplicate = 0
                var hasDuplicate = false

                if (file.exists()) {
                    val inputStream: InputStream = file.inputStream()
                    jsonString = inputStream.bufferedReader().use { it.readText() }
                    data = gson.fromJson(jsonString, Data::class.java)
                    arraylistDebtorEntries = data.debtor
                    arraylistCreditorEntries = data.creditor
                    arraylistDebtorCurrencyCodes = data.debtorCurrencies
                    arraylistCreditorCurrencyCodes = data.creditorCurrencies
                }

                val dataData = when (stringIntent) {
                    "Add New Debtor" -> data.debtor
                    else -> data.creditor
                }

                for (debtor in dataData) {
                    if (debtor.name.toUpperCase() == editTextName.text.toString().toUpperCase() && debtor.currency.toUpperCase() == editTextCurrency.text.toString().toUpperCase()) {
                        indexOfDuplicate = dataData.indexOf(debtor)
                        hasDuplicate = true
                    }
                }

                val fullJson = Data(
                    arraylistDebtorEntries,
                    arraylistDebtorCurrencyCodes,
                    arraylistCreditorEntries,
                    arraylistCreditorCurrencyCodes
                )

                if (hasDuplicate) {
                    dataData[indexOfDuplicate].amount =
                        (Integer.parseInt(dataData[indexOfDuplicate].amount) + Integer.parseInt(
                            editTextAmount.text.toString()
                        )).toString()
                    dataData[indexOfDuplicate].amountWithCurrency =
                        dataData[indexOfDuplicate].amount + dataData[indexOfDuplicate].currency

                    if (stringIntent == "Add New Debtor") {
                        fullJson.debtor = dataData
                    } else {
                        fullJson.creditor = dataData
                    }
                } else {
                    val entryDetails = EntryDetails(
                        editTextName.text.toString(),
                        editTextAmount.text.toString(),
                        editTextCurrency.text.toString().toUpperCase()
                    )
                    if (debtorOrCreditor.text == "Add New Debtor") {
                        arraylistDebtorEntries.add(entryDetails)
                        arraylistDebtorCurrencyCodes.add(entryDetails.currency)
                    } else {
                        arraylistCreditorEntries.add(entryDetails)
                        arraylistCreditorCurrencyCodes.add(entryDetails.currency)
                    }
                }

                jsonString = gson.toJson(fullJson)

                val fos = FileOutputStream(file)
                fos.write(jsonString.toByteArray())
                finish()
            }
        }
    }
}
