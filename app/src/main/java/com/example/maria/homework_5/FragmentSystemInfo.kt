package com.example.maria.homework_5

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.BatteryManager
import kotlinx.android.synthetic.main.fragment_system_info.*
import java.text.SimpleDateFormat
import java.util.*


class FragmentSystemInfo : Fragment() {
    private val mReceiver: BroadcastReceiver by lazy(this::InfoBroadcastReceiver)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity)
                .setActionBarTitle("System Info")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_system_info, container, false)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter().apply{
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_HEADSET_PLUG)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }

        activity!!.registerReceiver(mReceiver, filter)
        updateNetworkInfo()
        updateTimeInfo()
        updateHeadsetInfo()
        updateChargerInfo()
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(mReceiver)
    }

    fun updateNetworkInfo() {
        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected) {
            network_info.text = "Internet: available"
        } else network_info.text = "Internet: disable"
    }

    fun updateChargerInfo() {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = activity!!.registerReceiver(null, ifilter)

        val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        val chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

        if(isCharging || usbCharge || acCharge){
            charger_info.text = "Charger: connected"
        } else charger_info.text = "Charger: disconnected"
    }

    fun updateTimeInfo() {
        val timeText = "Time:  ${SimpleDateFormat("HH:mm").format(Date())}\nTimezone: ${TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)}"
        time_info.text = timeText
    }

    fun updateHeadsetInfo() {
        val audioManager = activity!!.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if(!audioManager.isWiredHeadsetOn && !audioManager.isBluetoothScoOn){
            headset_info.text = "Headset: unplugged"
        } else headset_info.text = "Headset: plugged"
    }

    inner class InfoBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
                    when (intent.action) {
                        ConnectivityManager.CONNECTIVITY_ACTION -> {
                            updateNetworkInfo()
                        }
                        Intent.ACTION_POWER_CONNECTED -> {
                            updateChargerInfo()
                        }
                        Intent.ACTION_POWER_DISCONNECTED -> {
                            updateChargerInfo()
                        }
                        Intent.ACTION_HEADSET_PLUG -> {
                            updateHeadsetInfo()
                        }
                        Intent.ACTION_TIME_CHANGED -> {
                            updateTimeInfo()
                        }
                        Intent.ACTION_TIME_TICK -> {
                            updateTimeInfo()
                        }
                        Intent.ACTION_TIMEZONE_CHANGED -> {
                            updateTimeInfo()
                        }
                    }
        }
    }
}


