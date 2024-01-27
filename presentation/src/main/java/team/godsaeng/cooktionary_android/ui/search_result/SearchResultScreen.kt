import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.theme.ImagePlaceHolderColor
import team.godsaeng.cooktionary_android.ui.theme.PointColor
import team.godsaeng.cooktionary_android.ui.theme.ProgressBackgroundColor
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey1
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey2
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey3
import team.godsaeng.cooktionary_android.ui.theme.Typography

@Composable
fun SearchResultScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        TopSection()

        Spacer(modifier = Modifier.height(20.dp))

        SearchResultDescSection(
            recipeCount = 6
        )

        Spacer(modifier = Modifier.height(20.dp))

        SearchResultSection()
    }
}

@Composable
private fun TopSection() {
    TopBar(
        onClickBackButton = {

        },
        middleContents = {

        },
        onClickProfileIcon = {

        }
    )

    ProgressBar(50)
}

@Composable
private fun ProgressBar(progress: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(color = ProgressBackgroundColor)
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp

        Box(
            modifier = Modifier
                .width((screenWidth / 100 * progress).dp)
                .fillMaxHeight()
                .background(color = PointColor)
        )
    }
}

@Composable
private fun SearchResultDescSection(recipeCount: Int) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        StyledText(
            text = stringResource(R.string.result_count, recipeCount),
            style = Typography.bodyMedium,
            fontSize = 14,
            color = TextColorGrey1
        )

        Spacer(modifier = Modifier.height(4.dp))

        StyledText(
            stringId = R.string.result_desc,
            style = Typography.bodyMedium,
            fontSize = 12,
            color = TextColorGrey2
        )
    }
}

@Composable
private fun SearchResultSection() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Recipe()
        }
    }
}

@Composable
private fun Recipe() {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(6.dp))
                .size(70.dp)
                .background(color = ImagePlaceHolderColor)
        ) {

        }

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StyledText(
                    text = "맛있는 계란 볶음밥",
                    style = Typography.titleLarge,
                    fontSize = 18,
                    color = Color.Black
                )

                StyledText(
                    text = "${stringResource(id = R.string.cooking_time)}",
                    style = Typography.titleLarge,
                    fontSize = 13,
                    color = TextColorGrey3
                )

                StyledText(
                    text = "즉시 조리 가능",
                    style = Typography.titleLarge,
                    fontSize = 13,
                    color = PointColor
                )
            }

            Image(
                modifier = Modifier.padding(6.dp),
                painter = painterResource(R.drawable.ic_scrap),
                contentDescription = null
            )
        }
    }
}