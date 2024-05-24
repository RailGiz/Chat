package org.example
import java.io.BufferedReader
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(port: Int = 8080) {
    var socket: ServerSocket = ServerSocket(port)
    var clientSocket: Socket? = null
    lateinit var connection: Connection


    fun start(){
        clientSocket = socket.accept()
        thread {
            var is_client = true
            connection = clientSocket?.let { Connection(it) }!!
            var connectedClient = ConnectedClient(clientSocket!!)
            while (is_client) {

                try {
                    var text = connection.receive()
                    println(text)
                    if (text != null) {
                        connectedClient.sendAll(text.toString())
                    }
                }
                catch(e:Exception){
                    connectedClient.remove_client(connectedClient)
                    println("disconnected")
                    is_client = false
                }
            }
        }
    }
}