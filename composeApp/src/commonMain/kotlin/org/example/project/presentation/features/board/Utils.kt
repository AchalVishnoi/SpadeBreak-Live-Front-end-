package org.example.project.presentation.features.board

import androidx.compose.ui.geometry.Offset
import org.example.project.presentation.features.board.presentation.Seat

fun getRotationForSeat(seat: Seat): Float {
    return when (seat) {
        Seat.BOTTOM -> 0f
        Seat.LEFT -> 90f
        Seat.TOP -> 180f
        Seat.RIGHT -> 270f
    }
}

fun getTargetSlot(center: Offset, seat: Seat): Offset {
    val cardOffset = 60f
    return when (seat) {
        Seat.BOTTOM -> center.copy(y = center.y + cardOffset)
        Seat.TOP -> center.copy(y = center.y - cardOffset)
        Seat.LEFT -> center.copy(x = center.x - cardOffset)
        Seat.RIGHT -> center.copy(x = center.x + cardOffset)
    }
}

