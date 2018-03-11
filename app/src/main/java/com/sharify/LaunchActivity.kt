package com.sharify

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.format.Formatter
import com.sharify.sharify.R
import kotlinx.android.synthetic.main.activity_launch.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class LaunchActivity : AppCompatActivity(), AnkoLogger {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        b_find_devices.setOnClickListener { tv_message.text = executeCommand() }

    }

    private fun executeCommand(): String {
        try {

            val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            var ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
            info { ip }
            ip = ip.substring(0..ip.lastIndexOf('.', ip.length, true))
            for (i in 2..255) {
                val runtime = Runtime.getRuntime()
                val command = "ping $ip$i"
                val process = runtime.exec(command)
                Handler().postDelayed({ process.destroy() }, 500)
                info { command }
            }

            return getClientList()


        } catch (ignore: InterruptedException) {
            ignore.printStackTrace()
            println(" Exception:" + ignore)
        } catch (e: IOException) {
            e.printStackTrace()
            println(" Exception:" + e)
        }
        return ""
    }

    private fun getClientList(): String {
        var output = ""
        val br: BufferedReader?
        try {
            br = BufferedReader(FileReader("/proc/net/arp"))
            var line: String?
            line = br.readLine()
            while (line != null) {
                val splitted = line.split(" +".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val mac = splitted[3]
                val ip = splitted[0]
                if (mac.matches("..:..:..:..:..:..".toRegex()) && mac != "00:00:00:00:00:00") {
                    output += "Ip: $ip  Mac: $mac\n"
                }
                line = br.readLine()
            }
        } catch (e: Exception) {

        }

        return output

    }

}