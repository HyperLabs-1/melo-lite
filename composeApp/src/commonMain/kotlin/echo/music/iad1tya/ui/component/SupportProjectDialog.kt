package echo.music.iad1tya.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.hyperlabs_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun SupportProjectDialog(
    onDismiss: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Support the Project",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.hyperlabs_logo),
                    contentDescription = "HyperLabs",
                    modifier = Modifier.size(64.dp).padding(bottom = 4.dp)
                )

                Text(
                    text = "Melo Lite is a product of HyperLabs",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Button(
                    onClick = { uriHandler.openUri("https://www.instagram.com/hyperlabs.io") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.background,
                    )
                ) {
                    Text("Follow HyperLabs on Instagram", color = MaterialTheme.colorScheme.background)
                }

                OutlinedButton(
                    onClick = { uriHandler.openUri("https://github.com/HyperLabs-1/melo-lite") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Star on GitHub")
                }

                OutlinedButton(
                    onClick = { uriHandler.openUri("https://github.com/HyperLabs-1") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Follow HyperLabs on GitHub")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Don't show again")
            }
        }
    )
}
