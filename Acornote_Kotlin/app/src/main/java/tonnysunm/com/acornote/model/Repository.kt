package tonnysunm.com.acornote.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class Repository(private val application: Application) {

    private val folderDao: FolderDao by lazy {
        AppRoomDatabase.getDatabase(application).folderDao()
    }
    private val noteDao: NoteDao by lazy { AppRoomDatabase.getDatabase(application).noteDao() }

    suspend fun <T : SQLEntity> insert(entity: T): Long {
        if (entity is Folder) {
            return folderDao.insert(entity)
        } else if (entity is Note) {
            return noteDao.insert(entity)
        }


        throw IllegalArgumentException("type is not right.")
    }

    suspend fun <T : SQLEntity> update(entity: T, updatedAt: Long = Date().time) {
        if (entity is Folder) {
            entity.updatedAt = updatedAt
            folderDao.update(entity)
        } else if (entity is Note) {
            entity.updatedAt = updatedAt
            noteDao.update(entity)
        }

    }

    // Folder
    val folders = folderDao.getFolders()

    fun getFolder(id: Long?): LiveData<Folder> {
        if (id != null) {
            return folderDao.getFolder(id)
        }

        return MutableLiveData(Folder(title = ""))
    }


    // Note
    fun notes(filter: NoteFilter) = when (filter) {
        is NoteFilter.All -> {
            Log.d("ROOM", "get all")
            noteDao.getAll()
        }
        is NoteFilter.Favourite -> {
            Log.d("ROOM", "get favourite")
            noteDao.getFavourite()
        }
        is NoteFilter.ByFolder -> {
            Log.d("ROOM", "get folder by " + filter.id)
            noteDao.getByFolder(filter.id)
        }
    }

    fun getNote(id: Long?): LiveData<Note> {
        if (id != null) {
            val liveData = noteDao.note(id)
            if (liveData.value != null) {
                return liveData
            }
        }

        return MutableLiveData(Note(title = "", folderId = null))
    }

    fun notesAllCount() = noteDao.notesAllCount()

    fun notesFavouriteCount() = noteDao.notesFavouriteCount()

}

