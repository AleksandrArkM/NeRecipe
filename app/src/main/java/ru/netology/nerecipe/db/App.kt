package ru.netology.nerecipe.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database

@Database(
    entities = [RecipeEntity::class, StepEntity::class],
    version = 1
)

abstract class App: RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        @Volatile
        private var inst: App? = null

        fun getInstance(context: Context): App {
            return inst ?: synchronized(this) {
                inst ?: buildDatabase(context).also { inst = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context, App::class.java, "app.db"
            ).allowMainThreadQueries()
                .build()
    }
}