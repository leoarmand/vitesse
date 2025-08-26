package fr.vitesse.android.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.rememberAsyncImagePainter
import fr.vitesse.android.R

@Composable
fun AvatarComponent(avatarUri: Uri? = null, onAvatarUriChanged: ((uri: Uri) -> Unit)? = null) {
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            onAvatarUriChanged?.invoke(uri)
        }
    }

    Box(
        modifier = Modifier
            .aspectRatio(3f / 2f)
            .clickable(enabled = (onAvatarUriChanged != null)) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
    ) {
        var painter = painterResource(R.drawable.default_avatar)
        if (avatarUri != null) {
            painter = rememberAsyncImagePainter(avatarUri)
        }
        Image(
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painter,
            contentDescription = stringResource(id = R.string.default_avatar)
        )
    }
}
