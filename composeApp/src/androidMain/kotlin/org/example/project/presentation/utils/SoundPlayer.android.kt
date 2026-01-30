package org.example.project.presentation.utils

import android.content.Context
import android.media.SoundPool
import org.example.project.R

actual class SoundPlayer(context: Context){

    private val soundPool=SoundPool.Builder()
        .setMaxStreams(6)
        .build()

    private val soundMap= mapOf(

        UiSound.CLICK to soundPool.load(context, R.raw.button_click,1),
        UiSound.WATER_DROP to soundPool.load(context,R.raw.water_drip,1),
        UiSound.READY_SOUND to soundPool.load(context,R.raw.ready_sound,1)

    )

    actual fun playSound(sound: UiSound) {
        soundMap[sound]?.let{
            soundPool.play(it,1f,1f,1,0,1f)
        }

    }

    fun release(){
        soundPool.release()
    }


}
