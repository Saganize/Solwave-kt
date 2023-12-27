package com.saganize.solwave.solwave.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.saganize.solwave.R
import com.saganize.solwave.core.events.SolwaveEvents
import com.saganize.solwave.core.events.SolwaveNavEvents
import com.saganize.solwave.core.models.Response.Companion.genericErrorMsg
import com.saganize.solwave.core.models.SolwaveErrors
import com.saganize.solwave.core.models.WalletProvider
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.solwave.presentation.SolwaveViewModel
import com.saganize.solwave.solwave.presentation.components.WebViewClosingEvents
import com.saganize.solwave.solwave.presentation.components.WebViewInterface
import com.saganize.solwave.solwave.presentation.components.getWebViewClosingEvent

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(viewModel: SolwaveViewModel? = null) {
    val state = viewModel?.state?.value ?: throw IllegalStateException(genericErrorMsg)
    val context = LocalContext.current

    BackHandler(enabled = true) {
        viewModel.onNav(SolwaveNavEvents.CloseWebViewScreen(context))
    }

    var showProgressBar by remember { mutableStateOf(true) }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading_animation),
    )

    val iterations by remember {
        mutableIntStateOf(LottieConstants.IterateForever)
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        // clipSpec = LottieClipSpec.Progress(0f, maxClipSpec),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    webViewClient = WebViewClient()

                    settings.javaScriptEnabled = true
                    setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

                    settings.cacheMode = WebSettings.LOAD_NO_CACHE

                    state.url?.let {
                        loadUrl(state.url)
                    }

                    addJavascriptInterface(
                        WebViewInterface(
                            viewModel,
                            it,
                            message = state.message,
                            state.transactionParams,
                            // TODO: save email id too
                            onWalletReceived = { _, publicKey ->
                                viewModel.onEvent(
                                    SolwaveEvents.SaveWalletFromWebview(
                                        WalletProvider.Saganize,
                                        publicKey,
                                        context,
                                    ),
                                )
                            },
//                            onToast = { message, shouldEndWebView ->
//                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//                                if (shouldEndWebView) {
//                                    // viewModel.onNav(SolwaveNavEvents.CloseWebViewScreen)
//                                } else {
//                                    viewModel.onEvent(
//                                        SolwaveEvents.TransactionError(
//                                            SolwaveErrors.WebviewErrorMessage.setError(message)
//                                        )
//                                    )
//                                }
//                            } ,
                            onClosed = { event, failure ->
                                when (getWebViewClosingEvent(event)) {
                                    WebViewClosingEvents.UserCreationSuccess -> viewModel.onNav(
                                        SolwaveNavEvents.CloseWebViewScreen(
                                            context,
                                            closeActivity = true,
                                        ),
                                    )

                                    WebViewClosingEvents.LoginSuccessful -> viewModel.onNav(
                                        SolwaveNavEvents.CloseWebViewScreen(
                                            context,
                                            closeActivity = true,
                                        ),
                                    )

                                    WebViewClosingEvents.TransactionCompleted -> viewModel.onNav(
                                        SolwaveNavEvents.CloseWebViewScreen(
                                            context,
                                            closeActivity = false,
                                        ),
                                    )

                                    WebViewClosingEvents.SigningMessageSuccess -> viewModel.onNav(
                                        SolwaveNavEvents.CloseWebViewScreen(
                                            context,
                                            closeActivity = true,
                                        ),
                                    )

                                    WebViewClosingEvents.UserCreationFailure -> viewModel.onEvent(
                                        SolwaveEvents.Error(
                                            title = "User Creation Failed",
                                            SolwaveErrors.WebviewErrorMessage.setError(failure),
                                            closeWebView = true,
                                        ),
                                    )

                                    WebViewClosingEvents.LoginFailure -> viewModel.onEvent(
                                        SolwaveEvents.Error(
                                            title = "Login Failed",
                                            SolwaveErrors.WebviewErrorMessage.setError(failure),
                                            closeWebView = true,
                                        ),
                                    )

                                    WebViewClosingEvents.ServerError -> viewModel.onEvent(
                                        SolwaveEvents.Error(
                                            error = SolwaveErrors.WebviewErrorMessage.setError(
                                                failure,
                                            ),
                                            closeWebView = true,
                                        ),
                                    )

                                    WebViewClosingEvents.TransactionFailed -> viewModel.onEvent(
                                        SolwaveEvents.Error(
                                            title = "Transaction Failed",
                                            SolwaveErrors.WebviewErrorMessage.setError(failure),
                                            closeWebView = true,
                                        ),
                                    )
                                }
                            },
                        ),
                        "Solwave",
                    )
                }
            },
        ) { webView ->

            webView.webViewClient = object : WebViewClient() {

                // TODO: webview listeners
                override fun onPageFinished(view: WebView?, url: String?) {
                    // Do something when the page finishes loading
                    Log.d("WebView", "your current url when webpage loading.. finish $url")
                    super.onPageFinished(view, url)
                    showProgressBar = false

                    // Check if the URL is not null and contains a query string
//                    if (url != null && url.contains("?")) {
//                        val uri = Uri.parse(url)
//
//                        val queryParameterNames = uri.queryParameterNames
//
//                        // Iterate through the parameters and their values
//                        for (paramName in queryParameterNames) {
//                            val paramValue = uri.getQueryParameter(paramName)
//                            Log.d("WebView", "Parameter: $paramName = $paramValue")
//                        }
//                    }
//
//                    when (state.screen) {
//                        Screens.SignupScreen -> viewModel.onAuthEvent(
//                            SolwaveAuthNavEvents.OnCreateUserDone(null)
//                        )
//
//                        Screens.LoginScreen -> viewModel.onAuthEvent(
//                            SolwaveAuthNavEvents.OnLoginDone(null)
//                        )
//
//                        is Screens.PayScreen -> viewModel.onAuthEvent(
//                            SolwaveAuthNavEvents.OnTransactionDone(null)
//                        )
//
//                        else -> {}
//                    }
//                }
//
//                override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
//                    super.onPageStarted(view, url, favicon)
//                    Log.d("WebView", "your current url when webpage loading..$url")
//                }
//
//                override fun onLoadResource(view: WebView?, url: String?) {
//                    // TODO Auto-generated method stub
//                    super.onLoadResource(view, url)
//                }
//
//                override fun shouldOverrideUrlLoading(
//                    view: WebView?,
//                    request: WebResourceRequest?
//                ): Boolean {
//                    Log.d(
//                        "WebView",
//                        "your current url when webpage loading.. finish${request?.url}"
//                    )
//
//                    // Get the URL of the requested page
//                    val url = request?.url?.toString()
//
//                    // Check if the URL is not null and contains a query string
//                    if (url != null && url.contains("?")) {
//                        val uri = Uri.parse(url)
//
//                        val queryParameterNames = uri.queryParameterNames
//
//                        // Iterate through the parameters and their values
//                        for (paramName in queryParameterNames) {
//                            val paramValue = uri.getQueryParameter(paramName)
//                            Log.d("WebView", "Parameter: $paramName = $paramValue")
//                        }
//                    }
//
//                    return super.shouldOverrideUrlLoading(view, request)
//                }
//
//                override fun onReceivedError(
//                    view: WebView?,
//                    request: WebResourceRequest?,
//                    error: WebResourceError?
//                ) {
//                    super.onReceivedError(view, request, error)
//
//                    Log.d("WebView", "onReceivedError: $error")
//                }
                }
            }
        }

        if (showProgressBar) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.FillBounds,
                )
            }
        }

        // top icon to close
        Row(
            Modifier
                .fillMaxSize()
                .padding(SolwaveTheme.dimensions.largePadding),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(
                onClick = {
                    viewModel.onNav(
                        SolwaveNavEvents.CloseWebViewScreen(
                            context,
                            closeActivity = false,
                        ),
                    )
                },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_wv_back),
                    contentDescription = null,
                )
            }
        }
    }
}