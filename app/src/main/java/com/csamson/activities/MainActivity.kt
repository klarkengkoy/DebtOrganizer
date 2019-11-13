package com.csamson.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.csamson.debtorganizer.R
import com.csamson.debtorganizer.objects.Data
import com.csamson.debtorganizer.objects.EntryDetails
import com.csamson.debtorganizer.objects.ListAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.main.*
import java.io.File
import java.io.InputStream
import android.widget.AdapterView.OnItemSelectedListener

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val allPermissions = 1

    var isOnDebtorsTabSelected: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        setContentView(R.layout.main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val debtorsButton: Button? = findViewById(R.id.debtorsButton)
        val creditorsButton: Button? = findViewById(R.id.creditorsButton)
        val addNewButton: Button = findViewById(R.id.addNewButton)
        val white = ContextCompat.getColor(this, R.color.white)
        val gray = ContextCompat.getColor(this, R.color.gray)
        val black = ContextCompat.getColor(this, R.color.black)
        var file = File(getExternalFilesDir(null), "data.txt")
        var totalAmountView = findViewById<TextView>(R.id.totalAmount)

        if (file.exists()) {
            var gson = Gson()
            var jsonString: String = getValues()
            var data = gson.fromJson(jsonString, Data::class.java)
            var debtorArray: ArrayList<EntryDetails>? = data.debtor
            var debtorCurrencyArray : ArrayList<String> = data.debtorCurrencies
            val adapter = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, debtorCurrencyArray
            )
            list.layoutManager = LinearLayoutManager(this)
            list.adapter = ListAdapter(debtorArray, this)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            val sItems = findViewById<Spinner>(R.id.currencySpinner)
            sItems.adapter = adapter
            sItems.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    // your code here
                    val selected = sItems.selectedItem.toString()
                    if (selected == "what ever the option was") {
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    // your code here
                }

            }
        }


        creditorsButton?.setOnClickListener { changeTab() }
        creditorsButton?.setTextColor(white)
        creditorsButton?.setBackgroundColor(gray)
        debtorsButton?.setTextColor(black)
        addNewButton.setOnClickListener {
            val intent = Intent(this, AddNewActivity::class.java)
            if (!debtorsButton?.isEnabled!!) {
                intent.putExtra("debtorOrCreditor", "Add New Debtor")
            } else {
                intent.putExtra("debtorOrCreditor", "Add New Creditor")
            }
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        changeTab()
        changeTab()
    }

    private fun getValues(): String {
        val file = File(getExternalFilesDir(null), "data.txt")

        return if (file.exists()) {
            val inputStream: InputStream = file.inputStream()
            inputStream.bufferedReader().use { it.readText() }
        } else {
            ""
        }
    }

    private fun changeTab() {
        var debtorsButton: Button? = findViewById(R.id.debtorsButton)
        var creditorsButton: Button? = findViewById(R.id.creditorsButton)
        var addNewButton: Button = findViewById(R.id.addNewButton)
        var gson = Gson()
        var jsonString: String = getValues()
        var data = gson.fromJson(jsonString, Data::class.java)
        var debtorArray : ArrayList<EntryDetails>? = data?.debtor
        var creditorArray : ArrayList<EntryDetails>? = data?.creditor
        list.layoutManager = LinearLayoutManager(this)


        var debtorCurrencyArray : ArrayList<String> = data.debtorCurrencies
        var creditorCurrencyArray : ArrayList<String> = data.creditorCurrencies


        var adapter: ArrayAdapter<String>



        val white = ContextCompat.getColor(this, R.color.white)
        val gray = ContextCompat.getColor(this, R.color.gray)
        val black = ContextCompat.getColor(this, R.color.black)
        isOnDebtorsTabSelected = !isOnDebtorsTabSelected
        debtorsButton?.isEnabled = !debtorsButton?.isEnabled!!
        creditorsButton?.isEnabled = !creditorsButton?.isEnabled!!
        if (isOnDebtorsTabSelected) {
            adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, debtorCurrencyArray
            )
            creditorsButton.setTextColor(white)
            creditorsButton.setBackgroundColor(gray)
            debtorsButton.setTextColor(black)
            debtorsButton.setBackgroundColor(white)
            addNewButton.text = "ADD NEW DEBTOR"
            list.adapter = ListAdapter(debtorArray, this)
            creditorsButton.setOnClickListener { changeTab() }
        } else {
            adapter = ArrayAdapter(
                this, android.R.layout.simple_spinner_item, creditorCurrencyArray
            )
            debtorsButton.setTextColor(white)
            debtorsButton.setBackgroundColor(gray)
            creditorsButton.setTextColor(black)
            creditorsButton.setBackgroundColor(white)
            addNewButton.text = "ADD NEW CREDITOR"
            list.adapter = ListAdapter(creditorArray, this)
            debtorsButton.setOnClickListener { changeTab() }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val sItems = findViewById<Spinner>(R.id.currencySpinner)
        sItems.adapter = adapter
    }


    private fun requestPermissions(): Boolean {
        if (!hasPermissions(this, *permissions)) {
            ActivityCompat.requestPermissions(this, permissions, allPermissions)
            return false
        }
        return true
    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}
