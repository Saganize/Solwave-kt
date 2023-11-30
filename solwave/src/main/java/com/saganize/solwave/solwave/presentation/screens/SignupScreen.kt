package com.saganize.solwave.solwave.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saganize.solwave.R
import com.saganize.solwave.core.events.SolwaveAuthNavEvents
import com.saganize.solwave.core.events.SolwaveNavEvents
import com.saganize.solwave.core.presentation.components.AppNameBar
import com.saganize.solwave.core.presentation.components.ButtonPrimary
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.core.presentation.theme.bold
import com.saganize.solwave.core.presentation.theme.interFamily
import com.saganize.solwave.core.presentation.theme.rubikFamily
import com.saganize.solwave.core.util.extensions.showToast
import com.saganize.solwave.solwave.presentation.SolwaveViewModel
import com.saganize.solwave.solwave.presentation.components.firebaseSignIn

@Composable
fun SignupScreen(viewModel: SolwaveViewModel? = null) {
    // back to no account screen when back btn pressed
    BackHandler(enabled = true) {
        viewModel?.onNav(SolwaveNavEvents.GoToNoAccountScreen)
    }

    val state = viewModel?.state?.value
    val context = LocalContext.current

    var btnLoading by remember {
        mutableStateOf(false)
    }

    val firebaseSignIn =
        firebaseSignIn(
            onDone = {
                viewModel?.onAuthEvent(
                    SolwaveAuthNavEvents.InitCreateUser(context, it),
                )
                btnLoading = false
            },
            onErr = {
                // Factorize this and create our own error class to handle errors
                btnLoading = false
                context.showToast(it)
            },
        )

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .paint(
                painterResource(id = R.drawable.colored_background),
                contentScale = ContentScale.FillBounds,
            )
            .padding(SolwaveTheme.dimensions.largePadding),
        horizontalAlignment = Alignment.Start,
    ) {
        AppNameBar()

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Get Started",
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 30.sp,
                fontFamily = rubikFamily,
                fontWeight = FontWeight(600),
                color = Color(0xFFF9F9F9),
            ),
        )
        Spacer(modifier = Modifier.height(SolwaveTheme.dimensions.basePadding))
        Text(
            text = "Just few easy steps to get you started",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 30.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight(300),
                color = Color(0xFFF9F9F9),
            ),
        )
        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = "Sign in with Google",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight(500),
                color = Color(0xFFF9F9F9),
            ),
        )
        Spacer(modifier = Modifier.height(SolwaveTheme.dimensions.mediumPadding))

        ButtonPrimary(
            onClick = {
                btnLoading = true
                firebaseSignIn()
            },
            enabled = !btnLoading && state?.url.isNullOrEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        ) {
            if (btnLoading && state?.url.isNullOrEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(SolwaveTheme.dimensions.largePadding)
                        .padding(4.dp),
                    color = Color.Black,
                )
            } else {
                Box {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Image(
                            painter = painterResource(id = R.drawable.google_ic),
                            contentDescription = null,
                        )
                    }

                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Create new account",
                            color = Color.Black,
                            style = MaterialTheme.typography.button.bold,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(240.dp))
    }
}

@Preview
@Composable
fun SignupScreenPreview() {
    SignupScreen()
}
