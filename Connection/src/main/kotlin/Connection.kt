package org.example

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class Connection {

    private var socket: Socket? = null
    private var pw: PrintWriter? = null
    private var br: BufferedReader? = null

    private var stop = false

    constructor(socket: Socket) {
        this.socket = socket
    }

    fun start() {

    }

    fun send(text: String) {
        pw = PrintWriter(socket?.getOutputStream(), true)
        pw?.println(text)
    }

    fun receive():String? {
        br = socket?.getInputStream()?.bufferedReader()
        return br?.readLine()
    }

    fun finish() {
        pw?.close()
        br?.close()
    }

    fun stopReceiving() {
        stop = true
        socket?.close()
    }

}