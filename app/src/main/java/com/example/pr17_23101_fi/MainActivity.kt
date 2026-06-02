package com.example.pr17_23101_fi

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pr17_23101_fi.ui.theme.Pr1723101fiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pr1723101fiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SQLiteScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SQLiteScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Button(
            onClick = {
                if (name.isBlank() || email.isBlank()) {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val cv = ContentValues().apply {
                    put("name", name)
                    put("email", email)
                }

                val db = dbHelper.writableDatabase
                val rowID = db.insert("mytable", null, cv)
                db.close()

                Log.d("myLogs", "row inserted, ID = $rowID")
                Toast.makeText(context, "Запись добавлена (ID=$rowID)", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Добавить запись", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Button(
            onClick = {
                val db = dbHelper.readableDatabase
                val cursor = db.query("mytable", null, null, null, null, null, null)

                Log.d("myLogs", "--- Rows in mytable: ---")
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                        val dbName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                        val dbEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                        Log.d("myLogs", "ID = $id, name = $dbName, email = $dbEmail")
                    } while (cursor.moveToNext())
                    Toast.makeText(context, "Данные выведены в Logcat", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("myLogs", "0 rows")
                    Toast.makeText(context, "Таблица пуста", Toast.LENGTH_SHORT).show()
                }
                cursor.close()
                db.close()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Показать все записи", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        Button(
            onClick = {
                val db = dbHelper.writableDatabase
                val count = db.delete("mytable", null, null)
                db.close()

                Log.d("myLogs", "deleted rows count = $count")
                Toast.makeText(context, "Удалено $count записей", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Очистить таблицу", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}