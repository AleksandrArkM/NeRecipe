package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.recipe.Recipe
import ru.netology.nerecipe.recipe.Step

interface RecipeRepository {

    val data: LiveData<List<Recipe>>
    fun getNextIndexId(): Long
    fun delete(recipeId: Long)
    fun save(recipe: Recipe)
    fun updateAfterMove(from: Long, to: Long, fromId: Long, toId: Long)
    fun deleteStep(step: Step)
    fun saveStep(step: Step)
    fun addFavorite(recipeId: Long)
    fun filterList(filters: MutableSet<String>?): LiveData<List<Recipe>>

    companion object {
        const val NEW_RECIPE_ID = 0L
        const val NEW_STEP_ID = 0L
    }
}