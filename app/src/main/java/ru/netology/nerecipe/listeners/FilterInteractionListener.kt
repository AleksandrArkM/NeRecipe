package ru.netology.nerecipe.listeners

interface FilterInteractionListener {

    fun statusCheckBox(category: String): Boolean
    fun filterOn(category: String)
    fun filterOff(category: String)

}