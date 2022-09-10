package ru.netology.nerecipe.db

import ru.netology.nerecipe.recipe.Recipe
import ru.netology.nerecipe.recipe.Step

internal fun StepsRecipe.toRecipe() = Recipe(
    id = recipe.id,
    author = recipe.author,
    title = recipe.title,
    category = recipe.category,
    content = step.map {it.toStep()},
    isFavorite = recipe.isFavorite,
    idPosition = recipe.indexNumber
)

internal fun Recipe.toEntity() = RecipeEntity(
    id = id,
    author = author,
    title = title,
    category = category,
    isFavorite = isFavorite,
    indexNumber = idPosition
)

internal fun StepEntity.toStep() = Step(
    idStep = idStep,
    idStepRecipe = idRecipe,
    stepText = stepText,
    stepPicture = picture
)

internal fun Step.toEntity() = StepEntity(
    idStep = idStep,
    idRecipe = idStepRecipe,
    stepText = stepText,
    picture = stepPicture
)

