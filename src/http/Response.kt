package http

import fileManagment.*

import java.io.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class Response(private val request: Request) {
    private var fileManager: FileManager? = null

    internal var server: String? = "HighloadServer"
    internal var date: String? = null
    internal var connection: String? = "close"
    internal var contentLength: Long? = null
    internal var contentType: String? = null
    private var status = "200 OK"
    private val method: String?
    private val ending = "\r\n"

    init {
        method = request.method
        val filePath = request.filePath
        if (filePath != null) {
            fileManager = FileManager(filePath)
            contentLength = fileManager!!.fileSize()
            contentType = fileManager!!.contentType
        }
        validateStatus(request)
        date = serverTime
    }

    @Throws(IOException::class)
    fun writeResponse(output: OutputStream) {
        writeHeaders(output)
        if (method == "GET" && fileManager!!.FileExists()) {
            giveFile(output)
        }
    }

    @Throws(IOException::class)
    private fun writeHeaders(output: OutputStream) {
        output.write((HTTP_VERSION + status + ending).toByteArray())
        if (isValidMethod) {
            if (contentLength != null) {
                output.write(("Content-Length: " + contentLength + ending).toByteArray())
            }
            if (contentType != null) {
                output.write(("Content-Type: " + contentType + ending).toByteArray())
            }
            if (date != null) {
                output.write(("Date: " + date + ending).toByteArray())
            }
            if (server != null) {
                output.write(("Server: " + server + ending).toByteArray())
            }
            if (connection != null) {
                output.write(("Connection: " + connection!!).toByteArray())
            }
            output.write((ending + ending).toByteArray())
        }


    }

    private val isValidMethod: Boolean
        get() = method == "GET" || method == "HEAD"

    @Throws(IOException::class)
    private fun giveFile(output: OutputStream) {
        try {
            val reader = fileManager!!.getFile()
            val chunk = ByteArray(16 * 1024)
            var lenght: Int
            lenght = reader.read(chunk)
            while (lenght > 0) {
                output.write(chunk, 0, lenght)
                lenght = reader.read(chunk)
            }
            output.flush()
        } catch (e: FileNotFoundException) {
            println("cant give File it was not found")
        }

    }

    private fun validateStatus(request: Request) {
        if (fileManager != null && !fileManager!!.canRead()) {
            status = "403 Forbidden"
        }
        if (fileManager == null || !fileManager!!.FileExists()) {
            status = "404 Not Found"
        }
        if (request.method == "POST") {
            status = "405 Method Not Allowed"
        }
        if (fileManager != null && fileManager!!.isDir && !fileManager!!.FileExists()) {
            status = "403 Forbidden"
        }

    }

    companion object {

        private val HTTP_VERSION = "HTTP/1.1 "

        private val serverTime: String
            get() {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat(
                        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                dateFormat.timeZone = TimeZone.getTimeZone("GMT")
                return dateFormat.format(calendar.time)
            }
    }
}
