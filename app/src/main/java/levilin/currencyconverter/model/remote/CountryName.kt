package levilin.currencyconverter.model.remote

import com.google.gson.annotations.SerializedName

// Over 127 args will reach Kotlin limit and cause error

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

fun CountryName(): CountryName {
    return CountryName(
        aUD = "Australian Dollar",
        bGN = "Bulgarian Lev",
        bRL = "Brazilian Real",
        cAD = "Canadian Dollar",
        cHF = "Swiss Franc",
        cNY = "Chinese Yuan",
        cZK = "Czech Republic Koruna",
        dKK = "Danish Krone",
        eUR = "Euro",
        gBP = "British Pound Sterling",
        hKD = "Hong Kong Dollar",
        hRK = "Croatian Kuna",
        hUF = "Hungarian Forint",
        iDR = "Indonesian Rupiah",
        iNR = "Indian Rupee",
        iSK = "Icelandic Króna",
        jPY = "Japanese Yen",
        kRW = "South Korean Won",
        mXN = "Mexican Peso",
        mYR = "Malaysian Ringgit",
        nOK = "Norwegian Krone",
        nZD = "New Zealand Dollar",
        pHP = "Philippine Peso",
        pLN = "Polish Zloty",
        rON = "Romanian Leu",
        rUB = "Russian Ruble",
        sEK = "Swedish Krona",
        sGD = "Singapore Dollar",
        tHB = "Thai Baht",
        tRY = "Turkish Lira",
        tWD = "New Taiwan Dollar",
        uSD = "United States Dollar",
        zAR = "South African Rand"
    )
}
