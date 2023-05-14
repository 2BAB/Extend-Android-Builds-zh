package me.xx2bab.extendagp.sourceinteraction

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flavorLogic = Class.forName(
            "me.xx2bab.extendagp.sourceinteraction.FlavorLogicImpl"
        ).constructors.first().newInstance() as FlavorLogic
        flavorLogic.test()
    }
}