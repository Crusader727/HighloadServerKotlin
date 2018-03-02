package main

import http.Request
import http.Response

import java.io.*
import java.net.Socket
import java.util.concurrent.RecursiveAction


class Task(var socket: Socket) : Runnable {
    var reader: BufferedReader
    var outputStream: OutputStream

    init {
        outputStream = socket.getOutputStream()
        reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    }

    override fun run() {
        try {
            val request = Request()
            while (true) {
                val buf = reader.readLine()
                if (buf == null || buf.trim { it <= ' ' }.isEmpty()) {
                    break
                }
                request.newHeader(buf)
            }
            val response = Response(request)
            response.writeResponse(outputStream)
        } catch (e: IOException) {
            closeAll()
        } finally {
            closeAll()
        }
    }

    fun closeAll() {
        try {
            outputStream.close()
            reader.close()
            socket.close()
        } catch (e: IOException) {
            println("cant close stream in task");
        }

    }
}
