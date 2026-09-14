package com.jpt.todotogether.auth.presentation.loginPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jpt.todotogether.auth.presentation.welcomePage.WelcomePageAction
import com.jpt.todotogether.core.theming.Theme
import com.jpt.todotogether.home.presentation.homePage.HomePageAction
import com.mmk.kmpauth.google.rememberGoogleSignInState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import todotogether.composeapp.generated.resources.Res
import todotogether.composeapp.generated.resources.ic_apple_logo
import todotogether.composeapp.generated.resources.ic_google_logo

@Preview
@Composable
private fun LoginPageScreenPreview() {
    LoginPageScreen(
        state = LoginPageState(),
        onAction = {},
        navigateToHome = {},
    )
}

@Composable
fun LoginPageScreenRoot(
    viewModel: LoginPageViewModel = koinViewModel(),
    navigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginPageScreen(
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
        },
        navigateToHome,
    )
}


@Composable
fun LoginPageScreen(
    state: LoginPageState,
    onAction: (LoginPageAction) -> Unit,
    navigateToHome: () -> Unit
) {
    val googleSignIn = rememberGoogleSignInState(
        isAutoSelectEnabled = false,
        onResult = { result ->
            result
                .onSuccess { user -> onAction(LoginPageAction.OnGoogleSignInSucceeded(user.idToken)) }
                .onFailure { error -> onAction(LoginPageAction.OnGoogleSignInFailed(error.message ?: "Sign-in failed")) }
        })
    Theme() {
        Scaffold() {
            Box(
                modifier = Modifier
                    .offset(150.dp, (-150).dp)
                    .alpha(0.1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .size(350.dp)

            )
            Box(
                modifier = Modifier
                    .offset((-150).dp, (560).dp)
                    .alpha(0.1f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .size(250.dp)



            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 80.dp, horizontal = 16.dp)
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
                Spacer(Modifier.height(40.dp))
                var text1 by remember { mutableStateOf(value = "Email") }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = text1,
                        onValueChange = {},
                        shape = RoundedCornerShape(15.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                    var text2 by remember { mutableStateOf(value = "Password") }
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = text2,
                        onValueChange = {},
                        shape = RoundedCornerShape(15.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                    Button(
                        onClick = {
                            onAction(LoginPageAction.OnLoginPressed)
                            navigateToHome() // TODO: Change later
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(15.dp),
                    )
                    {
                        Text(
                            text = "Log in",
                            fontSize = 16.sp
                        )
                    }
                    Row {
                        Text(
                            text = "Don't have an account? Sign up",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                        )
                    }
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                        )
                    {
                        Divider(
                            modifier = Modifier
                                .weight(1f)
                            ,
                            color = Color.LightGray
                        )
                        Text(
                            text = "or",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                            )
                        Divider(
                            modifier = Modifier
                                .weight(1f),
                            color = Color.LightGray
                        )
                    }
                    Button(
                        onClick = {
                            googleSignIn.launch()
                            navigateToHome() // TODO: Change later
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonColors(
                            contentColor = Color.Black,
                            containerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContentColor = Color.Black
                            )
                    )
                    {
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                ,
                        )
                        {
                            Image(
                                painter = painterResource(Res.drawable.ic_google_logo),
                                contentDescription = "Google icon",
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Text(
                                text = "Sign in with Google",
                                fontSize = 16.sp
                            )
                        }
                    }
                    Button(
                        onClick = {
                            onAction(LoginPageAction.OnApplePressed)
                            navigateToHome() // TODO: Change later
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonColors(
                            contentColor = MaterialTheme.colorScheme.surface,
                            containerColor = Color.Black,
                            disabledContainerColor = Color.Black,
                            disabledContentColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    {
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                            ,
                        )
                        {
                            Icon(
                                painter = painterResource(Res.drawable.ic_apple_logo),
                                contentDescription = "Google icon",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Text(
                                text = "Sign in with Apple",
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
