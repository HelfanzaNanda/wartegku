package com.elf.wartegku.ui.result_search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.elf.wartegku.R
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_result_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultSearchActivity : AppCompatActivity() {

    private val resultSearchViewModel : ResultSearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_search)
        supportActionBar?.hide()
        observe()
    }

    private fun observe() { resultSearchViewModel.listenToState().observer(this, Observer { handleUiState(it) }) }

    private fun handleUiState(resultSearchState: ResultSearchState?) {
        resultSearchState?.let {
            when(it){
                is ResultSearchState.Loading -> handleLoading(it.state)
                is ResultSearchState.ShowToast -> toast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        if (b) {
            val doubleBounce = DoubleBounce()
            doubleBounce.setBounds(0, 0, 100, 100)
            loading.setIndeterminateDrawable(doubleBounce)
            loading.visible()
        } else {
            loading.gone()
        }
    }

    private fun search() = resultSearchViewModel.searchStoresByFood(getPassedKey()!!)
    private fun getPassedKey() = intent.getStringExtra("KEY")
    private fun setKeyInEditText() = et_search.setText(getPassedKey())

    override fun onResume() {
        super.onResume()
        search()
        setKeyInEditText()
    }
}