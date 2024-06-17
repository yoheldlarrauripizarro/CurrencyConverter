package br.edu.ifsp.scl.sdm.currencyconverter.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import br.edu.ifsp.scl.sdm.currencyconverter.model.api.CurrencyConverterApiClient
import br.edu.ifsp.scl.sdm.currencyconverter.model.livedata.CurrencyConverterLiveData
import java.net.HttpURLConnection.HTTP_OK

class ConvertService : Service() {
    private val convertServiceBinder = ConvertServiceBinder()
    private lateinit var handler : ConvertServiceHandler

    companion object{
        const val FROM_PARAMETER = "from"
        const val TO_PARAMETER = "to"
        const val AMOUNT_PARAMETER = "amount"
    }

    inner class ConvertServiceBinder: Binder(){
        fun getConvertService() = this@ConvertService
    }

    private inner class ConvertServiceHandler(looper: Looper): Handler(looper){
        override fun handleMessage(msg: Message) {
            with(msg.data){
                CurrencyConverterApiClient.service.converter(
                    from = getString(FROM_PARAMETER,""),
                    to = getString(TO_PARAMETER,""),
                    amount = getString(AMOUNT_PARAMETER,"")
                ).execute().also {response ->
                    if(response.code() == HTTP_OK){
                        response.body()?.let { conversionResult ->
                            CurrencyConverterLiveData.conversionResultLiveData.
                            postValue(conversionResult)
                        }
                    }
                }
            }
        }
    }

    fun convert(from:String, to:String, amount:String){
        HandlerThread(this.javaClass.simpleName).apply {
            start()
            handler = ConvertServiceHandler(looper)
        }
        handler.obtainMessage().apply {
            data.putString(FROM_PARAMETER,from)
            data.putString(TO_PARAMETER,to)
            data.putString(AMOUNT_PARAMETER,amount)
            handler.sendMessage(this)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.v(this.javaClass.simpleName,"Service started.")
        return convertServiceBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.v(this.javaClass.simpleName,"Service done.")
        return super.onUnbind(intent)
    }
}