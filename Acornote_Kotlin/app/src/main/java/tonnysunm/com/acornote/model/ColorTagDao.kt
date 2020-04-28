package tonnysunm.com.acornote.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ColorTagDao : BaseDao<ColorTag> {
    // Room executes all queries on a separate thread. So there is no suspend.
    @Query("SELECT * FROM color_tag_table ORDER BY id DESC")
    fun getAll(): LiveData<List<ColorTag>>

    @Query("SELECT count(*) from color_tag_table")
    suspend fun notesCount(): Int

    @Insert
    suspend fun insert(entities: List<ColorTag>)

    @Update
    suspend fun update(entities: List<ColorTag>)
}