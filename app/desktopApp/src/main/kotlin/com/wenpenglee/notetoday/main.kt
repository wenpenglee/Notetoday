package com.wenpenglee.notetoday

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.wenpenglee.notetoday.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Notetoday",
    ) {
        App()
    }
}