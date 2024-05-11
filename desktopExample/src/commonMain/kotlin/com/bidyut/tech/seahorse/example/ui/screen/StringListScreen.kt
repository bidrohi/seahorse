package com.bidyut.tech.seahorse.example.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.bidyut.tech.seahorse.model.LanguageBengali
import com.bidyut.tech.seahorse.model.LanguageEnglish
import kotlinx.coroutines.launch

@Composable
fun StringListScreen(
    modifier: Modifier = Modifier,
    viewModel: StringListViewModel,
) {
    val scope = rememberCoroutineScope()
    val snackbarHost = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current
    LaunchedEffect(true) {
        scope.launch {
            viewModel.effect.collect {
                when (it) {
                    is StringListContract.Effect.CopyKeyToClipboard -> {
                        clipboardManager.setText(AnnotatedString(it.key))
                        snackbarHost.showSnackbar(
                            message = "String key copied!"
                        )
                    }
                }
            }
        }
    }
    val state = viewModel.viewState.collectAsState().value
    when (state) {
        is StringListContract.UiState.StatusQuo -> {
            Column(
                modifier = modifier,
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.sendTrigger(
                                StringListContract.Trigger.LanguageChanged(
                                    when (state.languageId) {
                                        LanguageEnglish -> LanguageBengali
                                        else -> LanguageEnglish
                                    }
                                ),
                            )
                        },
                    ) {
                        Text(text = "Swap ${state.languageId}")
                    }
                    OutlinedButton(
                        onClick = {
                            viewModel.sendTrigger(
                                StringListContract.Trigger.FetchStringList(state.languageId),
                            )
                        },
                    ) {
                        Text(text = "Fetch ${state.languageId}")
                    }
                    OutlinedButton(
                        onClick = {
                            viewModel.sendTrigger(
                                StringListContract.Trigger.ClearStore(state.languageId),
                            )
                        },
                    ) {
                        Text(text = "Clear ${state.languageId}")
                    }
                }
                ExampleStringList(
                    viewModel = viewModel,
                    onItemClick = {
                        viewModel.sendTrigger(StringListContract.Trigger.StringClicked(it))
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        is StringListContract.UiState.Connecting -> {
            CircularProgressIndicator()
        }

        is StringListContract.UiState.Error -> {
            Text(text = state.message)
        }
    }
}

@Composable
fun ExampleStringList(
    viewModel: StringListViewModel,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
    ) {
        items(viewModel.stringKeys) {
            StringRow(key = it,
                value = when (it) {
                    "platform" -> {
                        viewModel.seahorse.getString(it, "Android")
                    }
                    "sentence_structure" -> {
                        viewModel.seahorse.getString(it, "Seahorse", "gives", "strings")
                    }
                    else -> {
                        viewModel.seahorse.getString(it)
                    }
                },
                modifier = Modifier.clickable {
                    onItemClick(it)
                })
            HorizontalDivider()
        }
    }
}

@Composable
fun StringRow(
    key: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = { Text(key) },
        supportingContent = { Text(value) },
        modifier = modifier.padding(4.dp),
    )
}
