package com.jpt.todotogether.auth.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jpt.todotogether.core.theming.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoginScreen() {
    Theme() {
        Scaffold {
            Box(
                modifier = Modifier
                    .offset(150.dp, (-150).dp)
                    .alpha(0.2f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .size(350.dp)

            )
            Box(
                modifier = Modifier
                    .offset((-150).dp, (450).dp)
                    .alpha(0.2f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .size(250.dp)



            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 80.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "",
                        modifier = Modifier.scale(1.7f),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "To-do Together",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Text(
                    text = "Stay on track with your people",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                )
                TextInputBox()
            }
        }
    }
}

@Composable
fun TextInputBox(){

}