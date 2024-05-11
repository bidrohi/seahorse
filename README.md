# Seahorse : String resources from local or remote sources

[![Kotlin Alpha](https://kotl.in/badges/alpha.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-CC_BY_SA_4.0-blue.svg)](https://github.com/bidrohi/seahorse/blob/master/LICENSE.md)

![badge-jvm](http://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-watchos](http://img.shields.io/badge/platform-watchos-C0C0C0.svg?style=flat)
![badge-tvos](http://img.shields.io/badge/platform-tvos-808080.svg?style=flat)
![badge-mac](http://img.shields.io/badge/platform-macos-111111.svg?style=flat)
![badge-linux](http://img.shields.io/badge/platform-linux-2D3F6C.svg?style=flat)
![badge-windows](http://img.shields.io/badge/platform-windows-4D76CD.svg?style=flat)

<img src="./assets/seahorse.svg" width="48">

Seahorse provides a simple framework to support getting strings from remote sources or fallback to the ones compiled into the app or locally available. The goal is to provide the string as fast as possible, so the framework fetches the remote source and store locally for quick access.

## Gradle setup

<details>
<summary>Kotlin DSL</summary>

```kotlin
implementation("com.bidyut.tech.seahorse:seahorse-core:<version>")
```

</details>
<details open>
<summary>Version Catalogue</summary>

```toml
[versions]
seahorse = "version"

[libraries]
seahorse-core = { group = "com.bidyut.tech.seahorse", name = "seahorse-core", version.ref = "seahorse" }
```

</details>

## Initialise Seahorse

Minimum requirement for a Seahorse set up requires the fallback source to be present, and we default to English. To get started, create an instance of Seahorse with the system fallback (where available).

For Android, we can fallback to the compiled resources:
```kotlin
val seahorse = Seahorse {
    fallbackSource = ResourceFallbackSource(context)
}
```

For iOS, we can fallback to bundled strings:
```swift
let seahorse = Seahorse { c in
    c.fallbackSource = NSLocalizedFallbackSource()
}
```

For platforms that don't have any such provisions we can fallback to a map or other options:
```kotlin
val seahorse = Seahorse {
    fallbackSource = MapFallbackSource(
        mapOf(
            "key" to "basic string",
            "parameterisedKey" to "got %s",
        )
    )
}
```

## Accessing strings

Now you can access the string resource for the currently selected language by:
```kotlin
seahorse.getString("key")
```
or if there is some parameters we can tack along
```kotlin
seahorse.getString("parameterisedKey", "milk")
```

## Setting up a remote source

Remote sources require a local store to cache the strings for quick access. The local store can be a simple map or a more complex database.

We can use a SQLite database for the local store, for example in Android:
```kotlin
val seahorse = Seahorse {
    fallbackSource = ResourceFallbackSource(context)
    localStore = AndroidDatabaseDriverFactory(context)
}
```
<details>
<summary>Library setup</summary>

```toml
[libraries]
seahorse-sqlite = { group = "com.bidyut.tech.seahorse", name = "seahorse-sqlite", version.ref = "seahorse" }
```

Or we can use Realm database:
```kotlin
val seahorse = Seahorse {
    fallbackSource = ResourceFallbackSource(context)
    localStore = RealmLocalStore()
}
```
<details>
<summary>Library setup</summary>

```toml
[libraries]
seahorse-realm = { group = "com.bidyut.tech.seahorse", name = "seahorse-realm", version.ref = "seahorse" }
```

</details>

### Ktor Network Source
And we can pair that up with an Ktor network source:
```kotlin
val seahorse = Seahorse {
    fallbackSource = ResourceFallbackSource(context)
    localStore = AndroidDatabaseDriverFactory(context)
    remoteSource = AndroidKtorNetworkSource { languageId ->
        "https://www.bidyut.com/tech/seahorse/sample/${languageId.lowercase()}.json"
    }
}
```
<details>
<summary>Library setup</summary>

```toml
[libraries]
seahorse-ktor = { group = "com.bidyut.tech.seahorse", name = "seahorse-ktor", version.ref = "seahorse" }
```

</details>

### OkHttp Network Source
There is also provide OkHttp implementation:
```kotlin
val seahorse = Seahorse {
    fallbackSource = ResourceFallbackSource(context)
    localStore = AndroidDatabaseDriverFactory(context)
    remoteSource = OkHttpNetworkSource(okHttpClient) { languageId ->
        "https://www.bidyut.com/tech/seahorse/sample/${languageId.lowercase()}.json"
    }
}
```
<details>
<summary>Library setup</summary>

```toml
[libraries]
seahorse-okhttp = { group = "com.bidyut.tech.seahorse", name = "seahorse-okhttp", version.ref = "seahorse" }
```

</details>
