package org.example
import java.net.Socket

//класс ConnectedClient представляет подключенного клиента на сервере.
class ConnectedClient(socket: Socket) {
    //Объект-компаньон является единственным экземпляром объекта, связанным с классом
    // класс ConnectedClient может создаваться для каждого клиента отдельно экземпляр,
    // а объект компаньон нужен для того чтобы был общий изменяемый объект среди всех экземпляров класса
    // Ведь список клиентов всегда общий но сами клиенты разные
    companion object {
        var clients = mutableListOf<ConnectedClient>()
    }
    //блок инициализации выполняется автоматически.
    // В данном случае, блок инициализации добавляет текущий экземпляр класса ConnectedClient
    // в список clients, который является переменной объекта-компаньона в классе ConnectedClient.
    init{
        clients.add(this)
    }
// переменная connection является переменной экземпляра класса ConnectedClient
// и хранит объект Connection, который используется для обмена данными с подключенным клиентом на сервере.
    var connection: Connection = Connection(socket)

    // используется для удаления подключенного клиента из списка clients,
    // который является переменной объекта-компаньона в классе ConnectedClient
    fun remove_client(connectedClient: ConnectedClient){
        clients.remove(connectedClient)
        println("now "+clients.size+" clients")
    }


    fun send(text: String) = connection.send(text)
    fun sendAll(text: String) = Companion.clients.onEach{
        it.send(text)
    }
}