package http

import java.io.UnsupportedEncodingException
import java.net.URLDecoder


class Request {
    var filePath: String? = null
    var method: String? = null

    fun newHeader(line: String) {
        val buf = getNameHeader(line)
        if (buf != null) {
            if (isValidMethod(buf)) {
                method = buf
                parseMethod(line)
            }
        }
    }

    fun getNameHeader(str: String): String? {
        val index = str.indexOf(' ')
        return if (index != -1) {
            str.substring(0, index)
        } else null
    }

    fun parseMethod(str: String) {

        val buf = str.substring(str.indexOf(' ') + 1)
        filePath = buf.substring(0, buf.indexOf(' '))

        try {
            filePath = URLDecoder.decode(filePath!!, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            println("UnsupportedEncodingException when reading file")
        }

        val index = filePath!!.indexOf('?')
        if (index != -1) {
            filePath = filePath!!.substring(0, index)
        }
        val index2 = filePath!!.indexOf('#')
        if (index2 != -1) {
            filePath = filePath!!.substring(0, index2)
        }

        if (filePath!!.contains("../")) {
            filePath = null
        }
    }

    private fun isValidMethod(buf: String): Boolean {
        return buf == "POST" ||
                buf == "HEAD" ||
                buf == "GET"
    }

}
