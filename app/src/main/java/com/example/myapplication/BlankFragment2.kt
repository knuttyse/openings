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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.viewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.github.bhlangonijr.chesslib.*
import com.github.bhlangonijr.chesslib.Board

data class SquareData(
    val square: Square,
    val piece: Piece,
    val isSelected: Boolean,
    val isCandidate: Boolean
)

val r8 =
    listOf(Square.A8, Square.B8, Square.C8, Square.D8, Square.E8, Square.F8, Square.G8, Square.H8)
val r7 =
    listOf(Square.A7, Square.B7, Square.C7, Square.D7, Square.E7, Square.F7, Square.G7, Square.H7)
val r6 =
    listOf(Square.A6, Square.B6, Square.C6, Square.D6, Square.E6, Square.F6, Square.G6, Square.H6)
val r5 =
    listOf(Square.A5, Square.B5, Square.C5, Square.D5, Square.E5, Square.F5, Square.G5, Square.H5)
val r4 =
    listOf(Square.A4, Square.B4, Square.C4, Square.D4, Square.E4, Square.F4, Square.G4, Square.H4)
val r3 =
    listOf(Square.A3, Square.B3, Square.C3, Square.D3, Square.E3, Square.F3, Square.G3, Square.H3)
val r2 =
    listOf(Square.A2, Square.B2, Square.C2, Square.D2, Square.E2, Square.F2, Square.G2, Square.H2)
val r1 =
    listOf(Square.A1, Square.B1, Square.C1, Square.D1, Square.E1, Square.F1, Square.G1, Square.H1)


val normalRows = listOf(
    r8, r7, r6, r5, r4, r3, r2, r1
)
val flippedRows = listOf(
    r1.reversed(), r2.reversed(), r3.reversed(), r4.reversed(),
    r5.reversed(), r6.reversed(), r7.reversed(), r8.reversed()
)

data class Blank2State(
    val board: Board,
    val selectedSquare: Square = Square.NONE,
    val promoteSquare: Square? = null
) {
    val flipped = board.sideToMove == Side.BLACK
    val legalMoves = board.legalMoves()
    val possibleMoves = legalMoves.filter { it.from === selectedSquare }
    val candidateSquares = possibleMoves.map { it.to }

    val squares = (if (flipped) flippedRows else normalRows).map { rank ->
        rank.map { square ->
            SquareData(
                square,
                board.getPiece(square),
                square == selectedSquare,
                candidateSquares.contains(square)
            )
        }
    }
}

class Blank2ViewModel(initialState: Blank2State) : ViewModel() {
    val state = MutableLiveData<Blank2State>(initialState)

    private fun setState(f: (Blank2State) -> Blank2State) {
        val value = state.value

        if (value != null) {
            state.postValue(f(value))
        }
    }

    fun clickSquare(data:SquareData) {
        val value = state.value
        val currentSelected = value?.selectedSquare
        val square = data.square

        if (currentSelected == data.square) {
            // Unselect piece
            setState { it.copy(selectedSquare = Square.NONE) }
        } else if (value?.candidateSquares?.contains(square) == true) {
            // Is move
            val possible = value.possibleMoves.filter { it.to == square }

            if (possible.size > 1) {
                setState { it.copy(promoteSquare = data.square) }
            } else {
                val board = value.board.clone()
                board.doMove(possible.first())
                setState {
                    it.copy(board = board, selectedSquare = Square.NONE, null)
                }
            }

        } else {
            setState { it.copy(selectedSquare = square) }
        }
    }

    /*fun flip() {
        setState { it.copy(flipped = !it.flipped) }
    }*/
}

class Blank2ViewModelFactory(val initialState: Blank2State) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Blank2ViewModel::class.java)) {
            return Blank2ViewModel(initialState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}

val Piece.drawable: Int
    get() = when (this) {
        Piece.WHITE_PAWN -> R.drawable.chess_plt45
        Piece.WHITE_KNIGHT -> R.drawable.chess_nlt45
        Piece.WHITE_BISHOP -> R.drawable.chess_blt45
        Piece.WHITE_ROOK -> R.drawable.chess_rlt45
        Piece.WHITE_QUEEN -> R.drawable.chess_qlt45
        Piece.WHITE_KING -> R.drawable.chess_klt45
        Piece.BLACK_PAWN -> R.drawable.chess_pdt45
        Piece.BLACK_KNIGHT -> R.drawable.chess_ndt45
        Piece.BLACK_BISHOP -> R.drawable.chess_bdt45
        Piece.BLACK_ROOK -> R.drawable.chess_rdt45
        Piece.BLACK_QUEEN -> R.drawable.chess_qdt45
        Piece.BLACK_KING -> R.drawable.chess_kdt45
        Piece.NONE -> R.drawable.chess_empty
    }

class BlankFragment2 : Fragment() {

    private val model: Blank2ViewModel by viewModels<Blank2ViewModel> {
        val board = Board()
        board.doNullMove()
        val state = Blank2State(board, Square.NONE, null)
        Blank2ViewModelFactory(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Board(model)
            }
        }
    }

    /*fun flip() {
        model.flip()
    }*/

    private fun navigateBack(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_blankFragment2_to_blankFragment)
    }
}
/*
@Preview
@Composable
fun PreviewBoard() {
    Board()
}*/


@Composable
fun Board(viewModel: Blank2ViewModel = viewModel()) {
    val state: Blank2State? by viewModel.state.observeAsState()
    val clickSquare: (SquareData)->Unit = {viewModel.clickSquare(it)}
    state?.let {
        Board2(it, clickSquare)
    } ?: Board2(state = Blank2State(Board(), Square.NONE, null), clickSquare)
}

@Composable
private fun Board2(state: Blank2State, clickSquare: (SquareData) -> Unit) {
    val rankRange = 0..7
    val columnRange = 0..7

    Column(Modifier.fillMaxWidth().fillMaxHeight()) {
        WithConstraints {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                rankRange.forEach { r -> // rows
                    Row(
                        //modifier = Modifier.weight(1f)
                    ) {
                        columnRange.forEach { c -> // columns
                            val color =//Color.LightGray
                                if (c.rem(2) != r.rem(2))
                                    Color.LightGray
                                else Color.White

                            val squareData = state.squares[r][c]
                            val pieceDrawable = squareData.piece.drawable

                            Box(
                                modifier =
                                Modifier.weight(1f)
                                    .background(color)
                                    .clickable(onClick = {
                                        clickSquare(squareData)
                                    })
                            ) {
                                Image(
                                    vectorResource(id = pieceDrawable),
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
