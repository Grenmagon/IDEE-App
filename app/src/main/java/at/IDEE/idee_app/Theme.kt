package at.IDEE.idee_app

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Navy,
    secondary = AppColors.Gold,
    background = AppColors.LightBackground,
    surface = AppColors.White,
    onPrimary = AppColors.White,
    onBackground = AppColors.TextPrimary
)
val Typography = Typography() //Vielleicht später auslagern wenn schrift individuell gemacht wird
@Composable
fun IstDasEhErlaubt_v2Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}