package se.mikaiklintman.pt123

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import se.mikaiklintman.pt123.ui.theme.Pt123Theme



class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pt123Theme {
                AppNavigation()
                anonymousLogin()

                }
            }
        }

    }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pt123Theme {
        AppNavigation()
    }
}