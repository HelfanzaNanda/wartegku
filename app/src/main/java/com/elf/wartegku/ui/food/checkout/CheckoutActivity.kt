package com.elf.wartegku.ui.food.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.wartegku.R
import com.elf.wartegku.models.Food
import com.elf.wartegku.ui.category.food.checkout.CheckoutAdapter
import com.elf.wartegku.ui.food.FoodState
import com.elf.wartegku.ui.food.FoodViewModel
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.content_checkout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : AppCompatActivity() {

    private val foodViewModel : FoodViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(findViewById(R.id.toolbar))
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = CheckoutAdapter(mutableListOf())
            layoutManager = LinearLayoutManager(this@CheckoutActivity)
        }
    }

    private fun observe() {
        observeState()
        observeSelectedFoods()
    }

    private fun observeState() = foodViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeSelectedFoods() = foodViewModel.listenToSelectedFoods().observe(this, Observer { handleFoodsSelected(it) })


    private fun handleFoodsSelected(list: List<Food>?) {
        list?.let {
            recycler_view.adapter?.let { a ->
                if (a is CheckoutAdapter) a.changelist(it)
            }
        }
    }

    private fun handleUiState(foodState: FoodState?) {
        foodState?.let {
            when(it){
                is FoodState.Loading -> handleLoading(it.state)
                is FoodState.ShowToast -> toast(it.messsage)
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
}