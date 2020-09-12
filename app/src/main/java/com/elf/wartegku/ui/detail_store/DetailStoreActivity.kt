package com.elf.wartegku.ui.detail_store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.wartegku.R
import com.elf.wartegku.models.Food
import com.elf.wartegku.models.Store
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_detail_store.*
import kotlinx.android.synthetic.main.content_detail_store.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailStoreActivity : AppCompatActivity() {

    private val detailStoreViewModel : DetailStoreViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_store)
        setSupportActionBar(toolbar)
        setUpUI()
        observe()
    }

    private fun setUpUI() {
        recycler_view.apply {
            adapter = FoodAdapter(mutableListOf(), this@DetailStoreActivity)
            layoutManager = LinearLayoutManager(this@DetailStoreActivity)
        }
    }

    private fun observe() {
        detailStoreViewModel.listenToState().observer(this, Observer { handleUiState(it) })
        detailStoreViewModel.listenToFoods().observe(this, Observer { handleFoods(it) })
    }

    private fun handleFoods(list: List<Food>?) {
        list?.let {
            recycler_view.adapter?.let { adapter ->
                if (adapter is FoodAdapter) adapter.changelist(it)
            }
        }
    }

    private fun handleUiState(detailStoreState: DetailStoreState?) {
        detailStoreState?.let {
            when(it){
                is DetailStoreState.Loading -> handleLoading(it.state)
                is DetailStoreState.ShowToast -> toast(it.messsage)
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        if (b){
            val doubleBounce = DoubleBounce() as Sprite
            loading.setIndeterminateDrawable(doubleBounce)
            loading.visible()
        }else{
            loading.gone()
        }
    }

    private fun fetchFoodsByStore() = detailStoreViewModel.fetchFoodsByStore(getPassedStore()?.id.toString())
    private fun getPassedStore() : Store? = intent.getParcelableExtra("STORE")

    override fun onResume() {
        super.onResume()
        fetchFoodsByStore()
    }
}