package com.example.aupairconnect

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aupairconnect.fragments.AccountFragment
import com.example.aupairconnect.fragments.DiscoveryFragment
import com.example.aupairconnect.fragments.MessagesFragment

class ACViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return   when(position){
            0->{
                DiscoveryFragment()
            }
            1->{
                MessagesFragment()
            }
            2->{
                AccountFragment()
            }
            else->{
                DiscoveryFragment()
            }

        }
    }
}