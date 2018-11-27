package com.example.maria.homework_5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_green.*

class GreenFragment : Fragment() {
    private val mReceiver: BroadcastReceiver by lazy(this::CommunicationBroadcastReceiver)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_green, container, false)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter().apply {
            addAction(SWITCH_INTENT)
            addAction(TEXT_INTENT)
            addAction(BUTTON_INTENT)
        }
        context?.registerReceiver(mReceiver, filter)
        updateText("Text: ")
        updateSwitchInfo("Switch: \"Off\"")
        updateButtonInfo("Button state: \"Not pressed\"")
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(mReceiver)
    }

    fun updateText(text: String) {
        edit_text_info.text = text
    }
    fun updateSwitchInfo(text: String) {
        switch_info.text = text
    }
    fun updateButtonInfo(text: String) {
        button_state.text = text
    }
    inner class CommunicationBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                SWITCH_INTENT ->{
                    if(intent.getBooleanExtra("EXTRA_SWITCH_CHANGED", false))
                    updateSwitchInfo("Switch: \"On\"")
                    else updateSwitchInfo("Switch: \"Off\"")
                }
                TEXT_INTENT ->{
                        updateText("Text: \""+intent.getStringExtra("EXTRA_TEXT_CHANGED")+"\"")
                }
                BUTTON_INTENT ->{
                    if(intent.getBooleanExtra("EXTRA_BUTTON_CHANGED", false))
                        updateButtonInfo("Button state: \"Pressed\"")
                    else updateButtonInfo("Button state: \"Not pressed\"")
                }
            }
        }
    }

    companion object {
        const val TEXT_INTENT = "MY_TEXT_CHANGE"
        const val SWITCH_INTENT = "MY_SWITCH_CHANGE"
        const val BUTTON_INTENT = "MY_BUTTON_CHANGE"
    }
}
