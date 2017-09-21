package be.maartenballiauw.rider.svcutil.util

import java.io.*

// Source: https://stackoverflow.com/questions/882772/capturing-stdout-when-calling-runtime-exec/18955510#18955510

/**
 * Execute external process and optionally read output buffer.
 */
class ShellExec @JvmOverloads constructor(private val readOutput: Boolean = false, private val readError: Boolean = false) {
    var exitCode: Int = 0
        private set

    private var errorGobbler: StreamGobbler? = null
    private var outputGobbler: StreamGobbler? = null

    /**
     * Execute a command.
     * @param command   command ("c:/some/folder/script.bat" or "some/folder/script.sh")
     * @param workdir   working directory or NULL to use command folder
     * @param wait  wait for process to end
     * @param args  0..n command line arguments
     * @return  process exit code
     */
    @Throws(IOException::class)
    fun execute(command: String, workdir: String?, wait: Boolean, vararg args: String): Int {
        var cmdArr: Array<String>

        if (args != null && args.size > 0) {
            cmdArr = Array<String>(1 + args.size, { _ -> "" })
            cmdArr[0] = command
            System.arraycopy(args, 0, cmdArr, 1, args.size)
        } else {
            cmdArr = arrayOf(command)
        }

        val pb = ProcessBuilder(*cmdArr)
        val workingDir = if (workdir == null) File(command).parentFile else File(workdir)
        pb.directory(workingDir)

        val process = pb.start()

        // Consume streams, older jvm's had a memory leak if streams were not read,
        // some other jvm+OS combinations may block unless streams are consumed.
        errorGobbler = StreamGobbler(process.errorStream, readError)
        outputGobbler = StreamGobbler(process.inputStream, readOutput)
        errorGobbler!!.start()
        outputGobbler!!.start()

        exitCode = 0
        if (wait) {
            try {
                process.waitFor()
                exitCode = process.exitValue()
            } catch (ex: InterruptedException) {
            }

        }
        return exitCode
    }

    val isOutputCompleted: Boolean
        get() = if (outputGobbler != null) outputGobbler!!.isCompleted else false

    val isErrorCompleted: Boolean
        get() = if (errorGobbler != null) errorGobbler!!.isCompleted else false

    val output: String?
        get() = if (outputGobbler != null) outputGobbler!!.getOutput() else null

    val error: String?
        get() = if (errorGobbler != null) errorGobbler!!.getOutput() else null

    //********************************************
    //********************************************

    /**
     * StreamGobbler reads inputstream to "gobble" it.
     * This is used by Executor class when running
     * a commandline applications. Gobblers must read/purge
     * INSTR and ERRSTR process streams.
     * http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4
     */
    private inner class StreamGobbler(private val `is`: InputStream, readStream: Boolean) : Thread() {
        private val output: StringBuilder?
        /**
         * Is input stream completed.
         * @return
         */
        @Volatile
        var isCompleted: Boolean = false
            private set // mark volatile to guarantee a thread safety

        init {
            this.output = if (readStream) StringBuilder(256) else null
        }

        override fun run() {
            isCompleted = false
            try {
                val NL = System.getProperty("line.separator", "\r\n")

                val isr = InputStreamReader(`is`)
                val br = BufferedReader(isr)
                var line: String? = br.readLine()
                while (line != null) {
                    output?.append(line + NL)
                    line = br.readLine()
                }
            } catch (ex: IOException) {
                // ex.printStackTrace();
            }

            isCompleted = true
        }

        /**
         * Get inputstream buffer or null if stream
         * was not consumed.
         * @return
         */
        fun getOutput(): String? {
            return output?.toString()
        }

    }

}