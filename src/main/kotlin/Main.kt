package org.example

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.DragData
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.Writer
import kotlin.concurrent.thread


lateinit var client: Client
@Composable
@Preview
fun App() {
    var login by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var messages = remember { mutableStateListOf<String>() }
    var client: Client? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Сообщения
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                if (isLogin) {
                    for (m in messages) {
                        Text(
                            text = m,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Ввод сообщения и кнопка отправки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите ваше сообщение здесь") }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    if (isLogin) {
                        text = "$login: $message"
                        client?.send(text)
                    } else {
                        isLogin = true
                        login = message
                        text = "Добро пожаловать $login"
                        client = Client()
                        client?.start()
                        client?.send(text)
                        thread {
                            while (true) {
                                val t = client?.receive()
                                if (t != null) {
                                    messages.add(t)
                                }
                            }
                        }
                    }
                    text = ""
                    message = ""
                },
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Отправить сообщение"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Отправить")
            }
        }
    }
}

fun main() = application {
    var msg = ""
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}