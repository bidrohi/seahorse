package com.bidyut.tech.seahorse.example

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bidyut.tech.seahorse.Seahorse
import com.bidyut.tech.seahorse.desktopexample.generated.resources.Res
import com.bidyut.tech.seahorse.desktopexample.generated.resources.app_name
import com.bidyut.tech.seahorse.example.ui.screen.StringListScreen
import com.bidyut.tech.seahorse.example.ui.screen.StringListViewModel
import com.bidyut.tech.seahorse.example.ui.theme.MyApplicationTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun AppWindow(
    seahorse: Seahorse,
    stringKeys: List<String>,
) {
    MyApplicationTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(stringResource(Res.string.app_name))
                })
            },
        ) { padding ->
            StringListScreen(
                viewModel = viewModel(
                    factory = StringListViewModel.buildFactory(seahorse, stringKeys),
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        }
    }
}
