package tonnysunm.com.acornote

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.invoke
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.app_bar_navigation.*
import tonnysunm.com.acornote.model.NoteFilter
import tonnysunm.com.acornote.service.BubbleService
import tonnysunm.com.acornote.ui.note.NoteActivity


class HomeActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        var scrollToTop = false
    }

    private val homeSharedModel: HomeSharedViewModel by lazy {
        ViewModelProvider(this).get(HomeSharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //set drawer icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(DrawerArrowDrawable(this))

        homeSharedModel.noteFilterLiveData.observe(this, Observer {
            titleView.text = it.title

            if (it is NoteFilter.ByColorTag) {
                colorTagView.colorString = it.colorTag.color
                colorTagView.visibility = View.VISIBLE
            } else {
                colorTagView.visibility = View.GONE
            }
        })

        //fab
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val startForResult = prepareCall(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                }
            }

            startForResult(Intent(this, NoteActivity::class.java).apply {
                when (val noteFilter = homeSharedModel.noteFilterLiveData.value) {
                    NoteFilter.Star ->
                        putExtra(getString(R.string.starKey), true)
                    is NoteFilter.ByLabel ->
                        putExtra(getString(R.string.labelIdKey), noteFilter.labelId)
                }

            })
        }

        showOverlayIfNecessary()
    }

    private fun showOverlayIfNecessary() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val overlayEnabled = prefs.getBoolean("settings_overlay", false)

        if (overlayEnabled && Settings.canDrawOverlays(this)) {
            startService(Intent(this, BubbleService::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        fun navigateUp(): Boolean {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navController = findNavController(R.id.nav_host_fragment)

            val currentDestination = navController.currentDestination ?: return false

            return if (R.id.nav_label == currentDestination.id) {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            } else {
                navController.navigateUp()
            }
        }

        return navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed()
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}
