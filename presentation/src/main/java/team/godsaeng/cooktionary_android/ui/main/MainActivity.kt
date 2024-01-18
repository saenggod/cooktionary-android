package team.godsaeng.cooktionary_android.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import team.godsaeng.cooktionary_android.ui.theme.CooktionaryandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CooktionaryandroidTheme {

            }
        }
    }
}