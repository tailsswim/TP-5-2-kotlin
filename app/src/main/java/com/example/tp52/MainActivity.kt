package com.example.networkapp

import android.R
import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    private var networkStatus: TextView? = null

    private var networkReceiver: NetworkReceiver? = null

    private var receiverRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkStatus = findViewById<TextView?>(R.id.networkStatus)

        networkReceiver = NetworkReceiver()

        // Afficher le statut initial
        updateNetworkStatus()

        // Enregistrer le BroadcastReceiver
        val filter = IntentFilter(
            ConnectivityManager.CONNECTIVITY_ACTION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                networkReceiver,
                filter,
                RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(networkReceiver, filter)
        }

        receiverRegistered = true
    }

    private fun updateNetworkStatus() {
        val cm =
            getSystemService(
                CONNECTIVITY_SERVICE
            ) as ConnectivityManager?

        var connected = false

        if (cm != null) {
            val network = cm.getActiveNetwork()

            if (network != null) {
                val capabilities =
                    cm.getNetworkCapabilities(network)

                connected = capabilities != null &&
                        capabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_INTERNET
                        ) &&
                        capabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_VALIDATED
                        )
            }
        }

        if (connected) {
            networkStatus!!.setText("Connecté à Internet")
        } else {
            networkStatus!!.setText("Non connecté à Internet")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (receiverRegistered) {
            unregisterReceiver(networkReceiver)
            receiverRegistered = false
        }
    }
}