package com.github.ursteiner.todofx.model

data class Category(val name: String, var id: Int){

    //overwritten for JavaFX dropdowns
    override fun toString(): String {
        return name
    }
}