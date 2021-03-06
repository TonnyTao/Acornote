package tonnysunm.com.acornote.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "color_tag_table"
)
data class ColorTag(
    @PrimaryKey
    var color: String,

    var name: String
) {

    companion object {
        val colorNames = mapOf(
            "#000000" to "Black",
            "#A4A4A4" to "Gray",
            "#56D769" to "Green",
            "#3D94FE" to "Blue",
            "#FFAA47" to "Orange",
            "#BF75E5" to "Purple",
            "#FF625C" to "Red",
            "#FFD64B" to "Yellow"
        )
    }

    @Ignore
    val defaultName = colorNames[color.toUpperCase(Locale.ROOT)]

}