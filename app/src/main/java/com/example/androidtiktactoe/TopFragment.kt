package com.example.androidtiktactoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class TopFragment : Fragment(R.layout.fragment_top) {
    private lateinit var statusTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusTextView = view.findViewById(R.id.status_text)
        val resetButton = view.findViewById<Button>(R.id.reset_button)

        savedInstanceState?.let {
            statusTextView.text = it.getString("statusText", "Player X's Turn")
        }

        resetButton.setOnClickListener {
            statusTextView.text = "Player X's Turn"
            (activity as? GameResetListener)?.onGameReset()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val statusTextView = view?.findViewById<TextView>(R.id.status_text)
        outState.putString("statusText", statusTextView?.text.toString())
    }
    //Change turn
    fun updateTurn(player: String) {
        statusTextView.text = "Player $player's Turn"
    }

    interface GameResetListener {
        fun onGameReset()
    }
}


