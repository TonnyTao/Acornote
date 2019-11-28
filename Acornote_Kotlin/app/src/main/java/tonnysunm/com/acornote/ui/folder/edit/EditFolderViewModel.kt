package tonnysunm.com.acornote.ui.folder

import android.app.Application
import androidx.lifecycle.*
import tonnysunm.com.acornote.model.Folder
import tonnysunm.com.acornote.model.Repository
import java.lang.IllegalStateException

class EditFolderViewModelFactory(
    private val application: Application,
    private val folderId: Long?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditFolderViewModel(application, folderId) as T

}

class EditFolderViewModel(application: Application, private val folderId: Long?) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val folderLiveData: LiveData<Folder> by lazy {
        repository.getFolder(folderId)
    }

    val folderEditing = FolderEditing()

    suspend fun updateOrInsertFolder(title: String): Long {
        val folder = folderLiveData.value ?: throw IllegalStateException("folder is not set")

        folder.title = title

        return if (folderId == null) {
            repository.insert(folder)
        } else {
            folder.id = folderId
            repository.update(folder)
        }
    }

    inner class FolderEditing {
        val title = MutableLiveData<String>()

        val favourite = MutableLiveData<Boolean>()

        val flippable = MutableLiveData<Boolean>()
    }
}
