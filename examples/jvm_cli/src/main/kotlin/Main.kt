import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.data.CioKtorNetworkSource
import com.bidyut.tech.seahorse.data.JdbcSqliteLocalStore
import com.bidyut.tech.seahorse.data.LocalStore
import com.bidyut.tech.seahorse.data.MapLocalStore
import com.bidyut.tech.seahorse.example.data.JsonResourceFallbackSource
import com.bidyut.tech.seahorse.model.LanguageBengali
import com.bidyut.tech.seahorse.model.LanguageEnglish
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

fun main() {
    println("SeaHorse Library - JVM Desktop Example")
    println("======================================")

    val stringKeys = listOf(
        "hello",
        "world",
        "platform",
        "sentence_structure",
        "percent",
        "guarantee",
        "foundation",
        "dune",
        "three_body_problem",
    )

    val seahorse: Seahorse by lazy {
        Seahorse {
            fallbackSource = JsonResourceFallbackSource()
            localStore = makeLocalStore()
            networkSource = CioKtorNetworkSource { languageId ->
                "https://www.bidyut.com/tech/seahorse/sample/${languageId.lowercase()}.json"
            }
        }
    }

    var shouldKeepGoing = true
    do {
        println()
        println("1. Show current strings (${seahorse.defaultLanguageId})")
        println("2. Fetch from network (${seahorse.defaultLanguageId})")
        println("3. Clear fetched cache (${seahorse.defaultLanguageId})")
        println("4. Swap to next language")
        println("5. Exit")
        print("Enter your choice: ")
        val choice = readlnOrNull() ?: ""
        when (choice) {
            "1" -> {
                showStrings(seahorse, stringKeys)
            }

            "2" -> {
                println("Fetching from network...")
                runBlocking { seahorse.fetchStrings(seahorse.defaultLanguageId) }.fold(
                    onSuccess = {
                        showStrings(seahorse, stringKeys)
                    },
                    onFailure = {
                        println("Failed to fetch: ${it.message}")
                    }
                )
            }

            "3" -> {
                println("Clearing cache...")
                runBlocking { seahorse.clearStore(seahorse.defaultLanguageId) }.fold(
                    onSuccess = {
                        showStrings(seahorse, stringKeys)
                    },
                    onFailure = {
                        println("Failed to clear cache: ${it.message}")
                    }
                )
            }

            "4" -> {
                when (seahorse.defaultLanguageId) {
                    LanguageEnglish -> seahorse.defaultLanguageId = LanguageBengali
                    else -> seahorse.defaultLanguageId = LanguageEnglish
                }
                println("Switched to ${seahorse.defaultLanguageId}")
                showStrings(seahorse, stringKeys)
            }

            "5" -> {
                println("Bye bye!")
                shouldKeepGoing = false
            }

            else -> {
                println("Invalid choice. Please try again.")
            }
        }
    } while (shouldKeepGoing)
}

private fun makeLocalStore(): LocalStore {
    println()
    println("1. In-memory store")
    println("2. SQLite store")
    print("Enter your choice: ")
    val choice = readlnOrNull() ?: ""
    return when (choice) {
        "1" -> {
            println("Using in-memory store")
            MapLocalStore()
        }

        "2" -> {
            println("Using SQLite store")
            JdbcSqliteLocalStore()
        }

        else -> {
            println("Invalid choice. Using in-memory store")
            exitProcess(-1)
        }
    }
}

private fun showStrings(
    seahorse: Seahorse,
    stringKeys: List<String>
) {
    println("Current strings:")
    stringKeys.forEach { key ->
        val value = when (key) {
            "platform" -> {
                seahorse.getString(key, "JVM")
            }

            "sentence_structure" -> {
                seahorse.getString(key, "Seahorse", "gives", "strings")
            }

            "percent" -> {
                seahorse.getString(key, 42)
            }

            else -> {
                seahorse.getString(key)
            }
        }
        println("\t$key => $value")
    }
}
