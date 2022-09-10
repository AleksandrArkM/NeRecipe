package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.RecipeStepsBinding
import ru.netology.nerecipe.listeners.StepInteractionListener
import ru.netology.nerecipe.recipe.Step
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil


internal class StepsAdapter(
    private val interactionListener: StepInteractionListener
) : ListAdapter<Step, StepsAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(
        private val binding: RecipeStepsBinding,
        listener: StepInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var step: Step

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.optionsStep).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveStepClicked(step)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditStepClicked(step)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.optionsStep.setOnClickListener { popupMenu.show() }
        }

        fun bind(step: Step) {
            this.step = step

            with(binding) {
                stepContent.text = step.stepText
                stepPicture.isVisible = step.stepPicture.isNotBlank()
                if (step.stepPicture.isNotBlank()) stepPicture.setImageURI(
                    step.stepPicture.toUri()
                )
                stepNumber.text = (absoluteAdapterPosition + 1).toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeStepsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stepItem = differ.currentList[position]
        holder.bind(stepItem)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Step>() {

        override fun areItemsTheSame(oldItem: Step, newItem: Step) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Step, newItem: Step) =
            oldItem.stepText == newItem.stepText

    }

    val differ = AsyncListDiffer(this, DiffCallback)

}