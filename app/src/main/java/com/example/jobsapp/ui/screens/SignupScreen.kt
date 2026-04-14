package com.example.jobsapp.ui.screens

import com.example.jobsapp.auth.FirebaseInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SignupScreen(navController: NavHostController) {

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var selectedRole by remember { mutableStateOf("Job Seeker") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = fullName, onValueChange = { fullName = it },
            label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = phone, onValueChange = { phone = it },
            label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(10.dp))

        // PASSWORD
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = "Toggle Password"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // CONFIRM PASSWORD
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation =
                if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    showConfirmPassword = !showConfirmPassword
                }) {
                    Icon(
                        imageVector = if (showConfirmPassword)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = "Toggle Confirm Password"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Select Role", fontWeight = FontWeight.SemiBold)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedRole == "Job Seeker",
                    onClick = { selectedRole = "Job Seeker" }
                )
                Text("Job Seeker")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedRole == "Employer",
                    onClick = { selectedRole = "Employer" }
                )
                Text("Employer")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                // 1. Basic validation
                if (fullName.isEmpty() ||
                    phone.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty()
                ) {
                    return@Button
                }

                if (password != confirmPassword) {
                    return@Button
                }

                // 2. Create Firebase account
                FirebaseInstance.auth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            val uid = task.result.user?.uid

                            val userMap = hashMapOf(
                                "fullName" to fullName,
                                "phone" to phone,
                                "email" to email,
                                "role" to selectedRole,
                                "uid" to uid
                            )

                            // 3. Save user in Firestore
                            if (uid != null) {
                                FirebaseInstance.db.collection("users")
                                    .document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener {

                                        // 4. Navigate to login AFTER success
                                        navController.navigate("login") {
                                            popUpTo("signup") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener {
                                        println("Firestore error: ${it.message}")
                                    }
                            }

                        } else {
                            println("Auth error: ${task.exception?.message}")
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFE6B36A))
        ) {
            Text("Create Account", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text("Already have an account? Login")
        }
    }
}