package team.godsaeng.cooktionary_android.ui.on_boarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.getContext
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithGoogle
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithKakaoAccount
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEffect.LoginWithKakaoTalk
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickGoogleLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnClickKakaoLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnFailureLogin
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingContract.UiEvent.OnSuccessLogin
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey4
import team.godsaeng.cooktionary_android.ui.theme.Typography

val KakaoYellow = Color(0xFFFEE500)
val GoogleGrey = Color(0xFFF2F2F2)

@Composable
fun OnBoardingScreen(viewModel: OnBoardingViewModel = hiltViewModel()) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)
    val context = getContext()
    val socialLoginManager = remember {
        SocialLoginManager(
            context = context,
            onSuccess = { platform, token ->
                uiEvent(OnSuccessLogin(platform, token))
            },
            onFailure = {
                uiEvent(OnFailureLogin)
            }
        )
    }.also {
        it.InitGoogleLoginLauncher()
    }

    LaunchedEffect(uiEffect) {
        uiEffect.collectLatest { uiEffect ->
            when (uiEffect) {
                is LoginWithKakaoTalk -> socialLoginManager.loginWithKakaoTalk()

                is LoginWithKakaoAccount -> socialLoginManager.loginWithKakaoAccount()

                is LoginWithGoogle -> socialLoginManager.launchGoogleLoginLauncher()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        SkipSection()

        LogoSection()

        LoginSection(uiEvent)
    }
}

@Composable
private fun BoxScope.SkipSection() {
    StyledText(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp),
        stringId = R.string.skip,
        style = Typography.bodyMedium,
        fontSize = 14,
        color = TextColorGrey4
    )
}

@Composable
private fun BoxScope.LogoSection() {
    Column(
        modifier = Modifier
            .align(TopCenter)
            .padding(top = 200.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(24.dp))

        StyledText(
            stringId = R.string.app_desc,
            style = Typography.titleLarge,
            fontSize = 15
        )
    }
}

@Composable
private fun BoxScope.LoginSection(
    uiEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .align(BottomCenter)
            .padding(horizontal = 16.dp)
            .padding(bottom = 76.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SocialLoginButton(
            stringId = R.string.login_with_kakao,
            color = KakaoYellow,
            iconId = R.drawable.ic_kakao,
            onClick = { uiEvent(OnClickKakaoLogin) }
        )

        SocialLoginButton(
            stringId = R.string.login_with_google,
            color = GoogleGrey,
            iconId = R.drawable.ic_google,
            onClick = { uiEvent(OnClickGoogleLogin) }
        )
    }
}

@Composable
private fun SocialLoginButton(
    stringId: Int,
    color: Color,
    iconId: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(36.dp))
            .fillMaxWidth()
            .height(48.dp)
            .background(color = color)
            .clickable { onClick() }
    ) {
        Image(
            modifier = Modifier
                .align(CenterStart)
                .padding(start = 20.dp),
            painter = painterResource(id = iconId),
            contentDescription = null
        )

        StyledText(
            modifier = Modifier.align(Center),
            stringId = stringId,
            style = Typography.bodyMedium,
            fontSize = 13
        )
    }
}