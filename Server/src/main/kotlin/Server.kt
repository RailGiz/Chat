package org.example
import java.io.BufferedReader
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class Server(port: Int = 8080) {
    //Переменная socket является объектом ServerSocket,
    // который используется для приема подключений от клиентов на сервере,
    // а переменная clientSocket является объектом Socket,
    // который представляет собой сетевое соединение с подключенным клиентом на сервере.
    // Каждое подключение к серверу создает новый объект Socket,
    // который используется для обмена данными с этим клиентом.
    var socket: ServerSocket = ServerSocket(port)
    var clientSocket: Socket? = null
    //lateinit var connection: Connection


    fun start(){
        //Когда сервер запускается, создается новый объект ServerSocket,
        // который принимает подключения от клиентов на определенном порту.
        // Когда клиент подключается к серверу,
        // сервер должен принять это подключение и создать новое сетевое соединение с клиентом.
        // Это делается с помощью метода accept объекта ServerSocket.
        clientSocket = socket.accept()
        thread {
            var is_client = true
            //Внутри потока, создается новый объект Connection для обмена данными с клиентом
            // и объект ConnectedClient для управления подключенным клиентом.
            var connection = clientSocket?.let { Connection(it) }!!
            var connectedClient = ConnectedClient(clientSocket!!)
            println(connectedClient)
            println(connection)
            while (is_client) {
                try {
                    //сервер ожидает получения данных от клиента с помощью метода receive объекта Connection.
                    // Если данные получены, они отправляются всем подключенным клиентам на сервере с помощью метода sendAll
                    // объекта ConnectedClient.
                    // Если происходит ошибка в сетевом соединении,
                    // клиент удаляется из списка подключенных клиентов на сервере
                    // с помощью метода remove_client объекта ConnectedClient,
                    // и сетевое соединение закрывается с помощью метода finish объекта Connection.
                    println("wait")
                    var text = connection.receive()
                    println("receive "+connection)
                    println(text)
                    if (text != null) {
                        connectedClient.sendAll(text.toString())
                    }
                    println("sended")
                }
                catch(e:Exception){
                    connectedClient.remove_client(connectedClient)
                    connection.finish()
                    println("disconnected")
                    is_client = false
                }
            }
        }
    }
}