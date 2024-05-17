package org.example

import java.net.Socket

class Client(val host: String = "localhost", val port: Int = 8080) {
    var socket: Socket? = null
    lateinit var connection: Connection

    fun start(){
        try {
            socket = Socket(host, port)
            connection = Connection(socket!!)
        }catch (e:Exception){

        }
    }
    fun send(text:String)=connection.send(text)

    fun receive()=connection.receive()

}