package com.example.proje

import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.proje.ui.theme.ProjeTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "giris") {
                    composable("giris") { GirisScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("register") { RegisterScreen(navController) }
                    composable("write") { WriteScreen(navController) }
                    composable("translate") { TranslateScrenn() }
                }
            }
        }
    }


}

@Composable
fun GirisScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Arka plan resmi
        Image(
            painter = painterResource(id = R.drawable.ic_wallpaper),
            contentDescription = "Arka Plan Resmi",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // İçerikler
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "El Yazısı Tanıma Sistemi", fontSize = 24.sp, color = Color.White)
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kullanıcı Girişi")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kayıt Ol")
            }
        }
    }
}

fun Box(modifier: Unit) {

}


