package com.example.androidtiktactoe

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtiktactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TopFragment.GameResetListener, BottomFragment.TurnChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.top_fragment_container, TopFragment())
                .replace(R.id.bottom_fragment_container, BottomFragment())
                .commit()
        }
    }

    override fun onGameReset() {
        val bottomFragment = supportFragmentManager.findFragmentById(R.id.bottom_fragment_container) as? BottomFragment
        bottomFragment?.resetBoard()
    }

    override fun onTurnChanged(player: String) {
        val topFragment = supportFragmentManager.findFragmentById(R.id.top_fragment_container) as? TopFragment
        topFragment?.updateTurn(player)
    }
}
