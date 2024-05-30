package org.example

import java.net.ServerSocket
import kotlin.concurrent.thread

class Server(port: Int = 8080) {
    var socket: ServerSocket = ServerSocket(port)

    fun start() {
        while (true) {
            val clientSocket = socket.accept()
            thread {
                try {
                    val connection = Connection(clientSocket)
                    val login = connection.receive() ?: "Unknown"
                    val connectedClient = ConnectedClient(clientSocket, login)
                    println(connectedClient)
                    println(connection)

                    while (true) {
                        val text = connection.receive()
                        if (text != null) {
                            connectedClient.sendAll(text)
                        }
                    }
                } catch (e: Exception) {
                    println("Client disconnected")
                    val client = ConnectedClient.clients.find { it.connection.clientSocket == clientSocket }
                    client?.removeClient()
                    clientSocket.close()
                }
            }
        }
    }
}
