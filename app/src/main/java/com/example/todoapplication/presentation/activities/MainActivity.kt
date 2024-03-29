package com.example.todoapplication.presentation.activities

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapplication.R
import com.example.todoapplication.databinding.ActivityMainBinding
import com.example.todoapplication.presentation.fragments.TodoFragment
import com.example.todoapplication.presentation.service.BasicService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var basicService: BasicService

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initFragments()
        initServices()
    }

    private fun initServices() {
        val intent = Intent(this, BasicService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun initFragments() {
        val todoFragment = TodoFragment()
        val transactionManager = supportFragmentManager.beginTransaction()
        transactionManager.add(R.id.fragmentContainer, todoFragment)
        transactionManager.addToBackStack("TODO_FRAGMENT")
        transactionManager.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }
}
