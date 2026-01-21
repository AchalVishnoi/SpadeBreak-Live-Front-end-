package org.example.project.features.entry.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.wooden_background

@Composable
fun EntryBoardScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        val viewmodel = koinViewModel<HomeViewModel>()



        Image(
            painter = painterResource(Res.drawable.wooden_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )



    }

}