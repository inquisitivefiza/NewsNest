package com.example.newsnest.ui.theme



import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val NewsNestShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),   // pills, chips
    small      = RoundedCornerShape(8.dp),   // thumbnails
    medium     = RoundedCornerShape(12.dp),  // cards
    large      = RoundedCornerShape(16.dp),  // hero card, bottom sheet
    extraLarge = RoundedCornerShape(20.dp),
)