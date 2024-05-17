package org.example
import java.net.Socket


class ConnectedClient(socket: Socket) {
    companion object {
        var clients = mutableListOf<ConnectedClient>()
    }

    var connection: Connection = Connection(socket)

    fun send(text: String) = connection.send(text)
    fun sendAll(text: String) = Companion.clients.forEach{
        it.send(text)
    }
}