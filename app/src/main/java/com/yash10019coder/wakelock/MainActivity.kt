package com.yash10019coder.wakelock

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yash10019coder.wakelock.ui.theme.WakeLockTheme

class MainActivity : ComponentActivity() {
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WakeLockTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        notificationHelper = NotificationHelper(this)
        notificationHelper.showNotification("WakeLock", "WakeLock is running...", 1)
        val wakeLockIntentService = Intent(this, WakeLockService::class.java)
        startService(wakeLockIntentService)
    }

    override fun onResume() {
        super.onResume()
        notificationHelper.showNotification("WakeLock", "WakeLock is running onResume...", 1)
    }

    override fun onStart() {
        super.onStart()
        notificationHelper.showNotification("WakeLock", "WakeLock is running onStart...", 1)
    }

    override fun onRestart() {
        super.onRestart()
        notificationHelper.showNotification("WakeLock", "WakeLock is running onRestart...", 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationHelper.showNotification("WakeLock", "WakeLock is running onDestroy...", 1)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WakeLockTheme {
        Greeting("Android")
    }
}
