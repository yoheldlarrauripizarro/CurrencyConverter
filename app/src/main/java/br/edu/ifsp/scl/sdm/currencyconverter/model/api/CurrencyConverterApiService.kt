package br.edu.ifsp.scl.sdm.currencyconverter.model.api

import br.edu.ifsp.scl.sdm.currencyconverter.model.domain.ConversionResult
import br.edu.ifsp.scl.sdm.currencyconverter.model.domain.CurrencyList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyConverterApiService {
    @Headers(
        "x-rapidapi-host: currency-converter5.p.rapidapi.com",
        "x-rapidapi-key: 75b9fbc01cmsheba91af82aec3b3p11dd16jsnad215d3de1d4"
    )
    @GET("list")
    fun getCurrencies(): Call<CurrencyList>

    @Headers(
        "x-rapidapi-host: currency-converter5.p.rapidapi.com",
        "x-rapidapi-key: 75b9fbc01cmsheba91af82aec3b3p11dd16jsnad215d3de1d4"
    )
    @GET("convert")
    fun converter(@Query("from") from: String,
                  @Query("to") to: String,
                  @Query("amount") amount: String): Call<ConversionResult>
}