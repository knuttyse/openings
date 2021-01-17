package com.example.myapplication.boardscreen

import androidx.compose.ui.graphics.Color
import com.example.myapplication.R
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Square

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
                    Color.LightGray
                else Color.White
            this[s] = color
        }
    }
}.toMap()
