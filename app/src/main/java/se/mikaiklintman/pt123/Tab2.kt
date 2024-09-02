package se.mikaiklintman.pt123

import android.content.SyncStats
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Tab2(percVM : PercViewmodel, goStats : () -> Unit) {

    Column {
        Text(text = "Heyyyyy tab2")

        Button(onClick = {
            goStats()
        }) {
            Text("Stats")
        }
    }

}