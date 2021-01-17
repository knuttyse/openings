package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.myapplication.boardscreen.*
import com.github.bhlangonijr.chesslib.*
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.move.Move

data class SquareData(
    val square: Square,
    val piece: Piece,
    val isSelected: Boolean,
    val isCandidate: Boolean
)


class BoardFragment : BoardHandlers, Fragment() {

    private val model: BoardViewModel by viewModels {
        val board = Board()
        val correctMove = Move(Square.E2, Square.E4)
        val state = AbstractBoardState.BaseBoardState(board, correctMove)
        BoardViewModelFactory(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BoardScreen(model, this@BoardFragment)
            }
        }
    }

    private fun navigateBack(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_blankFragment2_to_blankFragment)
    }

    private fun transform(f: (AbstractBoardState) -> AbstractBoardState): AbstractBoardState {
        return model.transform(f)
    }

    override fun touchSquare(square: Square) {
        transform { it.touchSquare(square) }
    }

    override fun promoteTo(piece: Piece) {
        transform { it.promoteTo(piece) }
    }
}

interface BoardHandlers {
    fun touchSquare(square: Square)
    fun promoteTo(piece: Piece)
}