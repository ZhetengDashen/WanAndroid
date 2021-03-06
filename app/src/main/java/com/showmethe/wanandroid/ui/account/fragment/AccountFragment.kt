package com.showmethe.wanandroid.ui.account.fragment




import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.ken.materialwanandroid.entity.TabItem
import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.FragmentAccuntBinding
import com.showmethe.wanandroid.dialog.TextJumpDialog
import com.showmethe.wanandroid.ui.account.adapter.TabArticleAdapter
import com.showmethe.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_accunt.*
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.util.extras.onTabSelected
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar


class AccountFragment : LazyFragment<FragmentAccuntBinding, MainViewModel>() {


    private var pos = 0
    private val dialog = TextJumpDialog()
    private lateinit var adapter : TabArticleAdapter
    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<TabItem>()

    override fun initViewModel(): MainViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_accunt



    override fun onBundle(bundle: Bundle) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fixToolbar(toolbar)
    }

    override fun observerUI() {

        viewModel.tabs.observe(this, Observer { result ->
            result?.apply {
                response?.apply {
                    forEach {
                        titles.add(TabItem(it.id,it.name))
                        fragments.add(ArticleFragment.get(it.id))
                    }
                    initVp()
                    tab.postDelayed({
                        dialog.dismiss()
                    },1000)
                }
            }
        })
    }


    override fun init() {


        binding?.article = this
        viewModel.getChapters()


    }

    override fun initListener() {

        tab.onTabSelected {
            onTabSelected {
                pos = position
            }
        }


    }


    fun onFabClick(view: View){
        viewModel.tabs.value?.apply {
            viewModel.callId.value = titles[pos].id
        }
    }

    private fun initVp(){

        adapter = TabArticleAdapter(fragments,titles,childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vp.adapter = adapter
        vp.offscreenPageLimit = fragments.size
        tab.setupWithViewPager(vp)

        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                pos = position
            }

        })


    }


}