package com.example.observablescomparison

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.observablescomparison.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            bLiveData.setOnClickListener {
                viewModel.triggerLiveData()
            }
            bStateFlow.setOnClickListener {
                viewModel.triggerStateFlow()
            }
            bFlow.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.triggerFlow().collectLatest {
                        binding.tvFlow.text = it
                    }
                }
            }
            bSharedFlow.setOnClickListener {
                viewModel.triggerSharedFlow()
            }
        }
        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        viewModel.liveData.observe(
            this
        ) {
            binding.tvLiveData.text = it
        }
        //never use launch to collect from state flow
        //hot flow because it`s emit smth even if it`s no collectors
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                binding.tvStateFlow.text = it
            }
            viewModel.sharedFlow.collectLatest {
                binding.tvSharedFlow.text = it
                Toast.makeText(this@MainActivity, "Shared flow", Toast.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest {
                binding.tvSharedFlow.text = it
                Toast.makeText(this@MainActivity, "Shared flow", Toast.LENGTH_SHORT).show()
            }
        }
    }
}