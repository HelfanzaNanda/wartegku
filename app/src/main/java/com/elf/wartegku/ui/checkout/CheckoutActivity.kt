package com.elf.wartegku.ui.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elf.wartegku.R
import com.elf.wartegku.models.Food
import com.elf.wartegku.models.FoodSelected
import com.elf.wartegku.models.Order
import com.elf.wartegku.models.Store
import com.elf.wartegku.utils.Constants
import com.elf.wartegku.utils.ext.*
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.content_checkout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : AppCompatActivity() {

    private val checkoutViewModel : CheckoutViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = getPassedStore()?.name
        setUpRecyclerView()
        setSelectedFoods()
        observe()
        setUpTotalPrice()
        setUpDetail()
    }

    private fun observe() {
        observeState()
        observeSelectedFoods()
    }

    private fun observeState() = checkoutViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun setSelectedFoods() = checkoutViewModel.setSelectedFoods(getPassedSelectedFoods()!!)
    private fun observeSelectedFoods() = checkoutViewModel.listenToSelectedFoods().observe(this, Observer { handleSelectedFoods(it) })
    private fun getPassedSelectedFoods() = intent.getParcelableArrayListExtra<Food>("SELECTED_FOODS")
    private fun getPassedTotalPrice() = intent.getIntExtra("TOTAL_PRICE", 0)
    private fun getPassedStore() = intent.getParcelableExtra<Store>("STORE")
    private fun handleSelectedFoods(list: List<FoodSelected>?) = list?.let { order(it) }
    private fun isLoggedIn() = Constants.getToken(this@CheckoutActivity) != "UNDEFINED"


    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = CheckoutAdapter(mutableListOf())
            layoutManager = LinearLayoutManager(this@CheckoutActivity)
        }
    }

    private fun setUpDetail() {
        getPassedSelectedFoods()?.let {
            recycler_view.adapter?.let { a -> if (a is CheckoutAdapter) a.changelist(it) }
        }
    }

    private fun handleUiState(checkoutState: CheckoutState?) {
        checkoutState?.let {
            when(it){
                is CheckoutState.Loading -> handleLoading(it.state)
                is CheckoutState.ShowToast -> toast(it.message)
                is CheckoutState.Success -> handleSuccess()
            }
        }
    }

    private fun handleSuccess() = alertBasic(getString(R.string.message_success))

    private fun handleLoading(b: Boolean) {
        if (b){
            val doubleBounce = DoubleBounce() as Sprite
            loading.setIndeterminateDrawable(doubleBounce)
            loading.visible()
        }else{
            loading.gone()
        }
    }

    private fun setUpTotalPrice() {
        getPassedTotalPrice().let {
            txt_price.text = Constants.setToIDR(it)
            txt_postal_fee.text = "0"
            txt_total_price.text = Constants.setToIDR(it.plus(0))
        }
    }

    private fun order(foods : List<FoodSelected>) {
        btn_order.setOnClickListener {
            if (isLoggedIn()){
                val token = Constants.getToken(this@CheckoutActivity)
                val order = Order(store_id = getPassedStore()?.id, foods = foods)
                checkoutViewModel.order(token, order)
            }else{
                alertNotLogin(getString(R.string.message_not_login))
            }
        }
    }

}