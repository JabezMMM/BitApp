package com.bitapp

import android.app.Application
import com.bitapp.data.remote.BitRestApi
import com.bitapp.data.remote.BitSocketApi
import com.bitapp.data.repository.RestRepositoryImpl
import com.bitapp.data.repository.SocketRepositoryImpl
import com.bitapp.domain.RestRepository
import com.bitapp.domain.SocketRepository
import com.bitapp.presentation.ui.adapters.ItemClickListener
import com.bitapp.presentation.ui.adapters.SimpleListAdapter
import com.bitapp.presentation.ui.viewmodel.DetailViewModel
import com.bitapp.presentation.ui.viewmodel.ListViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// declare a module
val appModule = module {
    // Define single instance of AndroidLifecycle
    // Resolve constructor dependencies with get(), here we need an Application object
    single { createAndroidLifecycle(application = get()) }
    // Define single instance of OkHttpClient
    single { createOkHttpClient() }

    single { createRetrofitClient(okHttpClient = get(), converterFactory = MoshiConverterFactory.create()) }

    // Define single instance of Scarlet
    // Resolve constructor dependencies with get(), here we need an OkHttpClient, and a lifecycle
    single { createScarlet(okHttpClient = get(), lifecycle = get()) }

    // Define single instance of type BitAppDataSource
    // Resolve constructor dependencies with get(), here we need a BitAppApi
    //single<BitAppClient> { BitAppClientImpl(bitfinexApi = get()) }

    // Define single instance of type BitAppService (infered parameter in <>)
    // Resolve constructor dependencies with get(), here we need the BitAppApi
    single { get<Retrofit>().create(BitRestApi::class.java) }
    single<RestRepository> { RestRepositoryImpl(bitRestApi = get()) }

    single<SocketRepository> { SocketRepositoryImpl(bitSocketApi = get()) }


    // Define ViewModel and resolve constructor dependencies with get(),
    viewModel { ListViewModel(restRepository = get()) }
    viewModel { DetailViewModel(socketRepository = get()) }

    factory { (callbackListener: ItemClickListener<String>) -> SimpleListAdapter(callbackListener) }
}

private fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
}

private fun createAndroidLifecycle(application: Application): Lifecycle {
    return AndroidLifecycle.ofApplicationForeground(application)
}

private val jsonMoshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

// A Retrofit inspired WebSocket client for Kotlin, Java, and Android, that supports websockets.
private fun createScarlet(okHttpClient: OkHttpClient, lifecycle: Lifecycle): BitSocketApi {
    return Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(BitSocketApi.BASE_URI))
            .lifecycle(lifecycle)
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory(jsonMoshi))
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()
            .create()
}

private fun createRetrofitClient(okHttpClient: OkHttpClient, converterFactory: MoshiConverterFactory): Retrofit {
    return Retrofit.Builder()
            .baseUrl(BitRestApi.BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
}
