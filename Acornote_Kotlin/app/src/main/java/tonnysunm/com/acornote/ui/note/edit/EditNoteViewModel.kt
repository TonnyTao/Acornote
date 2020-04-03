package tonnysunm.com.acornote.ui.note.edit

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.Repository
import java.lang.IllegalStateException


class EditNoteViewModelFactory(
    private val application: Application,
    private val id: Long?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditNoteViewModel(application, id) as T
}

class EditNoteViewModel(
    application: Application,
    private val id: Long?
) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val noteLiveData: LiveData<Note> by lazy {
        repository.getNote(id)
    }

    val noteEditing = NoteEditing()

    suspend fun updateOrInsertNote(
        lifecycle: LifecycleOwner,
        labelId: Long,
        star: Boolean,
        title: String,
        description: String?
    ) {
        val note = noteLiveData.value ?: throw IllegalStateException("note is not set")

        note.labelId = if (labelId > 0) labelId else null

        note.title = title
        note.description = description
        note.star = star

        if (id == null) {
            repository.notesAllCount().observe(lifecycle, Observer {
                note.order = (it + 1).times(1_000.0)

                viewModelScope.launch {
                    repository.insert(note)
                }
            })
        } else {
            note.id = id
            repository.update(note)
        }
    }


    inner class NoteEditing {
        val title = MutableLiveData<String>()
        val description = MutableLiveData<String>()
    }
}
