package team.godsaeng.cooktionary_android.ui.secession

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.theme.CheckBoxDisabledColor
import team.godsaeng.cooktionary_android.ui.theme.SectionBackgroundColor
import team.godsaeng.cooktionary_android.ui.theme.SubColor
import team.godsaeng.cooktionary_android.ui.theme.Typography

@Composable
fun SecessionScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            TopSection()

            Spacer(modifier = Modifier.height(24.dp))

            SecessionDescSection()

            Spacer(modifier = Modifier.height(16.dp))

            AgreementSection()

            Spacer(modifier = Modifier.height(38.dp))

            SecessionButton()
        }
    }
}

@Composable
private fun TopSection() {
    TopBar(
        onClickBackButton = {},
        middleContents = {
            StyledText(
                stringId = R.string.secession,
                style = Typography.bodyMedium,
                fontSize = 16
            )
        }
    )
}

@Composable
private fun SecessionDescSection() {
    StyledText(
        stringId = R.string.notice_secession,
        style = Typography.titleLarge,
        fontSize = 20
    )

    Spacer(modifier = Modifier.height(10.dp))

    StyledText(
        stringId = R.string.notice_secession_desc,
        style = Typography.bodySmall,
        fontSize = 14
    )

    Spacer(modifier = Modifier.height(28.dp))

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .background(color = SectionBackgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            SecessionDescItem(number = 1, stringId = R.string.notice_secession_detail_1)
            SecessionDescItem(number = 2, stringId = R.string.notice_secession_detail_2)
            SecessionDescItem(number = 3, stringId = R.string.notice_secession_detail_3)
            SecessionDescItem(number = 4, stringId = R.string.notice_secession_detail_4)
            SecessionDescItem(number = 5, stringId = R.string.notice_secession_detail_5)
            SecessionDescItem(number = 6, stringId = R.string.notice_secession_detail_6)
        }
    }
}

@Composable
private fun SecessionDescItem(
    number: Int,
    stringId: Int
) {
    Row {
        StyledText(
            text = "$number.",
            style = Typography.bodySmall,
            fontSize = 13
        )

        Spacer(modifier = Modifier.width(4.dp))

        StyledText(
            stringId = stringId,
            style = Typography.bodySmall,
            fontSize = 13
        )
    }
}

@Composable
private fun AgreementSection() {
    Row(
        modifier = Modifier.clickableWithoutRipple { },
        verticalAlignment = CenterVertically
    ) {
        CheckBox()

        Spacer(modifier = Modifier.width(8.dp))

        StyledText(
            stringId = R.string.agreement_desc,
            style = Typography.bodyMedium,
            fontSize = 12
        )
    }
}

@Composable
private fun CheckBox() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .border(
                width = 1.dp,
                color = CheckBoxDisabledColor
            )
    ) {
        Icon(
            modifier = Modifier.align(Center),
            painter = painterResource(id = R.drawable.ic_check),
            tint = CheckBoxDisabledColor,
            contentDescription = null
        )
    }
}

@Composable
private fun SecessionButton() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .height(48.dp)
            .background(SubColor)
            .clickableWithoutRipple { }
    ) {
        StyledText(
            modifier = Modifier.align(Center),
            stringId = R.string.do_secession,
            style = Typography.bodyMedium,
            fontSize = 14,
            color = Color.White
        )
    }
}