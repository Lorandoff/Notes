package com.notes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDao
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import javax.inject.Inject
@HiltViewModel
class NoteListViewModel @Inject constructor(
   // private val noteDao : NoteDao,
    private val noteDatabase: NoteDatabase
) : ViewModel() {
    private val _errorDataValidate = MutableLiveData<String?>()
    val errorDataValidate : LiveData<String?> = _errorDataValidate
    private val _errorDataTitle = MutableLiveData<Boolean?>()
    val errorDataTitle : LiveData<Boolean?> = _errorDataTitle

    private val _finish = MutableLiveData<Unit?>()
    val finish : LiveData<Unit?> = _finish

    private val _notes = MutableLiveData<List<NoteListItem>?>()
    val notes: LiveData<List<NoteListItem>?> = _notes

    private val _navigateToNoteCreation = MutableLiveData<Unit?>()
    val navigateToNoteCreation: LiveData<Unit?> = _navigateToNoteCreation

    fun getList(){
        viewModelScope.launch(Dispatchers.IO) {
            _notes.postValue(
                noteDatabase.noteDao.getAll().map {
                    it.convertToNoteListItem()
                }
            )
        }
    }
    fun sortList(){
        if (_notes != null) {
            viewModelScope.launch(Dispatchers.IO) {
                _notes.postValue(
                    noteDatabase.noteDao.sortByModifiedASC().map {
                        it.convertToNoteListItem()
                    }
                )
            }
        }
    }
    fun insertNotetoDatabase(title: String, content: String){
       viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao.insertAll(
                NoteDbo(
                    0, title, content,
                    LocalDateTime.now(), LocalDateTime.now()
                )
            )
        finishWork()
        }
        //_finish.value = true
        //    _errorDataValidate.value = "Error data validate, please enter right text"

    }
    fun deledeNoteFromDatabase(noteDbo: NoteListItem){
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao.DeleteNote(noteDbo.convertToNoteDbo())
            getList()
        }
    }
    fun onCreateNoteClick() {
        _navigateToNoteCreation.postValue(Unit)
    }
    private fun ParseTitle(title : String?) : String{
        return title?.trim() ?: ""
    }
    private fun ParseId(id : String?) : Int {
        return try {
            id?.trim()?.toInt() ?: 0
        }catch (e : Exception) {
            0
        }
    }
    private fun validateInput(title: String, content: String) : Boolean {
        var result = true
        if (title.isBlank() ) {
            _errorDataTitle.value = true
            result = false
        }
        if (content.isBlank()) {
            //_finish.value = true
            result = false
        }
        return result
    }
    fun resDataInfoTitleError(){
        _errorDataTitle.value = false
    }
    fun resDataInfoContentError(){
  //      _finish.value = false
    }
    private fun finishWork(){
        _finish.postValue( Unit)
    }
}

data class NoteListItem(
    val id: Long,
    val title: String,
    val content: String,
){
    fun convertToNoteDbo() : NoteDbo{
        return NoteDbo(
            id = id,
            title = title,
            content = content,
            createdAt = LocalDateTime.now(),
            modifiedAt = LocalDateTime.now()
        )
    }
}