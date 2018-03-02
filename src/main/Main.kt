package main


import java.io.*


object Main {


    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        val server = Server()
        server.readConfig()
        server.start()

    }
}
