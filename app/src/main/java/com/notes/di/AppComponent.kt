package com.notes.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.notes.data.NoteDatabase
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Singleton
//@Component(
 //   modules = [
 //       AppModule::class,
 //   ]
//)
//interface AppComponent {

    //@Component.Factory
   // interface Factory {
  //      fun create(
          //  @BindsInstance application: Application,
  //      ): AppComponent
  //  }

 //   fun getNoteDatabase(): NoteDatabase

//}

@Module(
    includes = [
      //  AppModule.Binding::class
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

    //@Module
    //interface Binding {

     //   @Binds
    //    fun bindContext(application: Application): Context

   // }

}