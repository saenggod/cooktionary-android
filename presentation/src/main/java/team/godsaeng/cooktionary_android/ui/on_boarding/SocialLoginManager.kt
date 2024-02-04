package team.godsaeng.cooktionary_android.ui.on_boarding

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class SocialLoginManager(
    private val context: Context,
    val onSuccess: (String, String) -> Unit,
    val onFailure: () -> Unit
) {
    private val kakaoLoginInstance = UserApiClient.instance
    private lateinit var googleLoginLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
    private val googleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_CLIENT_ID)
            .requestServerAuthCode(GOOGLE_CLIENT_ID)
            .build()
    )

    fun loginWithKakao() {
        val loginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            token?.let { onSuccess(PLATFORM_KAKAO, it.accessToken) }
            error?.let { onFailure() }
        }

        if (kakaoLoginInstance.isKakaoTalkLoginAvailable(context)) {
            kakaoLoginInstance.loginWithKakaoTalk(
                context = context,
                callback = loginCallback
            )
        } else {
            kakaoLoginInstance.loginWithKakaoAccount(
                context = context,
                callback = loginCallback
            )
        }
    }

    @Composable
    fun InitGoogleLoginLauncher() {
        googleLoginLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                GoogleSignIn.getSignedInAccountFromIntent(result.data).result.serverAuthCode?.let {
                    onSuccess(PLATFORM_GOOGLE, it)
                } ?: run {
                    onFailure()
                }
            }
        )
    }

    fun launchGoogleLoginLauncher() {
        googleLoginLauncher.launch(googleSignInClient.signInIntent)
    }

    companion object {
        const val GOOGLE_CLIENT_ID = "1008080216028-jk0o1bcv13q8q99b9ugoi59jqqai2bbd.apps.googleusercontent.com"
        const val PLATFORM_KAKAO = "kakao"
        const val PLATFORM_GOOGLE = "google"
    }
}