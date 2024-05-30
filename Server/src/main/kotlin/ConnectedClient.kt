package org.example

import java.net.Socket

class ConnectedClient(socket: Socket, val login: String) {
    companion object {
        var clients = mutableListOf<ConnectedClient>()
    }

    var connection: Connection = Connection(socket)

    init {
        clients.add(this)
        sendUserListToAll()
    }

    fun removeClient() {
        clients.remove(this)
        sendUserListToAll()
        println("Now ${clients.size} clients")
    }

    fun send(text: String) = connection.send(text)

    fun sendAll(text: String) {
        clients.forEach {
            it.send(text)
        }
    }

    private fun sendUserListToAll() {
        val userList = clients.joinToString(separator = ",") { it.login }
        clients.forEach {
            it.send("/users $userList")
        }
    }
}
