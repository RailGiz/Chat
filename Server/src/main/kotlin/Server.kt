package org.example
import java.io.BufferedReader
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(port: Int = 8080) {
    var socket: ServerSocket = ServerSocket(port)
    var clientSocket: Socket? = null
    var br: BufferedReader? = null
    lateinit var connection: Connection


    fun start(){
        clientSocket = socket.accept()
        connection = Connection(clientSocket)
        thread {
            var text = receive()
            //receive()?.let { send(it) }
            if (text != null) {
                send(text)
                //println("")
            }
            println(text)
        }
    }
    fun send(text: String){
        var c = clientSocket?.let { ConnectedClient(it) }
        if (c != null) {
            c.sendAll(text)
        }
    }

    fun receive(): String?{
        br = clientSocket?.getInputStream()?.bufferedReader()
        return br?.readLine()
    }
    fun finish(){}



}