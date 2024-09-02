package se.mikaiklintman.pt123

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AppNavigation(

    navController: NavHostController = rememberNavController(),
    percVM : PercViewmodel = viewModel()

) {

  //  val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                listOfNavItems.forEach{ navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any{ it.route == navItem.route } == true,
                        onClick = {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.HomeView.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.HomeView.name) {
                HomeView(percVM)
            }
            composable(route = Screens.Tab2.name) {
                Tab2(percVM, goStats = {
                    navController.navigate(Screens.TopofTheKey.name)
                })

                }

            composable(route = Screens.TopofTheKey.name) {
                TopofTheKey(percVM)
            }

            }
        }
    }
fun anonymousLogin() {
    var auth: FirebaseAuth
    auth = Firebase.auth

    auth.signInAnonymously()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
//val user = auth.currentUser
                Log.i("Logs", "Log in successful")
            } else {
                Log.i("Logs", "Log in NOT successful")
            }
        }
}

