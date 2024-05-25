package org.example
import java.net.Socket


class ConnectedClient(socket: Socket) {
    companion object {
        var clients = mutableListOf<ConnectedClient>()
    }
    init{
        clients.add(this)
    }

    var connection: Connection = Connection(socket)

    fun remove_client(connectedClient: ConnectedClient){
        clients.remove(connectedClient)
        println("now "+clients.size+" clients")
    }


    fun send(text: String) = connection.send(text)
    fun sendAll(text: String) = Companion.clients.onEach{
        it.send(text)
    }
}