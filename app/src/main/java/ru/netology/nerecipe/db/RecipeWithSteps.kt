package ru.netology.nerecipe.db

import androidx.room.Embedded
import androidx.room.Relation

class StepsRecipe(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "idRecipe"
    )
    val step: List<StepEntity>
)