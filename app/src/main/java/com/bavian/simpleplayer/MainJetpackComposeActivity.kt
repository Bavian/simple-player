package com.bavian.simpleplayer

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainJetpackComposeActivity : AppCompatActivity() {

    private val chooseDirectory = {
        startActivity(Intent(this, DirectoryChooser::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChooseDirectoryLayout()
        }
    }

    @Preview
    @Composable
    fun ChooseDirectoryLayout() {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    stringResource(id = R.string.question_on_start),
                    style = MaterialTheme.typography.h6,
                )
                Text(
                    stringResource(id = R.string.tip_on_start),
                    style = MaterialTheme.typography.subtitle1,
                )
                Button(onClick = chooseDirectory) {
                    Text(stringResource(id = R.string.choose_directory_button))
                }
            }
            Row(
                Modifier
                    .height(75.dp)
                    .padding(start = 7.5.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(
                    modifier = Modifier
                        .width(48.75.dp)
                        .height(48.75.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.album),
                        contentDescription = stringResource(id = R.string.album_description),
                    )
                }
                Text(
                    "",
                    modifier = Modifier.weight(5f),
                    style = MaterialTheme.typography.subtitle1,
                )
                Text(
                    "",
                    style = MaterialTheme.typography.subtitle2,
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_pause_28),
                    contentDescription = stringResource(id = R.string.pause_description),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_mini_player_next_28),
                    contentDescription = stringResource(id = R.string.next_description),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight(),
                )
            }
        }
    }
}