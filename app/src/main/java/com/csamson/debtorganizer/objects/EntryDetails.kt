package com.csamson.debtorganizer.objects

class EntryDetails(name: String, amount: String, currency: String) {
    var name: String = name
    var amount: String = amount
    var currency: String = currency
    var amountWithCurrency: String = "$amount $currency"

}




