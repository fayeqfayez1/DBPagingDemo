package com.example.paginationdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadStateAdapter
import androidx.paging.map
import com.example.paginationdemo.databinding.ActivityMainBinding
import com.example.paginationdemo.db.ItemDao
import com.example.paginationdemo.db.ItemDatabase
import com.example.paginationdemo.pagination.MainLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: ItemDao
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(dao) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dao = ItemDatabase.getInstance(this).itemDao()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = MainAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            MainLoadStateAdapter()
        )

        lifecycleScope.launch {
            viewModel.data.collectLatest {
                Log.e("viewModel.data","${it.map { it.name }}")
                adapter.submitData(it)
            }
        }
    }
}