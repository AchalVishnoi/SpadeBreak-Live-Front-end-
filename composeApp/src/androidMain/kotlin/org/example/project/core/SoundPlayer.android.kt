package org.example.project.core

import android.content.Context
import android.media.SoundPool
import org.example.project.R

actual class SoundPlayer(context: Context){

    private val soundPool=SoundPool.Builder()
        .setMaxStreams(6)
        .build()

    private val soundMap= mapOf(

        UiSound.CLICK to soundPool.load(context, R.raw.button_click,1)


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
