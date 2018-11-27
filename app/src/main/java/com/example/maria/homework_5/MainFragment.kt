package com.example.maria.homework_5

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity)
                .setActionBarTitle("Homework_5")
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        system_info_button.setOnClickListener{
            fragmentManager!!.beginTransaction()
                    .replace(R.id.fragment_frame_layout, FragmentSystemInfo())
                    .addToBackStack(null)
                    .commit()
        }

        fragment_communications_button.setOnClickListener{
            fragmentManager!!.beginTransaction()
                    .replace(R.id.fragment_frame_layout, CommunicationFragment())
                    .addToBackStack(null)
                    .commit()
        }
    }
}