package com.example.todoapplication.presentation.fragments

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.R
import com.example.todoapplication.common.Constant.Companion.EXTRA_GLOBAL_TODO_ACTION
import com.example.todoapplication.common.Constant.Companion.EXTRA_LOCAL_TODO_ACTION
import com.example.todoapplication.common.Constant.Companion.GLOBAL_TODO_ACTION
import com.example.todoapplication.common.Constant.Companion.LOCAL_TODO_ACTION
import com.example.todoapplication.common.Utils
import com.example.todoapplication.common.helper.MessageEvent
import com.example.todoapplication.common.helper.SampleEventBus
import com.example.todoapplication.data.models.TodoItem
import com.example.todoapplication.databinding.FragmentTodoBinding
import com.example.todoapplication.presentation.adapter.TodoAdapter
import com.example.todoapplication.presentation.broadcast.LocalBroadCast
import com.example.todoapplication.presentation.broadcast.TodoBroadCastListener
import com.example.todoapplication.presentation.viewModels.DataEvent
import com.example.todoapplication.presentation.viewModels.TodoViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TodoFragment : Fragment() {
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var binding: FragmentTodoBinding
    private lateinit var localBroadCast: LocalBroadCast
    private val sampleEventBus = SampleEventBus()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val layout = inflater.inflate(R.layout.fragment_todo, container, false)
        binding = FragmentTodoBinding.bind(layout)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoViewModel = ViewModelProvider(requireActivity())[TodoViewModel::class.java]
        initView()
        initObserver()
        initBroadCast()

        sampleEventBus.register(MessageEvent::class.java){
            showToast(it)
        }

        EventBus.getDefault().register(this)
    }

    private fun showToast(it: MessageEvent) {

    }

    private fun initBroadCast() {
        localBroadCast = LocalBroadCast(object : TodoBroadCastListener {
            override fun onLocalTodoReceived(message: String?) {
                message?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        })

        if(::localBroadCast.isInitialized) {
            val localFilter = IntentFilter(LOCAL_TODO_ACTION)
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(localBroadCast, localFilter)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDataEvent(event: DataEvent) {
        val result = event.data
    }

    private fun initView() {
        val todoList = listOf(
            TodoItem(1, "Buy groceries", Utils.TodoStatus.PENDING),
            TodoItem(2, "Read a book", Utils.TodoStatus.PENDING),
            // Add more todos as needed
        )
        val recyclerView: RecyclerView = binding.todoRecyclerView
        val adapter = TodoAdapter()
        adapter.updateList(todoList)
        recyclerView.adapter = adapter
    }

    private fun initObserver() {
        todoViewModel.todoItemsListLiveData.observe(requireActivity(), Observer {
            Toast.makeText(requireContext(), it.task, Toast.LENGTH_SHORT).show()
            // send broadcast
            val localIntentFilter = Intent(LOCAL_TODO_ACTION)
            localIntentFilter.putExtra(EXTRA_LOCAL_TODO_ACTION, it.task)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntentFilter)

            val globalIntent = Intent(GLOBAL_TODO_ACTION)
            globalIntent.putExtra(EXTRA_GLOBAL_TODO_ACTION, it.task)
            requireActivity().sendBroadcast(globalIntent)

            sampleEventBus.post(MessageEvent(it.task))
        })
    }

    override fun onStop() {
        super.onStop()
        sampleEventBus.unregister(MessageEvent::class.java){}
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::localBroadCast.isInitialized) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(localBroadCast)
        }
    }
}