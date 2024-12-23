package com.duyts.fetch.core.data

import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.fetch.core.data.ext.testingObserveHiringItems
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.transformer.DataTransformer
import com.duyts.fetch.core.data.transformer.DataTransformerImpl
import com.duyts.fetch.network.model.HiringItemsResponseItem
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
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
		val entities = listOf(
			HiringItemEntity(1, "Alice", 1),
			HiringItemEntity(1, "Bob", 2)
		)
		val result = transformer.transform(entities)
		assertEquals(result, entities.testingObserveHiringItems())
	}

	@Test
	fun `transform from ResponseItem to EntityItem`() {
		val responseItem = HiringItemsResponseItem(name = "Alice", id = 1, listId = 101)
		val expectedEntity = HiringItemEntity(id = 1, name = "Alice", listId = 101)

		val result = transformer.transform(responseItem)

		assertEquals(expectedEntity, result)
	}

	@Test
	fun `transform filters out entities with blank or null names`() {
		val entities = listOf(
			HiringItemEntity(id = 1, name = "Alice", listId = 101),
			HiringItemEntity(id = 2, name = "", listId = 101),
			HiringItemEntity(id = 3, name = null, listId = 102)
		)
		val expectedMap = mapOf(
			101 to listOf(HiringItem(id = 1, name = "Alice"))
		)

		val result = transformer.transform(entities)

		assertEquals(expectedMap, result)
	}

	@Test
	fun `transform sorts entities by listId and name`() {
		val entities = listOf(
			HiringItemEntity(id = 2, name = "Bob", listId = 101),
			HiringItemEntity(id = 1, name = "Alice", listId = 101),
			HiringItemEntity(id = 3, name = "Charlie", listId = 102)
		)
		val expectedMap = mapOf(
			101 to listOf(
				HiringItem(id = 1, name = "Alice"),
				HiringItem(id = 2, name = "Bob")
			),
			102 to listOf(
				HiringItem(id = 3, name = "Charlie")
			)
		)

		val result = transformer.transform(entities)

		assertEquals(expectedMap, result)
	}
}