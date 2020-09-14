package com.elf.wartegku.ui.main.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.elf.wartegku.R
import com.elf.wartegku.models.Category
import com.elf.wartegku.models.Food
import com.elf.wartegku.ui.result_search.ResultSearchActivity
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel : HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpImageSlider()
        observe()
        btnSearch()
    }

    private fun setUpImageSlider() {
        requireView().image_slider.apply {
            setSliderAdapter(ImageSliderAdapter(requireActivity(), mutableListOf()))
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 4
        }.startAutoCycle()
    }

    private fun setUpUI() {
        requireView().recycler_view_food.apply {
            adapter = FoodByCategoryAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 4)
        }

        requireView().recycler_view_drink.apply {
            adapter = FoodByCategoryAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 4)
        }
    }

    private fun observe() {
        homeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
        homeViewModel.listenToCategoriesByFood()
            .observe(viewLifecycleOwner, Observer { handleCategoriesByFood(it) })
        homeViewModel.listenToCategoriesByDrink()
            .observe(viewLifecycleOwner, Observer { handleCategoriesByDrink(it) })
        homeViewModel.listenToFoodsLatest().observe(viewLifecycleOwner, Observer { handleFoodsLatest(it) })
    }

    private fun handleFoodsLatest(list: List<Food>?) {
        list?.let {
            requireView().image_slider.sliderAdapter?.let { pagerAdapter ->
                if (pagerAdapter is ImageSliderAdapter){
                    pagerAdapter.changelist(it)
                }
            }
        }
    }

    private fun handleCategoriesByDrink(list: List<Category>?) {
        list?.let {
            requireView().recycler_view_drink.adapter?.let { adapter ->
                if (adapter is FoodByCategoryAdapter) {
                    adapter.changelist(it)
                }
            }
        }
    }

    private fun handleCategoriesByFood(list: List<Category>?) {
        list?.let {
            requireView().recycler_view_food.adapter?.let { adapter ->
                if (adapter is FoodByCategoryAdapter) {
                    adapter.changelist(it)
                }
            }
        }
    }

    private fun handleUiState(homeState: HomeState?) {
        homeState?.let {
            when (it) {
                is HomeState.Loading -> handleLoading(it.state)
                is HomeState.ShowToast -> requireActivity().toast(it.message)
            }
        }
    }

    private fun btnSearch(){
        requireView().et_search.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                if (textView.text.toString().isEmpty()){
                    requireActivity().toast("tidak boleh kosong")
                }else{
                    goToResultSearchActivity(textView.text.toString())
                }
                true
            }else{
                false
            }
        }
    }

    private fun goToResultSearchActivity(key : String){
        startActivity(Intent(requireActivity(), ResultSearchActivity::class.java).apply {
            putExtra("KEY", key)
        })
    }

    private fun handleLoading(b: Boolean) {
        if (b) {
            val doubleBounce = DoubleBounce()
            doubleBounce.setBounds(0, 0, 100, 100)
            requireView().loading.setIndeterminateDrawable(doubleBounce)
            requireView().loading.visible()
            requireView().label_food.gone()
            requireView().label_drink.gone()
        } else {
            requireView().loading.gone()
            requireView().label_food.visible()
            requireView().label_drink.visible()
        }
    }

    private fun fetchCategories() = homeViewModel.fetchCategoriesByFood()
    private fun fetchFoodsLatest() = homeViewModel.fetchFoodsLatest()

    override fun onResume() {
        super.onResume()
        fetchCategories()
        fetchFoodsLatest()
    }
}