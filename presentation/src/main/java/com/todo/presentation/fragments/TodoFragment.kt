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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.todo.common.EXTRA_GLOBAL_TODO_ACTION
import com.todo.common.EXTRA_LOCAL_TODO_ACTION
import com.todo.common.GLOBAL_TODO_ACTION
import com.todo.common.LOCAL_TODO_ACTION1
import com.todo.common.LOCAL_TODO_ACTION2
import com.todo.common.Utils
import com.todo.common.helper.MessageEvent
import com.todo.common.helper.SampleEventBus
import com.todo.domain.models.TodoItem
import com.todo.presentation.R
import com.todo.presentation.adapter.TodoAdapter
import com.todo.presentation.broadcast.LocalBroadCast
import com.todo.presentation.broadcast.TodoBroadCastListener
import com.todo.presentation.databinding.FragmentTodoBinding
import com.todo.presentation.viewModels.ModelClass
import com.todo.presentation.viewModels.TodoViewModel
import com.todo.presentation.viewModels.TodoViewModel.Companion.DataEvent
import com.todo.presentation.viewModels.TodoViewModel2
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class TodoFragment : Fragment(), TodoAdapter.TodoListener {

    private val todoViewModel: TodoViewModel by viewModels()

    private lateinit var todoViewModel2: TodoViewModel2

//    private val todoViewModel2: TodoViewModel2 by activityViewModels()
    private lateinit var binding: FragmentTodoBinding
    private val localBroadCast: LocalBroadCast by lazy {
        LocalBroadCast(object : TodoBroadCastListener {
            override fun onLocalTodoReceived(message: String?) {
                message?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private val sampleEventBus = SampleEventBus<MessageEvent>()

    private val adapter: TodoAdapter by lazy { TodoAdapter() }

    @Inject
    lateinit var modelClass: ModelClass

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val layout = inflater.inflate(R.layout.fragment_todo, container, false)
        binding = FragmentTodoBinding.bind(layout)
        Log.d("TodoFragment", "onCreateView")
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TodoFragment", "onViewCreated")
        todoViewModel.getTodoItems()
        todoViewModel2 = ViewModelProvider(requireActivity()).get(TodoViewModel2::class.java)

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
            TodoItem(1, "Buy groceries", Utils.TodoStatus.PENDING.name),
            TodoItem(2, "Read a book", Utils.TodoStatus.PENDING.name)
            // Add more todos as needed
        )

//        val sampleUpdateList = TodoItem(3, "Eat FOOD", Utils.TodoStatus.COMPLETED.name)
//        todoViewModel.addTodoItem(sampleUpdateList)

        adapter.setListener(this)
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
        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
    }

    private fun initBroadCast() {
//        localBroadCast = LocalBroadCast(object : TodoBroadCastListener {
//            override fun onLocalTodoReceived(message: String?) {
//                message?.let {
//                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

//        if (::localBroadCast.isInitialized) {
        val localFilter = IntentFilter().apply {
            addAction(LOCAL_TODO_ACTION1)
            addAction(LOCAL_TODO_ACTION2)
        }
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(localBroadCast, localFilter)
//        }

//        val globalBroadCastReceiver = GlobalBroadCastReceiver()
//        requireContext().registerReceiver(globalBroadCastReceiver, IntentFilter(GLOBAL_TODO_ACTION), Context.RECEIVER_EXPORTED)
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
            val localIntentFilter = Intent(LOCAL_TODO_ACTION1)
            localIntentFilter.putExtra(EXTRA_LOCAL_TODO_ACTION, "Local: ${it[0].task}")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(localIntentFilter)

            val globalIntent = Intent(GLOBAL_TODO_ACTION)
            globalIntent.putExtra(EXTRA_GLOBAL_TODO_ACTION, "Global: ${it[0].task}")
            requireActivity().sendBroadcast(globalIntent)

            sampleEventBus.post(MessageEvent(it[0].task))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Collect values from stateFlow
                launch {
                    todoViewModel.sharedFlow.collect { value ->
                        Log.d("SharedFlow", "Collector 1 received: $value")
                    }
                }
                launch {
                    todoViewModel.stateFlow.collect { value ->
                        Log.d("StateFlow", "Collector 1 received: $value")
                    }
                }
// Collect values from stateFlow
                launch {
                    delay(2000)
                    todoViewModel.sharedFlow.collect { value ->
                        Log.d("SharedFlow", "Collector 2 received: $value")
                    }
                }
                launch {
                    delay(2000)
                    todoViewModel.stateFlow.collect { value ->
                        Log.d("Stateflow", "Collector 2 received: $value")
                    }
                }
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("TodoFragment", "onViewStateRestored")
    }

    override fun onStop() {
        super.onStop()
        sampleEventBus.unregister(MessageEvent::class.java) {}
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TodoFragment", "onDestroy")
//        if (::localBroadCast.isInitialized) {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(localBroadCast)
//        }
    }

    override fun onTodoItemClick(todoItem: TodoItem) {
        updateAnItemInDB(todoItem)
    }

    private fun updateAnItemInDB(todoItem: TodoItem) {
        todoViewModel.updateTodoItem(todoItem)
    }
}
