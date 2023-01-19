package levilin.currencyconverter.model.remote

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

// Over 127 args will reach Kotlin limit and cause error.

data class CountryName(
    @SerializedName("AUD")
    val aUD: String,
    @SerializedName("BGN")
    val bGN: String,
    @SerializedName("BRL")
    val bRL: String,
    @SerializedName("CAD")
    val cAD: String,
    @SerializedName("CHF")
    val cHF: String,
    @SerializedName("CNY")
    val cNY: String,
    @SerializedName("CZK")
    val cZK: String,
    @SerializedName("DKK")
    val dKK: String,
    @SerializedName("EUR")
    val eUR: String,
    @SerializedName("GBP")
    val gBP: String,
    @SerializedName("HKD")
    val hKD: String,
    @SerializedName("HRK")
    val hRK: String,
    @SerializedName("HUF")
    val hUF: String,
    @SerializedName("IDR")
    val iDR: String,
    @SerializedName("INR")
    val iNR: String,
    @SerializedName("ISK")
    val iSK: String,
    @SerializedName("JPY")
    val jPY: String,
    @SerializedName("KRW")
    val kRW: String,
    @SerializedName("MXN")
    val mXN: String,
    @SerializedName("MYR")
    val mYR: String,
    @SerializedName("NOK")
    val nOK: String,
    @SerializedName("NZD")
    val nZD: String,
    @SerializedName("PHP")
    val pHP: String,
    @SerializedName("PLN")
    val pLN: String,
    @SerializedName("RON")
    val rON: String,
    @SerializedName("RUB")
    val rUB: String,
    @SerializedName("SEK")
    val sEK: String,
    @SerializedName("SGD")
    val sGD: String,
    @SerializedName("THB")
    val tHB: String,
    @SerializedName("TRY")
    val tRY: String,
    @SerializedName("TWD")
    val tWD: String,
    @SerializedName("USD")
    val uSD: String,
    @SerializedName("ZAR")
    val zAR: String
)

fun CountryName.toCurrencyCode(): CountryName {
    return CountryName(
        aUD = "AUD",
        bGN = "BGN",
        bRL = "BRL",
        cAD = "CAD",
        cHF = "CHF",
        cNY = "CNY",
        cZK = "CZK",
        dKK = "DKK",
        eUR = "EUR",
        gBP = "GBP",
        hKD = "HKD",
        hRK = "HRK",
        hUF = "HUF",
        iDR = "IDR",
        iNR = "INR",
        iSK = "ISK",
        jPY = "JPY",
        kRW = "KRW",
        mXN = "MXN",
        mYR = "MYR",
        nOK = "NOK",
        nZD = "NZD",
        pHP = "PHP",
        pLN = "PLN",
        rON = "RON",
        rUB = "RUB",
        sEK = "SEK",
        sGD = "SGD",
        tHB = "THB",
        tRY = "TRY",
        tWD = "TWD",
        uSD = "USD",
        zAR = "ZAR"
    )
}

fun CountryName.toOrder(): CountryName {
    return CountryName(
        aUD = "0",
        bGN = "1",
        bRL = "2",
        cAD = "3",
        cHF = "4",
        cNY = "5",
        cZK = "6",
        dKK = "7",
        eUR = "8",
        gBP = "9",
        hKD = "10",
        hRK = "11",
        hUF = "12",
        iDR = "13",
        iNR = "14",
        iSK = "15",
        jPY = "16",
        kRW = "17",
        mXN = "18",
        mYR = "19",
        nOK = "20",
        nZD = "21",
        pHP = "22",
        pLN = "23",
        rON = "24",
        rUB = "25",
        sEK = "26",
        sGD = "27",
        tHB = "28",
        tRY = "29",
        tWD = "30",
        uSD = "31",
        zAR = "32"
    )
}

fun CountryName(): CountryName {
    return CountryName(
        aUD = "AUD",
        bGN = "BGN",
        bRL = "BRL",
        cAD = "CAD",
        cHF = "CHF",
        cNY = "CNY",
        cZK = "CZK",
        dKK = "DKK",
        eUR = "EUR",
        gBP = "GBP",
        hKD = "HKD",
        hRK = "HRK",
        hUF = "HUF",
        iDR = "IDR",
        iNR = "INR",
        iSK = "ISK",
        jPY = "JPY",
        kRW = "KRW",
        mXN = "MXN",
        mYR = "MYR",
        nOK = "NOK",
        nZD = "NZD",
        pHP = "PHP",
        pLN = "PLN",
        rON = "RON",
        rUB = "RUB",
        sEK = "SEK",
        sGD = "SGD",
        tHB = "THB",
        tRY = "TRY",
        tWD = "TWD",
        uSD = "USD",
        zAR = "ZAR"
    )
}
