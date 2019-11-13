package com.csamson.debtorganizer.objects

class Data {
    var debtor = ArrayList<EntryDetails>()
    var creditor = ArrayList<EntryDetails>()
    var debtorCurrencies = ArrayList<String>()
    var creditorCurrencies = ArrayList<String>()

    constructor() : super() {}

    constructor(
        debtor: ArrayList<EntryDetails>,
        debtorCurrencies: ArrayList<String>,
        creditor: ArrayList<EntryDetails>,
        creditorCurrencies: ArrayList<String>
    ) : super() {
        this.debtor = debtor
        this.creditor = creditor
        this.debtorCurrencies = debtorCurrencies
        this.creditorCurrencies = creditorCurrencies
    }
}

