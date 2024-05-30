package org.example

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.concurrent.thread

@Composable
@Preview
fun App() {
    var login by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(false) }
    var messages = remember { mutableStateListOf<String>() }
    var userList by remember { mutableStateOf(listOf<String>()) }
    var client: Client? by remember { mutableStateOf(null) }

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        // Основная часть чата
        Column(
            modifier = Modifier.weight(3f).fillMaxHeight(),
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
                            val text = "$login: $message"
                            client?.send(text)
                        } else {
                            isLogin = true
                            login = message
                            client = Client()
                            client?.start()
                            client?.send(login)
                            thread {
                                while (true) {
                                    val t = client?.receive()
                                    if (t != null) {
                                        if (t.startsWith("/users ")) {
                                            userList = t.substring(7).split(",")
                                        } else {
                                            messages.add(t)
                                        }
                                    }
                                }
                            }
                        }
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

        // Список пользователей
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(16.dp)
                .background(Color.LightGray)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Пользователи онлайн:", fontSize = 20.sp, modifier = Modifier.padding(vertical = 4.dp))
            for (user in userList) {
                Text(text = user, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
