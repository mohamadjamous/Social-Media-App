package com.example.socialmediaapp.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ui.theme.SocialMediaAppTheme
import com.example.socialmediaapp.viewmodels.AuthViewModel
import com.example.socialmediaapp.views.components.GreenButton
import kotlinx.coroutines.launch

@Composable
fun IntroPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

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


        DotsIndicator(
            totalDots = greetings.size,
            selectedIndex = pagerState.currentPage,
            selectedColor = R.color.green,
            unSelectedColor = R.color.black
        )

        Spacer(modifier = Modifier.height(34.dp))

        GreenButton(
            title = buttonName,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            enabled = true,
            isLoading = false
        ) {
            scope.launch {
                if (pagerState.currentPage < greetings.size - 1) {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                } else {
                    // Handle "Get Started" action
                    navController.navigate("login")
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
fun IntroPreview1() {
    SocialMediaAppTheme {
        val navController = rememberNavController()

        IntroPage(navController = navController, authViewModel = AuthViewModel())

    }
}