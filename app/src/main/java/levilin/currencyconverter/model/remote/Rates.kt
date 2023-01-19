package levilin.currencyconverter.model.remote


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

// Over 127 args will reach Kotlin limit and cause error.

data class Rates(
    @SerializedName("AUD")
    val aUD: Double,
    @SerializedName("BGN")
    val bGN: Double,
    @SerializedName("BRL")
    val bRL: Double,
    @SerializedName("CAD")
    val cAD: Double,
    @SerializedName("CHF")
    val cHF: Double,
    @SerializedName("CNY")
    val cNY: Double,
    @SerializedName("CZK")
    val cZK: Double,
    @SerializedName("DKK")
    val dKK: Double,
    @SerializedName("EUR")
    val eUR: Double,
    @SerializedName("GBP")
    val gBP: Double,
    @SerializedName("HKD")
    val hKD: Double,
    @SerializedName("HRK")
    val hRK: Double,
    @SerializedName("HUF")
    val hUF: Double,
    @SerializedName("IDR")
    val iDR: Double,
    @SerializedName("INR")
    val iNR: Double,
    @SerializedName("ISK")
    val iSK: Double,
    @SerializedName("JPY")
    val jPY: Double,
    @SerializedName("KRW")
    val kRW: Double,
    @SerializedName("MXN")
    val mXN: Double,
    @SerializedName("MYR")
    val mYR: Double,
    @SerializedName("NOK")
    val nOK: Double,
    @SerializedName("NZD")
    val nZD: Double,
    @SerializedName("PHP")
    val pHP: Double,
    @SerializedName("PLN")
    val pLN: Double,
    @SerializedName("RON")
    val rON: Double,
    @SerializedName("RUB")
    val rUB: Double,
    @SerializedName("SEK")
    val sEK: Double,
    @SerializedName("SGD")
    val sGD: Double,
    @SerializedName("THB")
    val tHB: Double,
    @SerializedName("TRY")
    val tRY: Double,
    @SerializedName("TWD")
    val tWD: Double,
    @SerializedName("USD")
    val uSD: Double,
    @SerializedName("ZAR")
    val zAR: Double
)

fun Rates.toCurrencyCode(): CountryName {
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

fun Rates.toOrder(): CountryName {
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

fun Rates(): Rates {
    return Rates(
        aUD = 0.00,
        bGN = 0.00,
        bRL = 0.00,
        cAD = 0.00,
        cHF = 0.00,
        cNY = 0.00,
        cZK = 0.00,
        dKK = 0.00,
        eUR = 0.00,
        gBP = 0.00,
        hKD = 0.00,
        hRK = 0.00,
        hUF = 0.00,
        iDR = 0.00,
        iNR = 0.00,
        iSK = 0.00,
        jPY = 0.00,
        kRW = 0.00,
        mXN = 0.00,
        mYR = 0.00,
        nOK = 0.00,
        nZD = 0.00,
        pHP = 0.00,
        pLN = 0.00,
        rON = 0.00,
        rUB = 0.00,
        sEK = 0.00,
        sGD = 0.00,
        tHB = 0.00,
        tRY = 0.00,
        tWD = 0.00,
        uSD = 0.00,
        zAR = 0.00
    )
}