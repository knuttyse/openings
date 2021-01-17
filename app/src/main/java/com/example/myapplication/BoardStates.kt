package com.example.myapplication

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move

fun doMove(board: Board, move: Move): Board {
    val clone = board.clone()
    clone.doMove(move)
    return clone
}

// make a move
fun AbstractBoardState.doMove(move: Move): AbstractBoardState {
    return when (this) {
        is AbstractBoardState.SelectedBoardState,
        is AbstractBoardState.PromotionBoardState -> {
            return if (move == correctMove) {
                AbstractBoardState.CorrectMoveState(board, correctMove)
            } else {
                AbstractBoardState.IncorrectMoveState(board, correctMove, incorrectMove = move)
            }
        }
        else -> this
    }
}

// promotion piece
fun AbstractBoardState.promoteTo(piece: Piece): AbstractBoardState {
    return when (this) {
        is AbstractBoardState.PromotionBoardState -> {
            val move = moves.first { it.promotion == piece }
            this.doMove(move)
        }
        else -> this
    }
}

// Touch square:
fun AbstractBoardState.touchSquare(square: Square): AbstractBoardState {
    return when (this) {
        is AbstractBoardState.BaseBoardState -> {
            return if (moves.any { it.from == square }) {
                AbstractBoardState.SelectedBoardState(board, correctMove, square)
            } else {
                this
            }
        }
        is AbstractBoardState.SelectedBoardState -> {
            val possibleMoves = moves.filter { it.from == selectedSquare && it.to == square }
            return when (possibleMoves.size) {
                0 -> AbstractBoardState.BaseBoardState(board, correctMove)
                1 -> this.doMove(possibleMoves.first())
                else -> AbstractBoardState.PromotionBoardState(board, correctMove, possibleMoves)
            }
        }
        is AbstractBoardState.PromotionBoardState ->
            AbstractBoardState.BaseBoardState(board, correctMove)
        else -> this
    }
}

sealed class AbstractBoardState(val board: Board, val correctMove: Move) {

    val moves: List<Move> by lazy {
        board.legalMoves()
    }

    class BaseBoardState(board: Board, correctMove: Move) :
        AbstractBoardState(board, correctMove)

    class SelectedBoardState(board: Board, correctMove: Move, val selectedSquare: Square) :
        AbstractBoardState(board, correctMove)

    class PromotionBoardState(board: Board, correctMove: Move, val promotionMoves: List<Move>) :
        AbstractBoardState(board, correctMove)

    class CorrectMoveState(board: Board, correctMove: Move) :
        AbstractBoardState(board, correctMove)

    class IncorrectMoveState(board: Board, correctMove: Move, val incorrectMove: Move) :
        AbstractBoardState(board, correctMove)
}