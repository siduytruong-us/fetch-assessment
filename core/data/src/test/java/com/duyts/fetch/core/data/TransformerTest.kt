package com.duyts.fetch.core.data

import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.fetch.core.data.model.toModel
import com.duyts.fetch.core.data.transformer.DataTransformer
import com.duyts.fetch.core.data.transformer.DataTransformerImpl
import com.duyts.fetch.network.model.HiringItemsResponseItem
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class TransformerTest {

	private lateinit var transformer: DataTransformer

	@Before
	fun setup() {
		transformer = DataTransformerImpl()
	}

	@Test
	fun `transform from Entities to GroupModel`() {
		val entity = HiringItemEntity(1, "Alice", 1)
		val result = transformer.transform(entity)
		assertEquals(result, entity.toModel())
	}

	@Test
	fun `transform from ResponseItem to EntityItem`() {
		val responseItem = HiringItemsResponseItem(name = "Alice", id = 1, listId = 101)
		val expectedEntity = HiringItemEntity(id = 1, name = "Alice", listId = 101)

		val result = transformer.transform(responseItem)

		assertEquals(expectedEntity, result)
	}
}