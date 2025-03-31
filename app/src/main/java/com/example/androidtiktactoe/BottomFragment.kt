package com.example.androidtiktactoe

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class BottomFragment : Fragment(R.layout.fragment_bottom) {
    private lateinit var board: Array<Array<Button>>
    private var currentPlayer = "X"
    private var turnChangeListener: TurnChangeListener? = null
    private var boardState = Array(3) { Array(3) { "" } }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is TurnChangeListener) {
            turnChangeListener = activity
        } else {
            throw RuntimeException("$activity must implement TurnChangeListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize board
        board = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("button_${row}_${col}", "id", requireContext().packageName)
                val button = view.findViewById<Button>(buttonId)
                button.setOnClickListener { onCellClick(button, row, col) } // Correctly captures row & col
                button
            }
        }

        savedInstanceState?.let {
            currentPlayer = it.getString("currentPlayer", "X")!!

            val flatBoard = it.getStringArray("boardState")
            if (flatBoard != null) {
                boardState = Array(3) { r -> Array(3) { c -> flatBoard[r * 3 + c] ?: "" } }
            }
            restoreBoard()
        }
    }
    //Restore boardState if orientation changes
    private fun restoreBoard() {
        for (row in 0..2) {
            for (col in 0..2) {
                board[row][col].text = boardState[row][col]
                board[row][col].isEnabled = boardState[row][col].isEmpty()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentPlayer", currentPlayer)

        // Flatten boardState into a single array for saving
        val flatBoard = boardState.flatten().toTypedArray()
        outState.putStringArray("boardState", flatBoard)
    }
    private fun onCellClick(button: Button, row: Int, col: Int) {
        if (button.text.isNotEmpty()) return

        button.text = currentPlayer
        button.isEnabled = false
        button.contentDescription = "Cell occupied by $currentPlayer"

        // Update boardState to reflect the current move
        boardState[row][col] = currentPlayer

        if (checkWinner()) {
            Toast.makeText(requireContext(), "Player $currentPlayer Wins!", Toast.LENGTH_SHORT).show()
        } else if (isBoardFull()) {
            Toast.makeText(requireContext(), "It's a Draw!", Toast.LENGTH_SHORT).show()
        } else {
            currentPlayer = if (currentPlayer == "X") "O" else "X"
            turnChangeListener?.onTurnChanged(currentPlayer)
        }
    }

    private fun checkWinner(): Boolean {
        val values = board.map { row -> row.map { it.text.toString() } }

        for (i in 0..2) {
            if (values[i][0] == values[i][1] && values[i][1] == values[i][2] && values[i][0].isNotEmpty()) return true
            if (values[0][i] == values[1][i] && values[1][i] == values[2][i] && values[0][i].isNotEmpty()) return true
        }
        if (values[0][0] == values[1][1] && values[1][1] == values[2][2] && values[0][0].isNotEmpty()) return true
        if (values[0][2] == values[1][1] && values[1][1] == values[2][0] && values[0][2].isNotEmpty()) return true

        return false
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it.text.isNotEmpty() } }
    }

    fun resetBoard() {
        board.forEach { row ->
            row.forEach { button ->
                button.text = ""
                button.isEnabled = true
                button.contentDescription = "Empty cell"
            }
        }

        currentPlayer = "X"
        turnChangeListener?.onTurnChanged("X")
    }

    interface TurnChangeListener {
        fun onTurnChanged(player: String)
    }
}
