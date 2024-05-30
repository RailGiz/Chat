package org.example

import java.io.BufferedReader
import java.io.PrintWriter
import java.net.Socket

class Connection(private val socket: Socket) {
    private var pw: PrintWriter? = null
    private var br: BufferedReader? = null
    private var stop = false

    val clientSocket: Socket
        get() = socket

    fun start() {}

    fun send(text: String) {
        pw = PrintWriter(socket.getOutputStream(), true)
        pw?.println(text)
    }

    fun receive(): String? {
        br = socket.getInputStream().bufferedReader()
        return br?.readLine()
    }

    fun finish() {
        pw?.close()
        br?.close()
    }

    fun stopReceiving() {
        stop = true
        socket.close()
    }
}
