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

    private var isOnDebtorsTabSelected: Boolean = true

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
        val file = File(getExternalFilesDir(null), "data.txt")
        val intent = Intent(this, AddNewActivity::class.java)
        val debtorsTab = true

        creditorsButton?.setOnClickListener { changeTab() }
        creditorsButton?.setTextColor(white)
        creditorsButton?.setBackgroundColor(gray)
        debtorsButton?.setTextColor(black)
        addNewButton.setOnClickListener {

            if (!debtorsButton?.isEnabled!!) {
                intent.putExtra("debtorOrCreditor", "Add New Debtor")
            } else {
                intent.putExtra("debtorOrCreditor", "Add New Creditor")
            }
            startActivity(intent)
        }

        if (file.exists()) {
            initializeFile(debtorsTab)
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
        val debtorsButton: Button? = findViewById(R.id.debtorsButton)
        val creditorsButton: Button? = findViewById(R.id.creditorsButton)
        val addNewButton: Button = findViewById(R.id.addNewButton)
        val gson = Gson()
        val jsonString: String = getValues()
        val data = gson.fromJson(jsonString, Data::class.java)
        val debtorArray: ArrayList<EntryDetails>? = data?.debtor
        val creditorArray: ArrayList<EntryDetails>? = data?.creditor
        var debtorCurrencyArray: ArrayList<String> = ArrayList()
        var creditorCurrencyArray: ArrayList<String> = ArrayList()
        list.layoutManager = LinearLayoutManager(this)

        if (data != null) {
            debtorCurrencyArray = data.debtorCurrencies
            creditorCurrencyArray = data.creditorCurrencies

        }

        val adapter: ArrayAdapter<String>

        val white = ContextCompat.getColor(this, R.color.white)
        val gray = ContextCompat.getColor(this, R.color.gray)
        val black = ContextCompat.getColor(this, R.color.black)
        isOnDebtorsTabSelected = !isOnDebtorsTabSelected
        debtorsButton?.isEnabled = !debtorsButton?.isEnabled!!
        creditorsButton?.isEnabled = !creditorsButton?.isEnabled!!
        if (isOnDebtorsTabSelected) {
            adapter = ArrayAdapter(
                this, android.R.layout.simple_spinner_item, debtorCurrencyArray.distinct()
            )
            creditorsButton.setTextColor(white)
            creditorsButton.setBackgroundColor(gray)
            debtorsButton.setTextColor(black)
            debtorsButton.setBackgroundColor(white)
            addNewButton.text = getString(R.string.addnewdebtor)
            list.adapter = ListAdapter(debtorArray, this)
            creditorsButton.setOnClickListener { changeTab() }
            if (data != null) {
                initializeFile(isOnDebtorsTabSelected)
            }

        } else {
            adapter = ArrayAdapter(
                this, android.R.layout.simple_spinner_item, creditorCurrencyArray.distinct()
            )
            debtorsButton.setTextColor(white)
            debtorsButton.setBackgroundColor(gray)
            creditorsButton.setTextColor(black)
            creditorsButton.setBackgroundColor(white)
            addNewButton.text = getString(R.string.addnewcreditor)
            list.adapter = ListAdapter(creditorArray, this)
            debtorsButton.setOnClickListener { changeTab() }
            if (data != null) {
                initializeFile(isOnDebtorsTabSelected)
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val sItems = findViewById<Spinner>(R.id.currencySpinner)
        sItems.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun initializeFile(debtorTab: Boolean) {

        val totalAmountView = findViewById<TextView>(R.id.totalAmount)
        val gson = Gson()
        val jsonString: String = getValues()
        val data = gson.fromJson(jsonString, Data::class.java)
        val debtorArray: ArrayList<EntryDetails>? = data.debtor
        val creditorArray: ArrayList<EntryDetails>? = data.creditor
        val listArray: ArrayList<EntryDetails>? = when (debtorTab) {
            true -> debtorArray
            false -> creditorArray
        }
        val amountArray: ArrayList<Int> = ArrayList()
        val debtorCurrencyArray: ArrayList<String> = data.debtorCurrencies
        val creditorCurrencyArray: ArrayList<String> = data.creditorCurrencies
        val currencyArray: ArrayList<String> = when (debtorTab) {
            true -> debtorCurrencyArray
            false -> creditorCurrencyArray
        }

        val adapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, currencyArray.distinct()
        )
        val sItems = findViewById<Spinner>(R.id.currencySpinner)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = ListAdapter(listArray, this)
        totalAmountView.text = ""
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sItems.adapter = adapter
        sItems.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selected = sItems.selectedItem.toString()
                amountArray.clear()
                if (listArray != null) {
                    for (debtorEntry in listArray) {
                        if (debtorEntry.currency == selected) {
                            amountArray.add(debtorEntry.amount.toInt())
                        }
                    }
                }
                totalAmountView.text = amountArray.sum().toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }
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
