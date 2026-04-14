package com.example.jobsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.jobsapp.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobApp()
        }
    }
}

@Composable
fun JobApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {

        composable("welcome") {
            WelcomeScreen(navController)
        }

        composable("auth") {
            AuthScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("signup") {
            SignupScreen(navController)
        }

        composable("home") {
            HomeScreen()
        }
    }
}