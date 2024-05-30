import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.example.di.AppGraph
import com.bidyut.tech.seahorse.model.LanguageBengali
import com.bidyut.tech.seahorse.model.LanguageEnglish
import kotlinx.coroutines.runBlocking

fun main() {
    println("SeaHorse Library - JVM Desktop Example")
    println("======================================")
    AppGraph.assign(AppGraph())

    val seahorse = AppGraph.instance.seahorse
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
                showStrings(seahorse)
            }

            "2" -> {
                println("Fetching from network...")
                runBlocking { seahorse.fetchStrings(seahorse.defaultLanguageId) }
                showStrings(seahorse)
            }

            "3" -> {
                println("Clearing cache...")
                runBlocking { seahorse.clearStore(seahorse.defaultLanguageId) }
                showStrings(seahorse)
            }

            "4" -> {
                4
                when (seahorse.defaultLanguageId) {
                    LanguageEnglish -> seahorse.defaultLanguageId = LanguageBengali
                    else -> seahorse.defaultLanguageId = LanguageEnglish
                }
                println("Switched to ${seahorse.defaultLanguageId}")
                showStrings(seahorse)
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

private fun showStrings(seahorse: Seahorse) {
    println("Current strings:")
    AppGraph.instance.stringKeys.forEach { key ->
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
