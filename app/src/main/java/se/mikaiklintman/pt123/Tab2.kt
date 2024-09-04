package se.mikaiklintman.pt123

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Tab2(percVM: PercViewmodel, goStats: (FirebaseClass) -> Unit) {
    val statsList by percVM.statsList.collectAsState()
    var deleteMode by remember { mutableStateOf(false) } // Toggle delete mode

    // Define the desired order of the positions
    val positionsOrder =
        listOf("Top of the key", "Right wing", "Left wing", "Right corner", "Left Corner")

    Column {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "My Stats",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            // Top bar with trash icon
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { deleteMode = !deleteMode }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Mode",
                        tint = if (deleteMode) Color.Red else Color.Gray
                    )
                }
            }

            if (statsList != null) {
                LazyColumn {
                    positionsOrder.forEach { position ->
                        // Check if there are stats for this position
                        val stats = statsList!!.filter { it.position == position }

                        if (stats.isNotEmpty()) {
                            // Position header
                            item {
                                Text(
                                    text = position,
                                    modifier = Modifier.padding(
                                        vertical = 8.dp,
                                        horizontal = 20.dp
                                    ),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Show only the 3 most recent stats under each position
                            val recentStats = stats.take(3)
                            items(recentStats) { stat ->
                                val currentTime = remember {
                                    SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    ).format(Date())
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = currentTime,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "${stat.shotsMade}/${stat.shotsTook}",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = String.format("%.0f%%", stat.percentage),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )

                                    // Show delete button if in delete mode
                                    if (deleteMode) {
                                        IconButton(
                                            onClick = {
                                                percVM.deleteitem(stat)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Stat",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

