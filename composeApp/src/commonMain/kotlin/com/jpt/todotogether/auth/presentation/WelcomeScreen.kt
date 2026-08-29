package com.jpt.todotogether.auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jpt.todotogether.core.theming.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun WelcomeScreen() {
    Theme {
        Scaffold(
            bottomBar = {
            Column(Modifier.padding(15.dp)) {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = "Get started", fontSize = 16.sp)
                }
                OutlinedButton(onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(15.dp)
                ){
                    Text(
                        text = "Log in",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp
                    )
                }
            }
        }) {
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
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)

            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "",
                            modifier = Modifier.scale(1.7f),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "To-do Together",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold
                            )
                    }
                    Text(text = "Your day. Your people. Done.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(36.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DescriptionItem("Plan your day in seconds", Icons.Outlined.CheckCircle)
                    DescriptionItem("Stay accountable with friends", Icons.Outlined.People)
                    DescriptionItem("Build streaks together", Icons.Outlined.WbSunny)
                }
            }
        }
    }
}

@Composable
fun DescriptionItem(text: String, icon: ImageVector){
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(text)
    }
}