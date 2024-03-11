package team.godsaeng.cooktionary_android.ui.my_page

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.CollectUiEffectWithLifecycle
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.alpha
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEffect.GoToPrivacyTerms
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEffect.GoToSavedRecipeList
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEffect.GoToServiceTerms
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEvent
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEvent.OnClickPrivacyTerms
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEvent.OnClickSavedRecipeList
import team.godsaeng.cooktionary_android.ui.my_page.MyPageContract.UiEvent.OnClickServiceTerms
import team.godsaeng.cooktionary_android.ui.theme.ArrowBackgroundColor
import team.godsaeng.cooktionary_android.ui.theme.ArrowTint
import team.godsaeng.cooktionary_android.ui.theme.SectionBackgroundColor
import team.godsaeng.cooktionary_android.ui.theme.SubColor
import team.godsaeng.cooktionary_android.ui.theme.Typography
import team.godsaeng.cooktionary_android.util.UserInfo

private val LocalUiEvent = compositionLocalOf { { _: UiEvent -> } }

@Composable
fun MyPageScreen(
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)
    val onEvent = remember { { event: UiEvent -> uiEvent(event) } }
    val context = LocalContext.current

    CollectUiEffectWithLifecycle(uiEffect) { collected ->
        when (collected) {
            is GoToSavedRecipeList -> Unit

            is GoToServiceTerms -> context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/3fb3e86aafd047d38d8fd947c11824b9")))

            is GoToPrivacyTerms ->context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notion.so/0f4bba8c41334ff18a094ebdad6524d2")))
        }
    }

    CompositionLocalProvider(LocalUiEvent provides onEvent) {
        val localUiEvent = LocalUiEvent.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
                .padding(horizontal = 16.dp)
        ) {
            TopBar(
                onClickBackButton = {},
                middleContents = {
                    StyledText(
                        stringId = R.string.my_page,
                        style = Typography.bodyMedium,
                        fontSize = 16
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            UserSection()

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = spacedBy(8.dp)) {
                MyPageButton(
                    stringId = R.string.saved_recipe_list,
                    onClick = { localUiEvent(OnClickSavedRecipeList) }
                )

                MyPageButton(
                    stringId = R.string.service_terms_of_use,
                    onClick = { localUiEvent(OnClickServiceTerms) }
                )

                MyPageButton(
                    stringId = R.string.privacy_policy,
                    onClick = { localUiEvent(OnClickPrivacyTerms) }
                )
            }
        }
    }
}

@Composable
private fun UserSection() {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(80.dp)
            .background(color = SectionBackgroundColor)
            .border(
                width = 1.dp,
                color = Color.Black.alpha(3),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp),
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = SpaceBetween
        ) {
            StyledText(
                text = UserInfo.name,
                style = Typography.bodyLarge,
                fontSize = 18
            )

            StyledText(
                text = UserInfo.email,
                style = Typography.bodyMedium,
                fontSize = 15
            )
        }

        Box(
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(26.dp)
                .background(color = ArrowBackgroundColor)
        ) {
            Icon(
                modifier = Modifier.align(Center),
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                tint = ArrowTint,
                contentDescription = null
            )
        }
    }
}

@Composable
private inline fun MyPageButton(
    stringId: Int,
    crossinline onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clickableWithoutRipple { onClick() },
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        StyledText(
            stringId = stringId,
            style = Typography.bodySmall,
            fontSize = 16
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            tint = SubColor,
            contentDescription = null
        )
    }
}