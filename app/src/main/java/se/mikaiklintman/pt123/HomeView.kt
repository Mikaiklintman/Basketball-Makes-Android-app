package se.mikaiklintman.pt123

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(percVM : PercViewmodel) {

    val statsList by percVM.statsList.collectAsState()

    var addShotsTook by remember { mutableStateOf("") }
    var addShotsMade by remember { mutableStateOf("") }

    val shotPercentage = remember(addShotsTook, addShotsMade) {
        val took = addShotsTook.toFloatOrNull() ?: 0f
        val made = addShotsMade.toFloatOrNull() ?: 0f
        if (took > 0) (made / took) * 100 else 0f
    }

    val context = LocalContext.current
    val positionsDropDown = arrayOf("Top of the key", "Right wing", "Left wing", "Right corner", "Left Corner")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(positionsDropDown[0]) }



    LaunchedEffect(true) {
        percVM.loadPerc()
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column {

            OutlinedTextField(
                value = addShotsTook,
                onValueChange = {
                    addShotsTook = it
                },
                label = { Text("Total Shots") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = addShotsMade,
                onValueChange = { addShotsMade = it },
                label = { Text("Shots Made") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = selectedText,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        positionsDropDown.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedText = item
                                    expanded = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }



            Text(
                text = "Selected Position: ${selectedText ?: ""}",
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )


            Column {

//
//            TextField(value = addShotsTook, onValueChange = { addShotsTook = it })
//            TextField(value = addShotsMade, onValueChange = { addShotsMade = it })


                Button(
                    onClick = {
                        percVM.addStuff(addShotsTook, addShotsMade, selectedText, shotPercentage )
                    },
                    modifier = Modifier
                        .fillMaxWidth() // Makes the button take up the full width
                        .align(Alignment.CenterHorizontally) // Aligns the button to the center horizontally
                ) {
                    Text("Submit")
                }


                if (statsList != null) {
                    LazyColumn {
                        items(statsList!!) {
                            Text("Shots Made: ${it.shotsTook.toString()!!}")
                            Text(it.shotsMade.toString()!!)
                            Text("hello")
                        }
                    }
                } else {
                    Text("loading...")
                }
            }
        }
    }
}


