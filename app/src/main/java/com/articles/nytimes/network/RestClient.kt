package com.articles.nytimes.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.articles.nytimes.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class RestClient {

    private val TAG = RestClient::class.java.canonicalName

    private var webService: ApiService? = null


    /**
     * @return
     */
    fun getClient(context:Context,isPullToRefreshRequest:Boolean): ApiService {

        try {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val client = getHttpClient(context,isPullToRefreshRequest)
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client).build()
            webService = retrofit.create(ApiService::class.java!!)

        } catch (e: Exception) {
            Log.e( TAG,
                "Error while creating  network client and message = " + e.message)
        }
        return webService!!
    }

    private fun getHttpClient(context:Context,isPullToRefreshRequest:Boolean): OkHttpClient {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)

        var twoHoursTime = 60 * 60 * 2

        val httpClient = OkHttpClient.Builder()
           .cache(myCache)
            .addInterceptor { chain ->


                var request = chain.request()
                    request = if (hasNetwork(context)!!){

                        if(isPullToRefreshRequest){

                            /**
                             *  request is Pull to Refresh then discard the data if its
                             *  more then 1 second and reload again
                             */

                            request.newBuilder().header(
                                "Cache-Control",
                                "public, max-age=" + 1).build()

                        } else {

                            /**
                             *  if request is not pull to refresh then discard the data
                             *  if its more then 2 hour
                             */

                            request.newBuilder().header(
                                "Cache-Control",
                                "public, max-age=" + {twoHoursTime}).build()

                        }
                   } else {
                        /**
                         * Store the date for Two hours if older then two horus then purge it
                         * */

                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + { twoHoursTime }).build()

                }

                chain.proceed(request)
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)


        val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)

        httpClient.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build()

                return chain.proceed(request)
            }
        })

        return httpClient.build()
    }

    fun hasNetwork(context: Context): Boolean? {

        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true

        return isConnected

    }

}