package main

import fileManagment.FileManager

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.util.concurrent.ForkJoinPool

class Server {
    private var PORT: Int = 8080
    private var POOL_SIZE: Int = 2
    private var ROOTDIR = "/Users/rubenhovhannisyan/Desktop/http-test-suite-master"
    private val CONFIG_PATH = "./httpd.conf"

    @Throws(IOException::class)
    fun readConfig() {
        val fstream = FileInputStream(CONFIG_PATH)
        val bufferedReader = BufferedReader(InputStreamReader(fstream))

        var line: String
        while (true) {
            try {
                line = bufferedReader.readLine()
            } catch (e: Exception) {
                break;
            }
            val maps = line.split(" ")
            if (maps[0] == "listen") {
                PORT = Integer.parseInt(maps[1])
            }
            if (maps[0] == "cpu_limit") {
                POOL_SIZE = Integer.parseInt(maps[1])
            }
            if (maps[0] == "document_root") {
                ROOTDIR = maps[1]
            }
        }
        fstream.close()
        bufferedReader.close()
        println("Configuration file successfully read")
    }

    fun start() {
        FileManager.DOCUMENT_ROOT = ROOTDIR
        val serverSocket: ServerSocket
        try {
            serverSocket = ServerSocket(PORT)
        } catch (e: IOException) {
            println("Unable to start server")
            return
        }

        val pool = MyThreadPool(POOL_SIZE)
        println("Server successfully started:" +
                "\nPort Number: " + PORT +
                "\nRoot Directory: " + ROOTDIR +
                "\nCPU Count: " + POOL_SIZE)

        while (true) {
            try {
                val socket = serverSocket.accept()
                pool.execute(Task(socket))
            } catch (e: IOException) {
                println("Server crashed")
                break
            }

        }

    }


}
