package tonnysunm.com.acornote.ui.colortag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.ColorTag
import tonnysunm.com.acornote.model.Repository

private val TAG = "ColorTagViewModel"

class ColorTagViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository: Repository by lazy { Repository(application) }

    val data: LiveData<List<ColorTag>> by lazy {
        repository.colorTagDao.getAll()
    }

    fun saveColorTags() {
        data.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                repository.colorTagDao.update(it)
            }
        }

    }
}
