package team.godsaeng.cooktionary_android.ui.recipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.ScrapButton
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.theme.PointColor
import team.godsaeng.cooktionary_android.ui.theme.RecipeDetailTextColor
import team.godsaeng.cooktionary_android.ui.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeScreen(
    navController: NavController
) {
    HorizontalPager(
        state = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { 1 }
        ),
        modifier = Modifier.fillMaxSize(),
    ) {
        RecipeDetail()
    }
}

@Composable
fun RecipeDetail() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colors.background)
    ) {
        TopBar(
            onClickBackButton = {},
            middleContents = {},
            onClickProfileIcon = {}
        )

        // todo : Coil
//        Image(
//            modifier = Modifier.fillMaxWidth(),
//            painter = painterResource("img_recipe_test.png"),
//            contentScale = ContentScale.FillWidth,
//            contentDescription = null
//        )

        Spacer(modifier = Modifier.height(18.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                StyledText(
                    text = "두부 탕수",
                    style = Typography.bodyMedium,
                    fontSize = 18
                )

                ScrapButton(onClick = {})
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = CenterVertically) {
                DifficultySection(2)

                Spacer(modifier = Modifier.width(16.dp))

                TimeSection(35)
            }

            Spacer(modifier = Modifier.height(24.dp))

            StyledText(
                text = "작은 큐브형으로 자른 두부를 기름에 노릇하게 튀겨 탕수 소스를 곁들였어요. 겉은 바삭, 속은 부드러운 두부의 단백한 속살에 새콤 달콤한 소스가 배어든 별미 반찬이에요. 간단한 레시피지만 손님 초대상에 올려도 근사하답니다.작은 큐브형으로 자른 두부를 기름에 노릇하게 튀겨 탕수 소스를 곁들였어요. 겉은 바삭, 속은 부드러운 두부의 단백한 속살에 새콤 달콤한 소스가 배어든 별미 반찬이에요. 간단한 레시피지만 손님 초대상에 올려도 근사하답니다.작은 큐브형으로 자른 두부를 기름에 노릇하게 튀겨 탕수 소스를 곁들였어요. 겉은 바삭, 속은 부드러운 두부의 단백한 속살에 새콤 달콤한 소스가 배어든 별미 반찬이에요. 간단한 레시피지만 손님 초대상에 올려도 근사하답니다.작은 큐브형으로 자른 두부를 기름에 노릇하게 튀겨 탕수 소스를 곁들였어요. 겉은 바삭, 속은 부드러운 두부의 단백한 속살에 새콤 달콤한 소스가 배어든 별미 반찬이에요. 간단한 레시피지만 손님 초대상에 올려도 근사하답니다.",
                style = Typography.bodyMedium,
                fontSize = 14,
                color = RecipeDetailTextColor,
                lineHeight = 1.5f
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DifficultySection(difficulty: Int) {
    Row(verticalAlignment = CenterVertically) {
        StyledText(
            stringId = R.string.difficulty,
            style = Typography.bodyMedium,
            fontSize = 12,
            color = PointColor
        )

        Spacer(modifier = Modifier.width(4.dp))

        repeat(3 - difficulty) {
            Image(
                painter = painterResource(R.drawable.ic_star_empty),
                contentDescription = null
            )
        }

        repeat(difficulty) {
            Image(
                painter = painterResource(R.drawable.ic_star_filled),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun TimeSection(timeAsMinute: Int) {
    Row(verticalAlignment = CenterVertically) {
        StyledText(
            stringId = R.string.cooking_time,
            style = Typography.bodyMedium,
            fontSize = 12,
            color = PointColor
        )

        Spacer(modifier = Modifier.width(4.dp))

        Image(
            painter = painterResource(R.drawable.ic_time),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(1.5.dp))

        StyledText(
            text = stringResource(id = R.string.minute, timeAsMinute),
            style = Typography.bodyMedium,
            fontSize = 10,
            color = PointColor
        )
    }
}