package com.bidyut.tech.seahorse.example

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bidyut.tech.seahorse.example.ui.screen.StringListScreen
import com.bidyut.tech.seahorse.example.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWindow() {
    MyApplicationTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(stringResource(id = R.string.app_name))
                })
            },
        ) { padding ->
            StringListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        }
    }
}
