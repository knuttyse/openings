package com.example.myapplication.boardscreen

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
import androidx.compose.ui.res.vectorResource
import com.example.myapplication.BoardHandlers
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move

@Composable
fun BoardScreen(viewModel: BoardViewModel, handlers: BoardHandlers) {
    val state = viewModel.liveData.observeAsState()
    state.value?.let {
        BoardView(it, handlers)
    }
}

@Composable
private fun BoardView(state: AbstractBoardState, handlers: BoardHandlers) {
    val board = state.board
    val rotation = when (board.sideToMove) {
        Side.WHITE -> 0f
        else -> 180f
    }

    val isCorrect = state is AbstractBoardState.CorrectMoveState
    val isIncorrect = state is AbstractBoardState.IncorrectMoveState
    val isCorrectOrIncorrect = isCorrect or isIncorrect
    val boardToDraw =
        if (isCorrectOrIncorrect) state.boardAfterCorrectMove
        else board
    val incorrectMove: Move? =
        if (state is AbstractBoardState.IncorrectMoveState)
            state.incorrectMove
        else null

    val incorrectSquares: Set<Square> = incorrectMove?.run {
        setOf(from, to)
    }.orEmpty()

    val correctSquares: Set<Square> = state.correctMove.run {
        setOf(from, to)
    }

    Column(modifier = Modifier.rotate(rotation)) {
        rows.forEach { squares ->

            Row {

                squares.forEach { square ->

                    val defaultColor = squareColors.getValue(square)
                    val isSquareCorrect = isCorrectOrIncorrect && square in correctSquares
                    val background = if (isSquareCorrect) Color.Green else defaultColor

                    Box(
                        modifier = Modifier
                            .background(background)
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable(onClick = {
                                handlers.touchSquare(square)
                            })
                    ) {
                        val pieceDrawable = boardToDraw.getPiece(square).drawable
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
