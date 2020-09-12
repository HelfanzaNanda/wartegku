package com.elf.wartegku.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.elf.wartegku.R
import com.elf.wartegku.ui.main.MainActivity
import com.elf.wartegku.utils.Constants
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.github.appintro.model.SliderPage


class IntroActivity : AppIntro2(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val sliderPage = SliderPage().apply {
            title = "Daftar WartegKu Sekarang"
            description = "Segera daftarkan diri Anda untuk mencari makanan favorit Anda di berbagai warteg"
            titleColor = Color.parseColor("#810000")
            backgroundColor = Color.parseColor("#ffffff")
            descriptionColor = Color.parseColor("#810000")
            imageDrawable = R.drawable.ic_undraw_street_food
        }

        val sliderPage2 = SliderPage().apply {
            title = "Kangen Masakan Rumah?"
            description = "Jangan takut, aplikasi WartegKu dapat mencarikan aneka masakan rumahan dari berbagai Warteg."
            imageDrawable = R.drawable.ic_undraw_eating_together
            titleColor = Color.parseColor("#810000")
            backgroundColor = Color.parseColor("#ffffff")
            descriptionColor = Color.parseColor("#810000")
        }

        val sliderPage3 = SliderPage().apply {
            title = "Males Beli Lauk/Makanan?"
            description = "Tenang gaes, WartegKu dapat membantu Anda untuk mencari dan memesan lauk di berbagai Warteg."
            imageDrawable = R.drawable.ic_undraw_review
            titleColor = Color.parseColor("#810000")
            backgroundColor = Color.parseColor("#ffffff")
            descriptionColor = Color.parseColor("#810000")
        }
        addSlide(AppIntroFragment.newInstance(sliderPage))
        addSlide(AppIntroFragment.newInstance(sliderPage2))
        addSlide(AppIntroFragment.newInstance(sliderPage3))
        setTransformer(AppIntroPageTransformerType.Zoom)
        isSkipButtonEnabled = true
        isVibrate = true
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        Constants.setFirstTime(this@IntroActivity, false).also {
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            20 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("IntroAct", "Permission has been denied by user")
                } else {
                    Log.i("IntroAct", "Permission has been granted by user")
                }
            }
        }
    }
}