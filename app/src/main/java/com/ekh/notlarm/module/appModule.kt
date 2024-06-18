package com.ekh.notlarm.module

import android.app.Application
import androidx.room.Room
import com.ekh.notlarm.domain.repository.NoteRepository
import com.ekh.notlarm.dataBasePackage.repoImp.NoteRepoImplementation
import com.ekh.notlarm.dataBasePackage.source.NoteDataBase
import com.ekh.notlarm.domain.useCases.AddNoteUseCase
import com.ekh.notlarm.domain.useCases.GetNote
import com.ekh.notlarm.domain.useCases.NotesUseCases
import com.ekh.notlarm.domain.useCases.SearchNotesUseCase
import com.ekh.notlarm.domain.useCases.deleteNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object appModule {
    @Provides
    @Singleton
    fun provideNoteDatabase( app: Application): NoteDataBase{
        return Room.databaseBuilder(
            app,
            NoteDataBase::class.java,
            NoteDataBase.DATABASE_NAME
        ).build()
    }
    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {
        return NotesUseCases(
            deleteNoteUseCase = deleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNote =  GetNote(repository),
            searchNotesUseCase = SearchNotesUseCase(repository)
        )

    }
    @Provides
    @Singleton
    fun provideNoteRepository( datab: NoteDataBase): NoteRepository {
        return NoteRepoImplementation(datab.noteDao)
    }

}