import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.bidyut.tech.seahorse.example.AppWindow
import com.bidyut.tech.seahorse.example.di.AppGraph
import com.bidyut.tech.seahorse.jvm_desktop.generated.resources.Res
import com.bidyut.tech.seahorse.jvm_desktop.generated.resources.app_name
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    AppGraph.assign(AppGraph())
    Window(
        title = stringResource(Res.string.app_name),
        onCloseRequest = ::exitApplication,
    ) {
        AppWindow(
            AppGraph.instance.seahorse,
            AppGraph.instance.stringKeys,
        )
    }
}
