@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.example.deletebutton.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deletebutton.designsystem.icon.ApplicationIcon
import com.example.deletebutton.designsystem.theme.DeleteButtonTheme

//px = dp * (dpi)

//dp = px / (dpi)

@Composable
fun ScreenRoot(paddingValues: PaddingValues) {

    Screen(
        bottomPaddingValues = paddingValues.calculateBottomPadding() + paddingValues.calculateTopPadding(),
    )
}

@Composable
private fun Screen(bottomPaddingValues: Dp) {
    val windowSize = currentWindowSize().width
    val density = LocalDensity.current.density
    val dpVersion = windowSize / density
    val check = dpVersion < 600

    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = bottomPaddingValues),
        contentAlignment = if (!check) (Alignment.Center) else Alignment.BottomCenter
    ) {
        Content()
    }
}

@Composable
private fun Content() {
    val boxColor = MaterialTheme.colorScheme.errorContainer
    val boxShape = RoundedCornerShape(14)
    val iconColor = MaterialTheme.colorScheme.onErrorContainer
    val paddingDp = 18.dp
    val text = listOf(
        buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = iconColor
                )
            ) {
                append("Are you sure?")
            }
        },
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Medium,
                    color = iconColor.copy(.6f)
                )
            ) {
                append("Deleting your account is permanent.\n")
                append("You'll lose access to all your posts, messages, followers, and saved content.")
            }
        },
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Medium,
                    color = iconColor.copy(.6f)
                )
            ) {
                append("This action can't be undone.")
            }
        }
    )

    Box(
        Modifier
            .height(IntrinsicSize.Min)
            .widthIn(max = 423.dp)
            .background(color = boxColor, boxShape)
            .padding(paddingDp),
    ) {
        Column {
            Icon(
                painterResource(ApplicationIcon.Alert),
                "Alert",
                Modifier
                    .size(50.dp),
                tint = iconColor,
            )
            Spacer(Modifier.height(16.dp))
            text.forEachIndexed { idx, text ->
                Text(text, maxLines = 3, overflow = TextOverflow.Ellipsis)
                if (idx == 1) {
                    Spacer(Modifier.height(12.dp))
                } else {
                    Spacer(Modifier.height(16.dp))
                }
            }
            DeleteButton()
        }
    }
}


@Composable
private fun DeleteButton() {
    val text = "Hold to Delete"
    val textColor = MaterialTheme.colorScheme.onBackground
    val overlayTextColor = MaterialTheme.colorScheme.errorContainer
    val bgColor = MaterialTheme.colorScheme.onErrorContainer
    var isPressed by remember { mutableStateOf(false) }
    val animatedTextWidth by animateFloatAsState(
        if (isPressed) 1f else 0f,
        if (isPressed) {
            tween(2000, easing = LinearEasing)
        } else {
            tween(200, easing = LinearEasing)
        }
    )
    val view = LocalView.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(24.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                        try {
                            awaitRelease()
                        } finally {
                            isPressed = false
                        }
                    }
                )
            },
    ) {
        ButtonText(
            isPressed,
            text,
            textColor,
        )
        ButtonOverlayBox(animatedTextWidth, bgColor)
        ButtonText(
            isPressed,
            text,
            overlayTextColor,
            Modifier
                .drawWithContent {
                    val maskRight = size.width * animatedTextWidth
                    clipRect(
                        right = maskRight
                    ) {
                        this@drawWithContent.drawContent()
                    }
                }
        )
    }
}

@Composable
private fun ButtonOverlayBox(
    animatedWidth: Float,
    bgColor: Color,
) {
    Box(
        Modifier
            .fillMaxWidth(animatedWidth)
            .fillMaxHeight()
            .background(bgColor)
    )
}

@Composable
private fun ButtonText(
    isPressed: Boolean,
    text: String,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    val animatedTextScale by animateFloatAsState(
        if (isPressed) .9f else 1f,
        MaterialTheme.motionScheme.slowEffectsSpec(),
    )

    Text(
        text,
        color = textColor,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        letterSpacing = 0.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .graphicsLayer(
                scaleX = animatedTextScale,
                scaleY = animatedTextScale
            )
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun ScreenPreview() {
    DeleteButtonTheme {
        Screen(50.dp)
    }
}
