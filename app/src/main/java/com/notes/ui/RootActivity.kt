package com.notes.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.notes.databinding.ActivityRootBinding
import com.notes.ui.list.NoteListFragment

//@AndroidEntryPoint
class RootActivity : AppCompatActivity(){

    private lateinit var viewBinding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityRootBinding.inflate(layoutInflater)
        this.viewBinding = viewBinding
        setContentView(viewBinding.root)
        supportFragmentManager
            .beginTransaction()
            .replace(
                viewBinding.container.id,
                NoteListFragment()
            )
            //.addToBackStack(null)
            .commit()
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            Log.d("fds", supportFragmentManager.backStackEntryCount.toString())
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}