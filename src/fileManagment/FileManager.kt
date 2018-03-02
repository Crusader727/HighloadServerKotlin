package fileManagment


import java.io.*

class FileManager(path: String) {

    private var file: File? = null
    var isDir = false
        private set

    init {
        file = File(DOCUMENT_ROOT + path)
        if (file!!.isDirectory) {
            file = File(DOCUMENT_ROOT + path + INDEX_NAME)
            isDir = true
        }
    }

    @Throws(FileNotFoundException::class)
    fun getFile(): FileInputStream {
        return FileInputStream(file!!.absoluteFile)
    }


    val contentType: String?
        get() {
            val contentType = fileExtension ?: return null
            var result: String? = null

            if (contentType == "css") {
                result = "text/css"
            }
            if (contentType == "js") {
                result = "text/javascript"
            }
            if (contentType == "jpg") {
                result = "image/jpeg"
            }
            if (contentType == "jpeg") {
                result = "image/jpeg"
            }
            if (contentType == "png") {
                result = "image/png"
            }
            if (contentType == "gif") {
                result = "image/gif"
            }
            if (contentType == "swf") {
                result = "application/x-shockwave-flash"
            }
            if (contentType == "html") {
                result = "text/html"
            }
            return result
        }

    fun FileExists(): Boolean {
        return file!!.exists()
    }

    fun canRead(): Boolean {
        return file!!.canRead()
    }

    fun fileSize(): Long {
        return file!!.length()
    }

    private val fileExtension: String?
        get() {
            val name = file!!.absolutePath
            try {
                return name.substring(name.lastIndexOf(".") + 1)
            } catch (e: RuntimeException) {
                return null
            }

        }

    companion object {
        var DOCUMENT_ROOT = ""
        val INDEX_NAME = "/index.html"
    }

}
