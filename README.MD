# 🌐 Network Watcher (ConnectivityMonitor)

## 📝 Overview

Network Watcher is a lightweight Android utility class that provides easy-to-use network connectivity monitoring. It helps developers track network connection changes and determine the current network type with minimal setup.

## ⭐ Features

- ⚡ Real-time network connectivity monitoring
- 🌍 Supports multiple network types:
  - 📶 WiFi
  - 📱 Cellular
  - 🖧 Ethernet
- 🛠️ Compatible with Android versions from API level 23 and above
- 🔔 Simple callback mechanism for network state changes
- 🔄 Handles both network availability and type changes

## 📥 Installation

Add the `ConnectivityMonitor.kt` file to your Android project's source directory.

### Gradle Dependency

Add the following dependency to your `build.gradle.kts` file:

```kotlin
implementation("io.github.kdroidfilter:netwatcher:0.1.0")
```

## 💻 Usage Example

```kotlin
class YourActivity : AppCompatActivity(), ConnectivityMonitor.ConnectivityListener {
    private lateinit var connectivityMonitor: ConnectivityMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityMonitor = ConnectivityMonitor(this, this)
        connectivityMonitor.startMonitoring()
    }

    // Implement the connectivity listener
    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: ConnectivityMonitor.NetworkType) {
        when {
            isConnected -> {
                when (networkType) {
                    ConnectivityMonitor.NetworkType.WIFI ->
                        println("Connected to WiFi")
                    ConnectivityMonitor.NetworkType.CELLULAR ->
                        println("Connected to Cellular Network")
                    ConnectivityMonitor.NetworkType.ETHERNET ->
                        println("Connected to Ethernet")
                    else -> println("Connected to Unknown Network")
                }
            }
            else -> println("No network connection")
        }
    }

    override fun onDestroy() {
        connectivityMonitor.stopMonitoring()
        super.onDestroy()
    }
}
```

## 🛠️ Methods

### 🔍 `startMonitoring()`

Begins monitoring network connectivity changes. Immediately triggers an initial callback with the current network state.

### 🛑 `stopMonitoring()`

Stops monitoring network connectivity and unregisters the network callback.

## 📡 Network Types

The library supports the following network types:

- 📶 `WIFI`: WiFi network connection
- 📱 `CELLULAR`: Mobile data network
- 🖧 `ETHERNET`: Ethernet network
- 🚫 `NONE`: No network connection
- ❓ `UNKNOWN`: Unrecognized network type

## 📋 Compatibility

- Minimum Android SDK: 23

## ⚖️ License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

