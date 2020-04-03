package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import tonnysunm.com.acornote.model.NoteFilter as NoteFilter


@Dao
interface NoteDao {

    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * from note_table ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getAll(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table  WHERE star == 1 ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getStar(): DataSource.Factory<Int, Note>

    @Query("SELECT * from note_table WHERE label_id == :id ORDER BY pinned DESC, `order` DESC, updated_at DESC")
    fun getByLabel(id: Long): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Note): Long

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * from note_table WHERE id = :id LIMIT 1")
    fun note(id: Long): LiveData<Note>

    @Query("SELECT count(*) from note_table")
    fun notesAllCount(): LiveData<Int>

    @Query("SELECT count(*) from note_table")
    fun notesCount(): Int

    @Query("SELECT count(*) from note_table WHERE star == 1")
    fun notesStarCount(): LiveData<Int>
}


sealed class NoteFilter(val title: String) {
    object All : NoteFilter("All")

    object Star : NoteFilter("Star")

    data class ByLabel(val id: Long, val labelTitle: String) : NoteFilter(labelTitle)

//    val title: String
//        get() = when (this) {
//            All -> "All"
//            Star -> "Star"
//            is ByLabel ->
//        }

    val labelId: Long?
        get() = when (this) {
            is ByLabel -> this.id
            else -> null
        }
}
