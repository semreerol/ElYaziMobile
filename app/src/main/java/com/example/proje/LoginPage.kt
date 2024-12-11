import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proje.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Arka plan resmi
        Image(
            painter = painterResource(id = R.drawable.blue),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Kullanıcı Girişi", fontSize = 24.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Kullanıcı Adı") },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifre") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // CoroutineScope ile giriş işlemini başlat
                    CoroutineScope(Dispatchers.IO).launch {
                        loginUser(username, password, navController, context)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Giriş Yap")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate("register")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kayıt Ol")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("yazi")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Yazı Ekranına Git")
            }
        }
    }
}

fun loginUser(username: String, password: String, navController: NavController, context: Context) {

    val client = OkHttpClient()
    val requestBody = """{"username":"$username","password":"$password"}""".toRequestBody()
    val request = Request.Builder()
        .url("http://10.0.2.2:5138/api/User/login") // Yerel makine IP adresinizi kullanın
        .post(requestBody)
        .addHeader("Content-Type", "application/json")
        .build()

    try {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            // UI değişikliklerini ana thread'de yapmak için Dispatchers.Main kullanılıyor
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                navController.navigate("write") // Başarılı girişte yönlendirme
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Kullanıcı adı veya şifre hatalı!", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "Bir hata oluştu: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
