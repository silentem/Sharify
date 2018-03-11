package com.sharify

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.net.InetAddress

/**
 * @author whaletail
 */

class PingAsynkTask : AsyncTask<String, Void, Void?>(), AnkoLogger {
    override fun doInBackground(vararg params: String): Void? {
        val runtime = Runtime.getRuntime()
        val command = "ping ${params[0]}"
        val process = runtime.exec(command)
//        Handler(Looper.getMainLooper()).postDelayed({ process.destroy() }, 100)
        info { command }
        return null
    }

}