package com.example.proje

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun RegisterScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Arka plan resmi
        Image(
            painter = painterResource(id = R.drawable.register), // background adlı resmi /res/drawable/ içine koyduğunuzdan emin olun
            contentDescription = "Arka Plan",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Form içeriği
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Kayıt Ol", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Adı Almak için Giriş Kutusu
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Ad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Soyadı Almak için Giriş Kutusu
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Soyad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Kullanıcı Adı Almak için Giriş Kutusu
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Kullanıcı Adı") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Şifre Almak için Giriş Kutusu
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Kayıt Ol Butonu
            Button(
                onClick = {
                    if (firstName.isNotEmpty() && lastName.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                        // Kayıt işlemi için verileri gönder
                        CoroutineScope(Dispatchers.IO).launch {
                            registerUser(firstName, lastName, username, password, context, navController)
                        }
                    } else {
                        Toast.makeText(context, "Lütfen tüm alanları doldurun.", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kayıt Ol")
            }
        }
    }
}

fun registerUser(
    firstName: String,
    lastName: String,
    username: String,
    password: String,
    context: Context,
    navController: NavController
) {
    val client = OkHttpClient()
    val requestBody = """
        {
            "Name": "$firstName",
            "SurName": "$lastName",
            "Username": "$username",
            "Password": "$password"
        }
    """.trimIndent().toRequestBody()

    val request = Request.Builder()
        .url("http://10.0.2.2:5138/api/User/register") // Emülatör için localhost
        .post(requestBody)
        .addHeader("Content-Type", "application/json")
        .build()


    try {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show()
                navController.navigate("login") // Kayıttan sonra giriş ekranına yönlendirme
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Kayıt başarısız: ${response.message}", Toast.LENGTH_LONG).show()
            }
        }
    } catch (e: Exception) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "Bir hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
