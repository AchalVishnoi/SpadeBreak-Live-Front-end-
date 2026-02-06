package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import org.example.project.data.remote.socket.SocketEngine
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {

        super.onPause()
    }
}

@Composable
fun AppAndroidPreview() {
    App()
}

@Composable
fun PauseSocketEngine(){
    val socketEngine = getKoin().get<SocketEngine>()
}