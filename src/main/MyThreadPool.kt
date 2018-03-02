package main

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executor

class MyThreadPool(threadCount: Int) : Executor {
    private val queue = ConcurrentLinkedQueue<Runnable>()
    @Volatile
    private var isRunning = true

    init {
        for (i in 0 until threadCount) {
            Thread(TaskWorker()).start()
        }
    }

    override fun execute(command: Runnable) {
        if (isRunning) {
            queue.offer(command)
        }
    }

    fun shutdown() {
        isRunning = false
    }

    private inner class TaskWorker : Runnable {

        override fun run() {
            while (isRunning) {
                val nextTask = queue.poll()

                try {
                    if (nextTask != null) {
                        nextTask.run()
                    }
                } catch (e: RuntimeException) {
//                    isRunning = false;
                    System.out.println("Server crashed : Threadpool couldnt get next task")
                }
            }
        }
    }
}
