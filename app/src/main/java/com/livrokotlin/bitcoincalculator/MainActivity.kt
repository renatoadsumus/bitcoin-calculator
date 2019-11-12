package com.livrokotlin.bitcoincalculator



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.net.URL
import java.text.NumberFormat
import java.util.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

import kotlinx.android.synthetic.main.quotation_block.*
import kotlinx.android.synthetic.main.input_block.*
import kotlinx.android.synthetic.main.output_block.*


class MainActivity : AppCompatActivity() {

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    var quoteBitcoin:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getQuote()

        btn_calculate.setOnClickListener(){
            calculate()
        }
    }

    fun getQuote() {
        doAsync {
            val response = URL(API_URL).readText();

            quoteBitcoin = JSONObject(response).getJSONObject("ticker").getDouble("last")

            val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val quoteFormat = f.format(quoteBitcoin)

            uiThread {
                txt_quotation.text  = quoteFormat
            }
        }
    }

    fun calculate(){
        if(txt_amount.text.isEmpty()){
            txt_amount.error = "Fill in a value";
            return
        }

        val value = txt_amount.text.toString().replace(",", ".").toDouble();

        val result = if(quoteBitcoin > 0) value / quoteBitcoin else 0.0

        txt_qtd_bitcoins.text =  "%.8f".format(result);
    }
}
