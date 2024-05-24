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
import androidx.compose.runtime.*
import androidx.compose.ui.DragData
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.concurrent.thread



@Composable
@Preview
fun App(client:Client, msg: String) {
    var login by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var is_login by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var messages = remember { mutableStateListOf<String>() }

    /*if(msg!=null){
        messages.add(msg)
    }*/
    println(msg)


    Column {

        //messages
        Box(modifier = Modifier.weight(9f)) {
            //val lazyListState = rememberLazyListState()
            /*LazyColumn(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                for(m in messages) {
                    Text(m)
                }
            }*/
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize()) {
                if (is_login) {
                    for(m in messages) {
                        Text(m)
                    }
                }
            }


        }
        //message and send message
        Row {
            OutlinedTextField(modifier = Modifier.weight(8f), value = message, onValueChange = { message = it })
            Button(
                onClick = {
                    if (is_login) {
                        text = login + ": " + message
                    } else {
                        thread{
                            println("new thread")
                        }
                        is_login = true
                        login = message
                        text = "Добро пожаловать " + login
                    }
                    client.send(text)
                    text = ""
                    message = ""
                }) {
                if (!is_login) {
                    Text("Ввести логин")
                } else {
                    Text("Отправить")
                }
            }
        }
    }

}


fun main() = application {
    var client = Client()
    client.start()
    var msg = ""
    Window(onCloseRequest = ::exitApplication) {
        App(client,msg)
    }
}