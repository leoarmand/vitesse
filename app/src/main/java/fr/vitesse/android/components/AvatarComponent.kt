package fr.vitesse.android.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import fr.vitesse.android.R

@Composable
fun AvatarComponent(isSmallVersion: Boolean = false, avatarUri: Uri? = null, onAvatarUriChanged: ((uri: Uri) -> Unit)? = null) {
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            onAvatarUriChanged?.invoke(it)
        }
    }

    Box(
        modifier =  if (isSmallVersion)
                        Modifier.width(54.dp).height(54.dp)
                    else
                        Modifier.aspectRatio(3f / 2f).clickable(enabled = (onAvatarUriChanged != null)) {
                            pickMedia.launch(arrayOf("image/*"))
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
