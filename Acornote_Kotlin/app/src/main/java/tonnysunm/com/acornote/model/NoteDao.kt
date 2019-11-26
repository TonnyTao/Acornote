package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import tonnysunm.com.acornote.model.NoteFilter as NoteFilter


@Dao
interface NoteDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_table ORDER BY updated_at DESC")
    fun getAll(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table WHERE favourite == 1 ORDER BY updated_at DESC")
    fun getFavourite(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table WHERE folder_id == :id ORDER BY updated_at DESC")
    fun getByFolder(id: Long): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Note): Long

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * from note_table WHERE id = :id LIMIT 1")
    fun note(id: Long): LiveData<Note>
}


sealed class NoteFilter(val title: String) {
    object All : NoteFilter("All")

    object Favourite : NoteFilter("Favourite")

    data class ByFolder(val id: Long, val folderTitle: String) : NoteFilter(folderTitle)

//    val title: String
//        get() = when (this) {
//            All -> "All"
//            Favourite -> "Favourite"
//            is ByFolder ->
//        }

    val folderId: Long?
        get() = when (this) {
            is ByFolder -> this.id
            else -> null
        }
}