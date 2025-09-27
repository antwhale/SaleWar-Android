package io.github.antwhale.salewar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.antwhale.salewar.data.vo.StoreType
import io.github.antwhale.salewar.ui.composable.CUScreen
import io.github.antwhale.salewar.ui.composable.GS25Screen
import io.github.antwhale.salewar.ui.composable.SevenElevenScreen
import io.github.antwhale.salewar.ui.theme.SaleWarTheme
import io.github.antwhale.salewar.viewmodel.CUViewModel
import io.github.antwhale.salewar.viewmodel.GS25ViewModel
import io.github.antwhale.salewar.viewmodel.SevenElevenViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SaleWarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Scaffold provides a consistent layout structure for common app elements.
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        // NavHost is placed inside the Scaffold's content.
        NavHost(
            navController = navController,
            startDestination = StoreType.GS25.rawValue,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Define the composable for each screen.
            composable(StoreType.GS25.rawValue) { backStackEntry ->
                val gS25ViewModel = hiltViewModel<GS25ViewModel>(backStackEntry)
                GS25Screen(Modifier.fillMaxSize(), gS25ViewModel)
            }
            composable(StoreType.CU.rawValue) { backStackEntry ->
                val cuViewModel = hiltViewModel<CUViewModel>(backStackEntry)
                CUScreen(Modifier.fillMaxSize(), cuViewModel)
            }
            composable(StoreType.SEVEN_ELEVEN.rawValue) { backStackEntry ->
                val sevenElevenViewModel = hiltViewModel<SevenElevenViewModel>(backStackEntry)
                SevenElevenScreen(Modifier.fillMaxSize(), sevenElevenViewModel)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
    ){
        NavigationBar(containerColor = Color.White) {
            //GS25
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == StoreType.GS25.rawValue } == true,
                onClick = {
                    // Navigate to the selected screen.
                    navController.navigate(StoreType.GS25.rawValue) {
                        // Pop up to the start destination of the graph to avoid building up a large back stack.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when re-selecting the same item.
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item.
                        restoreState = true
                    }
                },
                icon = { Image(modifier = Modifier.size(48.dp), painter = painterResource(id = StoreType.GS25.brandLogo), contentDescription = StoreType.GS25.rawValue) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent, selectedIconColor = Color.Black, unselectedIconColor = Color.Red,selectedTextColor = Color.Black, unselectedTextColor = Color.Gray)
            )

            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == StoreType.CU.rawValue } == true,
                onClick = {
                    // Navigate to the selected screen.
                    navController.navigate(StoreType.CU.rawValue) {
                        // Pop up to the start destination of the graph to avoid building up a large back stack.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when re-selecting the same item.
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item.
                        restoreState = true
                    }
                },
                icon = { Image(modifier = Modifier.size(48.dp), painter = painterResource(id = StoreType.CU.brandLogo), contentDescription = StoreType.CU.rawValue) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent, selectedIconColor = Color.Black, unselectedIconColor = Color.Red,selectedTextColor = Color.Black, unselectedTextColor = Color.Gray)
            )

            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == StoreType.SEVEN_ELEVEN.rawValue } == true,
                onClick = {
                    // Navigate to the selected screen.
                    navController.navigate(StoreType.SEVEN_ELEVEN.rawValue) {
                        // Pop up to the start destination of the graph to avoid building up a large back stack.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when re-selecting the same item.
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item.
                        restoreState = true
                    }
                },
                icon = { Image(modifier = Modifier.size(48.dp), painter = painterResource(id = StoreType.SEVEN_ELEVEN.brandLogo), contentDescription = StoreType.SEVEN_ELEVEN.rawValue) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent, selectedIconColor = Color.Black, unselectedIconColor = Color.Red,selectedTextColor = Color.Black, unselectedTextColor = Color.Gray)
            )

        }
    }

}


@Preview(showBackground = true, apiLevel = 31)
@Composable
fun GreetingPreview() {
    SaleWarTheme {
        AppNavigation()
    }
}