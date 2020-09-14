package com.elf.wartegku.ui.category.store

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.elf.wartegku.R
import com.elf.wartegku.models.Category
import com.elf.wartegku.models.Store
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.fragment_store.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreFragment : Fragment(R.layout.fragment_store){

    private val storeViewModel : StoreViewModel by viewModel()

    companion object {
        fun instance(category: Category?) : StoreFragment {
            return if (category == null) {
                StoreFragment()
            } else {
                val args = Bundle()
                args.putParcelable("category", category)
                StoreFragment().apply {
                    arguments = args
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        observe()
    }

    private fun setUpUI() {
        requireView().recycler_view.apply {
            adapter = StoreAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        storeViewModel.listenToState().observer(requireActivity(), Observer { handleUiState(it) })
        storeViewModel.listenToStores().observe(requireActivity(), Observer { handleStores(it) })
    }

    private fun handleStores(list: List<Store>?) {
        list?.let {
            requireView().recycler_view.adapter?.let { adapter ->
                if (adapter is StoreAdapter){
                    adapter.changelist(it)
                }
            }
        }
    }

    private fun handleUiState(storeState: StoreState?) {
        storeState?.let {
            when(it){
                is StoreState.Loading -> handleLoading(it.state)
                is StoreState.ShowToast -> requireActivity().toast(it.message)
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        if (b){
            val doubleBounce = DoubleBounce() as Sprite
            requireView().loading.setIndeterminateDrawable(doubleBounce)
            requireView().loading.visible()
        }else{
            requireView().loading.gone()
        }
    }

    private fun getPassedCategory(): Category? = requireArguments().getParcelable("category")
    private fun fetchStoresByCategory() = storeViewModel.fetchStoresByCategory(getPassedCategory()!!.id.toString())

    override fun onResume() {
        super.onResume()
        fetchStoresByCategory()
    }
}