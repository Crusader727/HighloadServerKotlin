package main

class MyThreadPool(threadCount: Int) {
    private val queue = ArrayList<Runnable>()
    @Volatile
    private var isRunning = true

    init {
        for (i in 0 until threadCount) {
            Thread(TaskWorker()).start()
        }
    }


    fun execute(command: Runnable) {
        synchronized(queue) {
            if (isRunning) {
                queue.add(command)
            }
        }
    }

    fun shutdown() {
        isRunning = false
    }

    private inner class TaskWorker : Runnable {

        override fun run() {
            while (!Thread.currentThread().isInterrupted) {
                if (queue.isNotEmpty()) {
                    var nextTask: Runnable? = null
                    synchronized(queue) {
                        if (queue.isNotEmpty()) {
                            nextTask = queue.get(0)
                            queue.removeAt(0)
                        }
                    }
                    try {
                        nextTask!!.run()
                    } catch (e: RuntimeException) {
                    }
                }
            }
        }
    }
}
