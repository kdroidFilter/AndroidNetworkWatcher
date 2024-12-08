package com.kdroid.netwatcher

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

/**
 * A utility class that monitors network connectivity changes and notifies the registered listener.
 *
 * @constructor Creates an instance of the ConnectivityMonitor with a given context and listener.
 * @param context The context used to access system services.
 * @param listener The listener that receives updates on network connectivity changes.
 */
class ConnectivityMonitor(
    context: Context,
    private val listener: ConnectivityListener
) {

    interface ConnectivityListener {
        fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetworkType)
    }

    enum class NetworkType {
        WIFI, CELLULAR, ETHERNET, NONE, UNKNOWN
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    private var lastNetworkType: NetworkType = NetworkType.NONE

    /**
     * Starts monitoring network connectivity changes.
     */
    fun startMonitoring() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val networkType = getNetworkType(network)
                if (networkType != lastNetworkType) {
                    listener.onNetworkConnectionChanged(true, networkType)
                    lastNetworkType = networkType
                }
            }

            override fun onLost(network: Network) {
                listener.onNetworkConnectionChanged(false, NetworkType.NONE)
                lastNetworkType = NetworkType.NONE
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                val networkType = getNetworkType(network)
                if (networkType != lastNetworkType) {
                    listener.onNetworkConnectionChanged(true, networkType)
                    lastNetworkType = networkType
                }
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)

        // Notify initial network state
        val initialNetworkType = getNetworkType()
        val isConnected = initialNetworkType != NetworkType.NONE && initialNetworkType != NetworkType.UNKNOWN
        listener.onNetworkConnectionChanged(isConnected, initialNetworkType)
        lastNetworkType = initialNetworkType
    }

    /**
     * Stops monitoring network connectivity changes.
     */
    fun stopMonitoring() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
            networkCallback = null
        }
    }

    /**
     * Determines the current network type for a given network.
     */
    private fun getNetworkType(network: Network? = null): NetworkType {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = network ?: connectivityManager.activeNetwork ?: return NetworkType.NONE
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                ?: return NetworkType.UNKNOWN

            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
                else -> NetworkType.UNKNOWN
            }
        } else {
            @Suppress("DEPRECATION")
            val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return NetworkType.NONE

            when (activeNetworkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
                ConnectivityManager.TYPE_MOBILE -> NetworkType.CELLULAR
                ConnectivityManager.TYPE_ETHERNET -> NetworkType.ETHERNET
                else -> NetworkType.UNKNOWN
            }
        }
    }
}

