package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureBlock
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.res.loadImageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlin.math.min

class Square(context: Context) : AppCompatImageView(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            widthMeasureSpec
        ) // This is the key that will make the height equivalent to its width
    }
}

val light: Color = Color.White
val dark: Color = Color.Black

val upperRow: List<Color> = listOf(light, dark, light, dark, light, dark, light, dark)
val lowerRow = listOf(dark, light, dark, light, dark, light, dark, light)

val squares: List<Color> = listOf(
    upperRow, lowerRow,
    upperRow, lowerRow,
    upperRow, lowerRow,
    upperRow, lowerRow
).flatten()

val flippedSquares = listOf(
    lowerRow, upperRow,
    lowerRow, upperRow,
    lowerRow, upperRow,
    lowerRow, upperRow
).flatten()


data class Blank2State(
    val flipped: Boolean,
    val pieces: List<Piece?>
)


class Blank2ViewModel(initialState: Blank2State) : ViewModel() {
    val state = MutableLiveData<Blank2State>(initialState)

    private fun setState(f: (Blank2State) -> Blank2State) {
        val value = state.value

        if (value == null) {
            state.postValue(value)
        } else {
            state.postValue(f(value))
        }
    }

    fun flip() {
        setState { it.copy(flipped = !it.flipped) }
    }
}

class Blank2ViewModelFactory(val initialState: Blank2State) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Blank2ViewModel::class.java)) {
            return Blank2ViewModel(initialState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}

enum class Piece(val drawable: Int) {
    BK(R.drawable.chess_kdt45),
    BQ(R.drawable.chess_qdt45),
    BR(R.drawable.chess_rdt45),
    BB(R.drawable.chess_bdt45),
    BN(R.drawable.chess_ndt45),
    BP(R.drawable.chess_pdt45),

    WK(R.drawable.chess_klt45),
    WQ(R.drawable.chess_qlt45),
    WR(R.drawable.chess_rlt45),
    WB(R.drawable.chess_blt45),
    WN(R.drawable.chess_nlt45),
    WP(R.drawable.chess_plt45)
}

val pieces = Piece.values()

val testPieces: List<Piece?> = (0..63).map(pieces::getOrNull)

class BlankFragment2 : Fragment() {

    private val model: Blank2ViewModel by viewModels<Blank2ViewModel> {
        val state = Blank2State(false, testPieces)
        Blank2ViewModelFactory(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Board()
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

@Preview
@Composable
fun PreviewBoard() {
    Board()
}


@Composable
fun Board() {
    Column(Modifier.fillMaxWidth().fillMaxHeight()) {
        WithConstraints {
            /*val boardSize =
                (min(constraints.maxHeight, constraints.maxWidth)
                    .toFloat() / 3).dp
            val squareSize = boardSize / 8*/
            Column {
                (1..8).forEach { r -> // rows
                    Row {
                        (1..8).forEach { c -> // columns
                            val color =
                                if (c.rem(2) == r.rem(2))
                                    Color.LightGray
                                else Color.White
                            Box(
                                modifier =
                                    Modifier.weight(1f)
                                            .background(color)
                            ) {
                                Image(
                                    vectorResource(id = R.drawable.chess_bdt45),
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
