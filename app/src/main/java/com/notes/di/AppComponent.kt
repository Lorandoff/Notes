package com.notes.di

import android.content.Context
import androidx.room.Room
import com.notes.data.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(
    includes = [
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    //@ViewModelScoped
    @Singleton
    fun provideNoteDatabase(
       @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        NoteDatabase::class.java, "database-note.db"
    ).createFromAsset("database-note.db")
        .build()


}