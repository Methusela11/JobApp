package com.example.jobsapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SignupScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(20.dp)) {

        Text("Signup Screen")

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate("login")
        }) {
            Text("Back to Login")
        }
    }
}