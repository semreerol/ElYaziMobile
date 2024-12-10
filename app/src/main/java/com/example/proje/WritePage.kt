package com.example.proje

import android.Manifest
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun YaziScreen(navController: NavController) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
            if (!isGranted) {
                Toast.makeText(context, "Kamera izni gerekli.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                Toast.makeText(context, "Fotoğraf yüklendi: $uri", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Fotoğraf yükleme iptal edildi.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Arka plan resmi
        Image(
            painter = painterResource(id = R.drawable.yazi), // Arka plan resminiz
            contentDescription = "Arka Plan",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // İçerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Fotoğraf Çekin veya Yükleyin", color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            // Fotoğraf ikonu ve buton yan yana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Kamera ikonu
                Image(
                    painter = painterResource(id = R.drawable.ic_camera), // Kamera ikonunuzun kaynağı
                    contentDescription = "Kamera",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape) // Kenarları yuvarlak yapar
                        .clickable {
                            if (hasCameraPermission) {
                                val intent = Intent(context, CameraActivity::class.java)
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "Lütfen kamera izni verin.", Toast.LENGTH_SHORT).show()
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                )

                Spacer(modifier = Modifier.width(16.dp)) // İkon ile buton arasında boşluk

                // Fotoğraf Yükle butonu
                Button(
                    onClick = {
                        pickImageLauncher.launch("image/*") // Sadece resim dosyalarını seçmek için
                    },
                    modifier = Modifier.weight(1f) // Buton genişliği için ağırlık verildi
                ) {
                    Text(text = "Belgelerden Yükle")
                }
            }
        }

        // Sağ alt kısımda buton
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(onClick = {
                navController.navigate("converter") // "converter" route'u tanımlanmış olmalı
            }) {
                Text(text = "Converter'a Git")
            }
        }
    }
}