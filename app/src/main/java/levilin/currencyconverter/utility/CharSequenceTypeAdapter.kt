package levilin.currencyconverter.utility

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.google.gson.TypeAdapter
import java.io.IOException

internal class CharSequenceTypeAdapter : TypeAdapter<CharSequence?>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: CharSequence?) {
        if (value == null) {
            out.nullValue()
        } else {
            // Assumes that value complies with CharSequence.toString() contract
            out.value(value.toString())
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): CharSequence? {
        return if (`in`.peek() === JsonToken.NULL) {
            // Skip the JSON null
            `in`.skipValue()
            null
        } else {
            `in`.nextString()
        }
    }
}