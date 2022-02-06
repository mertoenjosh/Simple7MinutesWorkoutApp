package com.mertoenjosh.a7minuteworkout

class ExerciseModel(
    private var id: Int,
    private var name: String,
    private var image: Int,
    private var isCompleted: Boolean,
    private var isSelected: Boolean
) {
    fun getId(): Int = id

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String = name

    fun setName(name: String) {
        this.name = name
    }

    fun getImage(): Int = image

    fun setImage(image: Int) {
        this.image = image
    }

    fun getIsCompleted(): Boolean = isCompleted

    fun setIsCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }


    fun getIsSelected(): Boolean = isSelected

    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }



}