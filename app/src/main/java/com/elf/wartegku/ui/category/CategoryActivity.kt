package com.elf.wartegku.ui.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.elf.wartegku.R
import com.elf.wartegku.models.Category
import com.elf.wartegku.ui.category.store.StoreFragment
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_category.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CategoryActivity : AppCompatActivity() {

    private val categoryViewModel : CategoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        observe()
    }

    private fun observe() {
        categoryViewModel.listenToState().observer(this, Observer { handleUiState(it) })
        getPassedCategory()?.let {
            if (it.is_food){
                categoryViewModel.listenToCategoriesByFood().observe(this, Observer { handleCategories(it) })
            }else{
                categoryViewModel.listenToCategoriesByDrink().observe(this, Observer { handleCategories(it) })
            }
        }

    }

    private fun handleCategories(list: List<Category>?) {
        list?.let {
            val adapter = ViewPagerAdapter(supportFragmentManager)
            //adapter.addFragment(StoreFragment.instance(null), "semua")
            it.forEach { category ->
                adapter.addFragment(StoreFragment.instance(category), category.category!!)
            }
            view_pager.adapter = adapter
            tab_layout.setupWithViewPager(view_pager)
            val findIndex = it.indexOf(getPassedCategory())
            tab_layout.getTabAt(findIndex)!!.select()
        }
    }

    private fun handleUiState(categoryState : CategoryState?) {
        categoryState?.let {
            when(it){
                is CategoryState.Loading -> handleLoading(it.state)
                is CategoryState.ShowToast -> toast(it.message)
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

    private fun fetchCategories(){
        getPassedCategory()?.let {
            if (it.is_food){
                categoryViewModel.fetchCategoriesByFood()
            }else{
                categoryViewModel.fetchCategoriesByFood()
            }
        }
    }

    private fun getPassedCategory() : Category? = intent.getParcelableExtra("CATEGORY")

    override fun onResume() {
        super.onResume()
        fetchCategories()
    }

}

class ViewPagerAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments: MutableList<Fragment> = ArrayList()
    private val titles: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}