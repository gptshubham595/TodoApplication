package com.todo.presentation.activities

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.todo.presentation.databinding.ActivityMainBinding
import com.todo.presentation.fragments.TodoFragment
import com.todo.presentation.service.BasicService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var basicService: BasicService

//    private lateinit var navController: NavController

    private var isServiceBound = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isServiceBound = true
            val binder = service as BasicService.BasicBinder
            basicService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        navController = findNavController(binding.navHostFragment)
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragments()
        initServices()
    }

    private fun initServices() {
        val intent = Intent(this, BasicService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun initFragments() {
        val todoFragment = TodoFragment()
        val transactionManager = supportFragmentManager.beginTransaction()
        transactionManager.add(binding.navHostFragment.id, todoFragment)
        transactionManager.addToBackStack("TODO_FRAGMENT")
        transactionManager.commit()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return super.onRetainCustomNonConfigurationInstance()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("MainActivity", "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("MainActivity", "onRestoreInstanceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }
}
