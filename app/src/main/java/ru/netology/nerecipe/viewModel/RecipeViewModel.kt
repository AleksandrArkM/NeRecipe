package ru.netology.nerecipe.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import SingleLiveEvent
import ru.netology.nerecipe.recipe.Recipe
import ru.netology.nerecipe.recipe.Step
import android.app.Application
import ru.netology.nerecipe.listeners.RecipeInteractionListener
import ru.netology.nerecipe.listeners.StepInteractionListener
import ru.netology.nerecipe.listeners.FilterInteractionListener
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.db.App
import java.util.*

class RecipeViewModel(
    application: Application
) : AndroidViewModel(application), RecipeInteractionListener, StepInteractionListener,
    FilterInteractionListener {

    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = App.getInstance(context = application).recipeDao
    )

    val data by repository::data

    private val currentRecipe = MutableLiveData<Recipe?>(null)
    private val currentStep = MutableLiveData<Step?>(null)

    val currentImageStep = MutableLiveData("")

    private val filters = MutableLiveData<MutableSet<String>?>(mutableSetOf())

    var filterResult = Transformations.switchMap(filters) { filter ->
        repository.filterList(filter)
    }

    val addAndEdit = SingleLiveEvent<Recipe>()
    val goToCurrentRecipe = SingleLiveEvent<Recipe>()
    val stepEdit = SingleLiveEvent<Step>()
    val stepAdd = SingleLiveEvent<String>()

    fun onSaveButtonClicked(
        category: String, title: String, content: List<Step>?
    ) {
        val recipeRecord = currentRecipe.value?.copy(
            category = category,
            content = content,
            title = title
        ) ?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            author = "Aleksandr",
            category = category,
            content = content,
            title = title,
            idPosition = repository.getNextIndexId()
        )
        repository.save(recipeRecord)
        currentRecipe.value = null
    }

    fun onSaveButtonStepClicked(textStep: String) {
        if (textStep.isBlank()) return

        val stepRecord = currentStep.value?.copy(
            stepText = textStep,
            stepPicture = currentImageStep.value.toString()
        ) ?: Step(
            idStep = RecipeRepository.NEW_STEP_ID,
            idStepRecipe = currentRecipe.value?.id ?: 0,
            stepText = textStep,
            stepPicture = currentImageStep.value.toString()
        )

        repository.saveStep(stepRecord)

        currentRecipe.value = null
        currentStep.value = null
        currentImageStep.value = ""
    }

    fun onAddClicked() {
        addAndEdit.call()
    }

    fun onAddStepClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        stepAdd.call()
    }

    fun updateAfterMove(from: Long, to: Long, fromId: Long, toId: Long) {
        repository.updateAfterMove(from, to, fromId, toId)
    }

    override fun onAddFavoriteClicked(recipe: Recipe) = repository.addFavorite(recipe.id)

    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id)

    override fun onEditClicked(recipe: Recipe) {
        currentRecipe.value = recipe
        addAndEdit.value = recipe
    }

    override fun onRecipeClicked(recipe: Recipe) {
        goToCurrentRecipe.value = recipe
    }

    override fun onRemoveStepClicked(step: Step) = repository.deleteStep(step)

    override fun onEditStepClicked(step: Step) {
        currentStep.value = step
        stepEdit.value = step
    }

    fun filterFavorite(recipes: List<Recipe>): List<Recipe> {
        return recipes.filter { it.isFavorite }
    }

    fun filterSearch(searchText: CharSequence?): MutableList<Recipe> {
        val filterRecipes = mutableListOf<Recipe>()
        val recipes = filterResult.value
        if (searchText?.isBlank() == true) {
            if (recipes != null) {
                filterRecipes.addAll(recipes)
            }
        } else if (recipes != null) {
            for (recipe in recipes) {
                if (
                    recipe.title
                        .lowercase(Locale.getDefault())
                        .contains(
                            searchText.toString().lowercase(Locale.getDefault())
                        )
                ) {
                    filterRecipes.add(recipe)
                }
            }
        }
        return filterRecipes
    }

    override fun statusCheckBox(category: String): Boolean {
        return filters.value?.contains(category) == true
    }

    override fun filterOn(category: String) {
        val filterList = filters.value
        filterList?.add(category)
        filters.value = filterList
    }

    override fun filterOff(category: String) {
        val filterList = filters.value
        filterList?.remove(category)
        filters.value = filterList
    }



}