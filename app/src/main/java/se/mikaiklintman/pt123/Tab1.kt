package se.mikaiklintman.pt123

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Tab1() {

//    FirebaseStuff()



    var selectedPosition by remember { mutableStateOf<String?>(null) }
    var shotsTook by remember { mutableStateOf("") }
    var shotsMade by remember { mutableStateOf("") }
    val shootingStatsByPosition = remember { mutableStateListOf<Pair<String, MutableList<Pair<Float, String>>>>() }



    // Function to update statistics
    fun updateStats() {
        val took = shotsTook.toFloatOrNull() ?: 0f
        val made = shotsMade.toFloatOrNull() ?: 0f
        if (took != 0f && selectedPosition != null) {
            val percentage = ((made / took) * 100).toInt().toFloat()
            val statsIndex = shootingStatsByPosition.indexOfFirst { it.first == selectedPosition }
            if (statsIndex != -1) {
                // If the position exists, update its statistics
                val stats = shootingStatsByPosition[statsIndex].second
                stats.add(percentage to "${took.toInt()} / ${made.toInt()}")
            } else {
                // If the position doesn't exist, add it with new statistics
                shootingStatsByPosition.add(selectedPosition!! to mutableListOf(percentage to "${took.toInt()} / ${made.toInt()}"))
            }
            shotsTook = ""
            shotsMade = ""
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selected Position: ${selectedPosition ?: ""}",
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Top of The Key", "Right Corner").forEach { position ->
                    Button(
                        onClick = { selectedPosition = position },
                        modifier = Modifier.wrapContentWidth().padding(4.dp)
                    ) {
                        Text(position)
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Right Wing", "Left Wing", "Left Corner").forEach { position ->
                    Button(
                        onClick = { selectedPosition = position },
                        modifier = Modifier.wrapContentWidth().padding(4.dp)
                    ) {
                        Text(position)
                    }
                }
            }
        }

        OutlinedTextField(
            value = shotsTook,
            onValueChange = { value ->
                shotsTook = value.takeIf { it.all { char -> char.isDigit() } } ?: ""
            },
            label = { Text("Total Shots") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = shotsMade,
            onValueChange = { value ->
                shotsMade = value.takeIf { it.all { char -> char.isDigit() } } ?: ""
            },
            label = { Text("Shots Made") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                updateStats()

                val database = Firebase.database
                val myRef = database.getReference("message")
                val exampleFirebase = ShotPercentageFb(selectedPosition, "90", "60/90")
                val positionRef = database.getReference("heyyyy")
                positionRef.setValue(exampleFirebase)

            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Submit")
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(shootingStatsByPosition) { (position, stats) ->
                StatsSection(position, stats)
            }
        }

        val totalPercentage = shootingStatsByPosition
            .flatMap { it.second.map { pair -> pair.first } } // Extract all percentage values
            .average() // Calculate average of all percentages

        Text(
            text = "Average Percentage: ${totalPercentage.toInt()}%",
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium
        )
    }
}



@Composable
fun StatsSection(
    position: String,
    stats: List<Pair<Float, String>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = position,
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        stats.reversed().forEach { (percentage, tookMade) ->
            val currentTime = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date()
            ) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = currentTime,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = tookMade,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${percentage.toInt()}%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}






fun FirebaseStuff() {

        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Just checking")

}

data class ShotPercentageFb(
    val position: String? = null,
    val percentage: String? = null,
 //   val date: Date? = null,
    val score: String? = null
    ) {

}

