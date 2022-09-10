package ru.netology.nerecipe.recipe

import kotlinx.serialization.Serializable

@Serializable
data class Step(
    val idStep: Long,
    val idStepRecipe: Long,
    val stepText: String,
    val stepPicture: String = ""
)