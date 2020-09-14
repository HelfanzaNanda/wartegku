package com.elf.wartegku.ui.food.detail_store

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.elf.wartegku.R
import com.elf.wartegku.models.Food
import com.elf.wartegku.models.Store
import com.elf.wartegku.ui.food.FoodClickInterface
import com.elf.wartegku.ui.food.FoodState
import com.elf.wartegku.ui.food.FoodViewModel
import com.elf.wartegku.ui.food.checkout.CheckoutActivity
import com.elf.wartegku.utils.Constants
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.content_detail_store.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailStoreActivity : AppCompatActivity(), FoodClickInterface {

    private val foodViewModel : FoodViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_store)
        supportActionBar?.hide()
        setUpRecyclerView()
        setViewStore()
        observe()
    }

    private fun setViewStore() {
        getPassedStore()?.let {
            txt_name.text = it.name
            txt_address.text = it.address
            img_store.load(R.drawable.store)
        }
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = FoodAdapter(mutableListOf(), this@DetailStoreActivity)
            layoutManager = LinearLayoutManager(this@DetailStoreActivity)
        }
    }

    private fun observe() {
        observeState()
        observeFoods()
        observeFilteredFoods()
    }

    private fun handleFoods(list: List<Food>?) {
        list?.let {
            recycler_view.adapter?.let { a -> if (a is FoodAdapter) a.changelist(it) }
            foodViewModel.setTotalItem(it)
            foodViewModel.setTotalPrice(it)
            foodViewModel.filterFoods(it)
            setViewCardCheckout()
        }
    }

    private fun handleFilteredFoods(list: List<Food>?) {
        list?.let {
            if (it.isNotEmpty()){
                card_checkout.visible()
                finishSelectFoods(it)
            }else{
                card_checkout.gone()
            }
        }
    }


    private fun fetchFoodsByStore() = foodViewModel.fetchFoodsByStore(getPassedStore()?.id.toString())
    private fun getPassedStore() : Store? = intent.getParcelableExtra("STORE")
    private fun observeState() = foodViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeFoods() = foodViewModel.listenToFoods().observe(this, Observer { handleFoods(it) })
    private fun observeFilteredFoods() = foodViewModel.listenToFilteredFoods().observe(this, Observer { handleFilteredFoods(it) })
    private fun getTotalItem() = foodViewModel.getTotalItem().value
    private fun getTotalPrice() = foodViewModel.getTotalPrice().value!!

    @SuppressLint("SetTextI18n")
    private fun setViewCardCheckout() {
        txt_qty.text = "${getTotalItem()} item"
        txt_name_store.text = getPassedStore()?.name
        txt_total_price.text = Constants.setToIDR(getTotalPrice())

    }

    private fun finishSelectFoods(foods: List<Food>) {
        card_checkout.setOnClickListener {
            foodViewModel.setSelectedFoods(foods)
            //startActivity(Intent(this@DetailStoreActivity, CheckoutActivity::class.java))
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

    override fun onResume() {
        super.onResume()
        fetchFoodsByStore()
    }

    override fun add(food: Food) = foodViewModel.addSelectedProduct(food)
    override fun increment(food: Food) = foodViewModel.incrementQuantity(food)
    override fun decrement(food: Food) = foodViewModel.decrementQuantity(food)
}
