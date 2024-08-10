package com.kotau.bootcounter.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kotau.bootcounter.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val notificationsPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                // TODO handle case when user denied permission once
            }

            else -> {
                // TODO handle case when user denied permission twice or more
            }
        }
    }

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isNotificationsPermissionGranted =
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

            if (!isNotificationsPermissionGranted) {
                notificationsPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        viewModel.bootEvents
            .onEach {
                binding.bootEventTextView.text = it
            }
            .launchIn(lifecycleScope)

        binding.dismissalsEditText.setOnEditorActionListener { v, _, _ ->
            viewModel.updateDismissCount(v.text.toString())
            val dismissals = v.text.toString()

            if (dismissals.isNotEmpty()) {
                viewModel.updateDismissCount(dismissals)
                true
            } else {
                false
            }
        }

        binding.intervalEditText.setOnEditorActionListener { v, _, _ ->
            val interval = v.text.toString()
            if (interval.isNotEmpty()) {
                viewModel.updateInterval(interval)
                true
            } else {
                false
            }
        }
    }
}