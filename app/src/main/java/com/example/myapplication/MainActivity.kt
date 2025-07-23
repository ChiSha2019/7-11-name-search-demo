package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val query = remember { mutableStateOf("") }
    val people = remember { loadPeopleFromJson(context) }
    
    val filteredPeople = people.filter { person ->
        person.name.lowercase().startsWith(query.value.lowercase())
    }
    
    val resultText = if (query.value.isNotEmpty()) {
        filteredPeople.joinToString(", ") { "${it.name} (${it.id})" }
    } else {
        ""
    }
    
    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = query.value,
            onValueChange = { query.value = it },
            label = { Text("Search") },
            modifier = Modifier.height(200.dp)
        )
        
        Text(
            text = resultText,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

fun loadPeopleFromJson(context: Context): List<People> {
    val json = context.assets.open("people.json").bufferedReader().use { it.readText() }
    val type = object : TypeToken<List<People>>() {}.type
    return Gson().fromJson(json, type)
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MyApplicationTheme {
        SearchScreen()
    }
}