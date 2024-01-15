package com.example.prm4

interface Navigable {
    enum class Destination {
        List, Add, Edit, Info, Photo
    }
    fun navigate(to: Destination)
}