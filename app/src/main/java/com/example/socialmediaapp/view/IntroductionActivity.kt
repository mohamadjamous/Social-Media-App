package com.example.socialmediaapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ui.theme.SocialMediaAppTheme
import com.example.socialmediaapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class IntroductionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            val navController = rememberNavController()

            SocialMediaAppTheme {
                NavHost(navController = navController, startDestination = "auth") {

                    navigation(startDestination = "intro", route = "auth"){

                        composable(route = "intro") {
                            IntroScreen {
                                navController.navigate("login")
                            }
                        }
                        composable(route = "login") {
                            val viewModel = it.sharedViewModel<LoginViewModel>(navController)

                            LoginScreen(
                                onBackClick = {navController.popBackStack() },
                                onSignupClick = {navController.navigate("signup")},
                                onForgotPasswordClick = { navController.navigate("forgot_password")}) {

                            }
                        }


                        composable("forgot_password"){
                            val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                            ForgotPasswordScreen()
                        }

                        composable("signup"){
                            val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                            SignupScreen()
                        }

                    }
                }
            }
        }
    }
}



@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroScreen(onClick: () -> Unit) {
    val greetings = listOf(
        Triple(
            R.drawable.bg_first,
            "Let's connect with each other",
            "Connect with your friends and family all in one place"
        ),
        Triple(
            R.drawable.bg_first,
            "Share Moments, Stay Close",
            "Capture and share your favorite memories, and stay connected with the people who matter most."
        )
    )

    var buttonName by rememberSaveable { mutableStateOf("Next") }
    var visibleSkip by remember { mutableStateOf(true) }

    val pagerState = rememberPagerState { greetings.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page < greetings.size - 1) {
                visibleSkip = true
                buttonName = "Next"
            } else {
                visibleSkip = false
                buttonName = "Get Started"
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 16.dp)
    ) {

        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            key = { it }, // Use the index as the key to ensure uniqueness
            pageSize = PageSize.Fill
        ) {
            ItemGreeting(greetings[it])
        }

        Spacer(modifier = Modifier.height(32.dp))

        DotsIndicator(
            totalDots = greetings.size,
            selectedIndex = pagerState.currentPage,
            selectedColor = R.color.green,
            unSelectedColor = R.color.black
        )

        Spacer(modifier = Modifier.height(34.dp))

        GreenButton(
            title = buttonName,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
        ) {
            scope.launch {
                if (pagerState.currentPage < greetings.size - 1) {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                } else {
                    // Handle "Get Started" action
                    onClick()
                }
            }
        }

    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Int,
    unSelectedColor: Int,
) {
    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(12.dp) // Set size for the dots
                    .clip(CircleShape)
                    .background(
                        if (index == selectedIndex) colorResource(id = selectedColor)
                        else colorResource(id = unSelectedColor)
                    )
            )
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.width(8.dp)) // Add spacing between dots
            }
        }
    }
}


@Composable
fun GreenButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12),
        colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.green)),
        onClick = {
            onClick()
        }) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = colorResource(id = R.color.white)
        )
    }
}


@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier
            .width(70.dp)
            .height(50.dp),
        shape = RoundedCornerShape(12),
        colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.green)),
        onClick = {
            onClick()
        }) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back icon",
            modifier = Modifier.size(24.dp),  // You can adjust the size as needed
            tint = Color.Unspecified          // Use this to retain the original color of the icon
        )
    }
}


@Composable
fun ItemGreeting(item: Triple<Int, String, String>) {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = item.first),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(550.dp)
                .height(280.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = item.second,
            fontSize = 35.sp,
            lineHeight = 50.sp,
            color = colorResource(id = R.color.black)

        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = item.third,
            fontSize = 19.sp,
            color = colorResource(id = R.color.grey)
        )


    }

}




@Preview(showSystemUi = true)
@Composable
fun IntroPreview() {
    SocialMediaAppTheme {
        val navController = rememberNavController()
        IntroScreen {
            navController.navigate("login_activity")
        }
    }
}