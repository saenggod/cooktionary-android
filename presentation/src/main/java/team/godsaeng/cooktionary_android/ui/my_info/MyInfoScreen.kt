package team.godsaeng.cooktionary_android.ui.my_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.CollectUiEffectWithLifecycle
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.container.ROUTE_SECESSION
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoContract.UiEffect.GoToSecession
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoContract.UiEvent
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoContract.UiEvent.OnClickSecession
import team.godsaeng.cooktionary_android.ui.theme.DividerColor
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey5
import team.godsaeng.cooktionary_android.ui.theme.Typography
import team.godsaeng.cooktionary_android.util.UserInfo

private val LocalUiEvent = compositionLocalOf { { _: UiEvent -> } }

@Composable
fun MyInfoScreen(
    navController: NavController,
    viewModel: MyInfoViewModel = hiltViewModel()
) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)
    val onEvent = remember { { event: UiEvent -> uiEvent(event) } }

    CollectUiEffectWithLifecycle(uiEffect) { collected ->
        when (collected) {
            is GoToSecession -> navController.navigate(ROUTE_SECESSION)
        }
    }

    CompositionLocalProvider(LocalUiEvent provides onEvent) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            TopBar(
                onClickBackButton = {},
                middleContents = {
                    StyledText(
                        stringId = R.string.my_info,
                        style = Typography.bodyMedium,
                        fontSize = 16
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoItem(
                stringId = R.string.nickname,
                value = UserInfo.name
            )

            Spacer(modifier = Modifier.height(36.dp))

            InfoItem(
                stringId = R.string.email_address,
                value = UserInfo.email
            )

            Spacer(modifier = Modifier.height(36.dp))

            Divider(
                color = DividerColor,
                thickness = 2.dp
            )

            if (UserInfo.isLoggedIn) {
                LogoutSection()

                Divider(
                    color = DividerColor,
                    thickness = 2.dp
                )

                SecessionSection()
            }
        }
    }
}

@Composable
private fun InfoItem(
    stringId: Int,
    value: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = SpaceBetween
    ) {
        StyledText(
            stringId = stringId,
            style = Typography.bodyLarge,
            fontSize = 14,
            color = TextColorGrey5
        )

        Spacer(modifier = Modifier.height(12.dp))

        StyledText(
            text = value,
            style = Typography.bodySmall,
            fontSize = 18
        )
    }
}

@Composable
private fun LogoutSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickableWithoutRipple { }
    ) {
        StyledText(
            modifier = Modifier
                .align(CenterStart)
                .padding(horizontal = 16.dp),
            stringId = R.string.logout,
            style = Typography.bodyLarge,
            fontSize = 14,
            color = TextColorGrey5
        )
    }
}

@Composable
private fun ColumnScope.SecessionSection() {
    val localUiEvent = LocalUiEvent.current

    Box(
        modifier = Modifier
            .align(End)
            .width(74.dp)
            .height(54.dp)
            .clickableWithoutRipple { localUiEvent(OnClickSecession) },
    ) {
        StyledText(
            modifier = Modifier.align(Center),
            style = Typography.bodySmall,
            stringId = R.string.secession,
            fontSize = 12,
            color = TextColorGrey5
        )
    }
}