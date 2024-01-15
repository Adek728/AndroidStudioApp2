package com.example.prm4.photo.Model

import android.graphics.Path

data class PathWithSettings (
    val path: Path = Path(),
    val settings: Settings
)