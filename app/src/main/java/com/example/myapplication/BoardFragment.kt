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
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.github.bhlangonijr.chesslib.*
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.move.Move

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


val rows = listOf(
    r8, r7, r6, r5, r4, r3, r2, r1
)

class BoardViewModel(initialState: AbstractBoardState) : ViewModel() {
    private val state = MutableLiveData(initialState)

    val liveData = state as LiveData<AbstractBoardState>

    fun transform(f: (AbstractBoardState) -> AbstractBoardState): AbstractBoardState {
        val newState = f(state.value!!)
        state.value = newState
        return newState
    }

}

class BoardViewModelFactory(private val initialState: AbstractBoardState) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BoardViewModel(initialState) as T
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

val squareColors: Map<Square, Color> = mutableMapOf<Square, Color>().apply {
    Square.values().forEach { s ->
        if (s != Square.NONE) {
            val f = s.ordinal % 8
            val r = s.ordinal / 8
            val color =
                if (f.rem(2) == r.rem(2))
                    Color.White
                else Color.LightGray
            this[s] = color
        }
    }
}.toMap()


/*val Square.color: Color
    get() = when (this) {
        Square.NONE -> Color.White
        else -> {
            val f = this.file
            val r = this.rank
        }
    }*/

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

@Composable
fun BoardScreen(viewModel: BoardViewModel, handlers: BoardHandlers) {
    val state = viewModel.liveData.observeAsState()
    state.value?.let {
        BoardView(it, handlers)
    }
}

@Composable
private fun BoardView(state: AbstractBoardState, handlers: BoardHandlers) {
    //val rankRange = 0..7
    //val columnRange = 0..7
    WithConstraints(Modifier.fillMaxHeight().fillMaxWidth()) {

        val rotation = when (state.board.sideToMove) {
            Side.WHITE -> 0f
            else -> 180f
        }
        Column(modifier = Modifier.rotate(rotation)) {
            rows.forEach { squares ->

                Row {

                    squares.forEach { square ->

                        Box(
                            modifier = Modifier
                                .background(squareColors.getValue(square))
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable(onClick = {
                                    handlers.touchSquare(square)
                                })
                        ) {
                            val pieceDrawable = state.board.getPiece(square).drawable
                            Image(
                                vectorResource(id = pieceDrawable),
                                modifier = Modifier.align(Alignment.Center).fillMaxSize()
                            )
                        }

                    }
                }
            }
        }
    }
}
