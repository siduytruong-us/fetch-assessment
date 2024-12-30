package com.duyts.fetch.home.model

import androidx.compose.runtime.Immutable
import com.duyts.fetch.core.data.model.HiringItem


@Immutable
sealed interface DisplayHiringItem {
	fun getKey(): Int
	data class Header(val listID: Int) : DisplayHiringItem {
		override fun getKey(): Int = listID
	}

	data class Item(val item: HiringItem) : DisplayHiringItem {
		override fun getKey(): Int = item.id
	}
}