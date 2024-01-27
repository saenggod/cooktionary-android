package team.godsaeng.cooktionary_android.ui.my_page

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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.alpha
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.theme.ArrowBackgroundColor
import team.godsaeng.cooktionary_android.ui.theme.ArrowTint
import team.godsaeng.cooktionary_android.ui.theme.SubColor
import team.godsaeng.cooktionary_android.ui.theme.Typography
import team.godsaeng.cooktionary_android.ui.theme.SectionBackgroundColor

@Composable
fun MyPageScreen(
    navController: NavController
) {
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
            MyPageButton(stringId = R.string.saved_recipe_list)
            MyPageButton(stringId = R.string.service_terms_of_use)
            MyPageButton(stringId = R.string.privacy_policy)
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
                text = "이름",
                style = Typography.bodyLarge,
                fontSize = 18
            )

            StyledText(
                text = "이메일",
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
private fun MyPageButton(
    stringId: Int
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clickableWithoutRipple { },
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