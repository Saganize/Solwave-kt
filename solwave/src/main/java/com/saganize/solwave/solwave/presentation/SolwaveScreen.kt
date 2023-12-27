package com.saganize.solwave.solwave.presentation

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.saganize.solwave.core.models.Screens
import com.saganize.solwave.core.presentation.theme.BackBlur
import com.saganize.solwave.core.presentation.theme.BackgroundBlack
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.solwave.presentation.components.LoadingItem
import com.saganize.solwave.solwave.presentation.screens.AddFundsScreen
import com.saganize.solwave.solwave.presentation.screens.ErrorScreen
import com.saganize.solwave.solwave.presentation.screens.LoadingScreen
import com.saganize.solwave.solwave.presentation.screens.LoginScreen
import com.saganize.solwave.solwave.presentation.screens.NoAccountScreen
import com.saganize.solwave.solwave.presentation.screens.PayScreen
import com.saganize.solwave.solwave.presentation.screens.SignMessageScreen
import com.saganize.solwave.solwave.presentation.screens.SignupScreen
import com.saganize.solwave.solwave.presentation.screens.TransactionDoneScreen
import com.saganize.solwave.solwave.presentation.screens.TransactionFailedScreen
import com.saganize.solwave.solwave.presentation.screens.WalletScreen
import com.saganize.solwave.solwave.presentation.screens.WebViewScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SolwaveScreen(viewModel: SolwaveViewModel) {
    val state = viewModel.state.value

    val context = LocalContext.current as Activity

    val bottomSheetState = rememberBottomSheetState(BottomSheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    LaunchedEffect(bottomSheetState.isCollapsed) {
        if (bottomSheetState.isCollapsed) {
            context.finish()
        }
    }

    BottomSheetScaffold(
        backgroundColor = BackBlur,
        sheetGesturesEnabled = state.screen is Screens.ErrorScreen ||
            state.screen == Screens.TransactionDoneScreen ||
            state.screen == Screens.TransactionFailedScreen,
        drawerShape = RoundedCornerShape(topEnd = 18.dp, topStart = 18.dp),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        contentColor = Color.Transparent,
        drawerBackgroundColor = Color.Transparent,
        drawerContentColor = Color.Transparent,
        drawerScrimColor = Color.Transparent,
        sheetBackgroundColor = Color.Transparent,
        sheetContentColor = Color.Transparent,
        sheetContent = {
            SolwaveTheme {
                AnimatedContent(
                    targetState = state.screen,
                    label = "",
                ) { state ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                BackgroundBlack,
                                RoundedCornerShape(topEnd = 18.dp, topStart = 18.dp),
                            ),
                    ) {
                        when (state) {
                            Screens.NoAccountScreen -> NoAccountScreen(viewModel = viewModel)
                            Screens.SignupScreen -> SignupScreen(viewModel = viewModel)
                            Screens.LoginScreen -> LoginScreen(viewModel = viewModel)
                            Screens.WalletScreen -> WalletScreen(viewModel = viewModel)
                            Screens.LoadingScreen -> LoadingScreen()
                            Screens.NoFundsScreen -> AddFundsScreen(viewModel = viewModel)
                            Screens.TransactionDoneScreen -> TransactionDoneScreen(
                                viewModel = viewModel,
                            )

                            is Screens.ErrorScreen -> ErrorScreen(
                                viewModel = viewModel,
                                title = state.title,
                            )

                            Screens.TransactionFailedScreen -> TransactionFailedScreen(
                                viewModel = viewModel,
                            )

                            is Screens.PayScreen -> PayScreen(
                                viewModel = viewModel,
                                transactionParams = state.transactionParams,
                            )

                            is Screens.SignMessageScreen -> {
                                SignMessageScreen(
                                    viewModel = viewModel,
                                    message = state.message,
                                )
                            }

                            else -> {
                                LoadingItem()
                            }
                        }
                    }
                }
            }
        },
    ) {}

    AnimatedVisibility(visible = !state.url.isNullOrEmpty(), enter = fadeIn(), exit = fadeOut()) {
        WebViewScreen(viewModel = viewModel)
    }
}
