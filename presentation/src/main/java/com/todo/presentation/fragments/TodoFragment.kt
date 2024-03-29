package com.todo.presentation.fragments

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.todo.todoapplication.R
import com.todo.todoapplication.common.Constant.Companion.EXTRA_GLOBAL_TODO_ACTION
import com.todo.todoapplication.common.Constant.Companion.EXTRA_LOCAL_TODO_ACTION
import com.todo.todoapplication.common.Constant.Companion.GLOBAL_TODO_ACTION
import com.todo.todoapplication.common.Constant.Companion.LOCAL_TODO_ACTION
import com.todo.todoapplication.common.Utils
import com.todo.todoapplication.common.helper.MessageEvent
import com.todo.todoapplication.common.helper.SampleEventBus
import com.todo.todoapplication.data.models.TodoItem
import com.todo.todoapplication.databinding.FragmentTodoBinding
import com.todo.presentation.adapter.TodoAdapter
import com.todo.presentation.broadcast.LocalBroadCast
import com.todo.presentation.broadcast.TodoBroadCastListener
import com.todo.presentation.viewModels.TodoViewModel.Companion.DataEvent
import com.todo.presentation.viewModels.ModelClass
import com.todo.presentation.viewModels.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class TodoFragment : Fragment() {

    private val todoViewModel: TodoViewModel by viewModels()

//    private val todoViewModel2: TodoViewModel2 by viewModels()
//    private val todoViewModel2: TodoViewModel2 by activityViewModels()
    private lateinit var binding: FragmentTodoBinding
    private lateinit var localBroadCast: LocalBroadCast
    private val sampleEventBus = SampleEventBus()

    private val adapter: TodoAdapter by lazy { TodoAdapter() }

    @Inject
    lateinit var modelClass: ModelClass

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val layout = inflater.inflate(R.layout.fragment_todo, container, false)
        binding = FragmentTodoBinding.bind(layout)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoViewModel.getTodoItems()
//        todoViewModel2.getTodoItems()
//        modelClass.getTodoItems()

        initView()
        initObserver()
        initBroadCast()
        initAdapter()
        initListener()

        sampleEventBus.register(MessageEvent::class.java) {
            showToast(it)
        }

        EventBus.getDefault().register(this)
    }

    private fun initAdapter() {
        val sampleTodoList = listOf(
            TodoItem(1, "Buy groceries", Utils.TodoStatus.PENDING),
            TodoItem(2, "Read a book", Utils.TodoStatus.PENDING)
            // Add more todos as needed
        )

        adapter.updateList(sampleTodoList)
        binding.todoRecyclerView.adapter = adapter
    }

    private fun initListener() {
        binding.sendBroadCast.setOnClickListener {
            Log.d("GlobalBroadcast", "Firing Global Broadcast")
//            val localIntentFilter = Intent(LOCAL_TODO_ACTION)
//            localIntentFilter.putExtra(EXTRA_LOCAL_TODO_ACTION, "Hello")
//            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntentFilter)
            val globalIntent = Intent(GLOBAL_TODO_ACTION)
            globalIntent.putExtra(EXTRA_GLOBAL_TODO_ACTION, "Hello $GLOBAL_TODO_ACTION")
            requireActivity().sendBroadcast(globalIntent)
        }
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

        if (::localBroadCast.isInitialized) {
            val localFilter = IntentFilter(LOCAL_TODO_ACTION)
            LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(localBroadCast, localFilter)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDataEvent(event: DataEvent) {
        val result = event.data
    }

    private fun initView() {
    }

    private fun initObserver() {
        todoViewModel.todoItemsListLiveData.observe(viewLifecycleOwner) {
            Log.d("TodoViewModel", "observed getTodoItems: $it")
            adapter.addAllItemToList(it)
            Toast.makeText(requireContext(), it?.get(0)?.task, Toast.LENGTH_SHORT).show()
            // send broadcast
            Log.d("LocalBroadcast", "Firing Local Broadcast")
            val localIntentFilter = Intent(LOCAL_TODO_ACTION)
            localIntentFilter.putExtra(EXTRA_LOCAL_TODO_ACTION, it[0].task)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntentFilter)

            val globalIntent = Intent(GLOBAL_TODO_ACTION)
            globalIntent.putExtra(EXTRA_GLOBAL_TODO_ACTION, it[0].task)
            requireActivity().sendBroadcast(globalIntent)

            sampleEventBus.post(MessageEvent(it[0].task))
        }
    }

    override fun onStop() {
        super.onStop()
        sampleEventBus.unregister(MessageEvent::class.java) {}
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::localBroadCast.isInitialized) {
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(localBroadCast)
        }
    }
}
