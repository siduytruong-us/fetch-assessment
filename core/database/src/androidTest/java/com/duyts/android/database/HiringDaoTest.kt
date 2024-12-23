package com.duyts.android.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duyts.android.database.dao.HiringDao
import com.duyts.android.database.entity.HiringItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HiringDaoTest {
	private lateinit var hiringDao: HiringDao
	private lateinit var db: AppDatabase

	@Before
	fun createDb() {
		val context = InstrumentationRegistry.getInstrumentation().targetContext
		db = Room.inMemoryDatabaseBuilder(
			context,
			AppDatabase::class.java,
		).allowMainThreadQueries().build()
		hiringDao = db.hiringDao()
	}

	@After
	fun closeDb() = db.close()

	@Test
	fun testInsertOrIgnoreHiringItems() = runTest {
		val items = listOf(
			HiringItemEntity(id = 1, name = "Alice", listId = 101),
			HiringItemEntity(id = 2, name = "Bob", listId = 102)
		)

		hiringDao.insertOrIgnoreHiringItems(items)
		hiringDao.insertOrIgnoreHiringItems(listOf(HiringItemEntity(id = 1, name = "Alice", listId = 101))) // Duplicate

		val allItems = hiringDao.observeHiringItems().first()
		assertEquals(2, allItems.size) // Ensure duplicates are ignored
		assertEquals("Alice", allItems[0].name)
		assertEquals("Bob", allItems[1].name)
	}

	@Test
	fun testObserveHiringItems() = runTest {
		val items = listOf(
			HiringItemEntity(id = 1, name = "Alice", listId = 101),
			HiringItemEntity(id = 2, name = "Bob", listId = 102)
		)
		hiringDao.insertOrIgnoreHiringItems(items)

		val observedItems = hiringDao.observeHiringItems().first()

		assertEquals(2, observedItems.size)
		assertEquals("Alice", observedItems[0].name)
		assertEquals("Bob", observedItems[1].name)
	}

	@Test
	fun testInsertEmptyList() = runTest {
		hiringDao.insertOrIgnoreHiringItems(emptyList())

		val allItems = hiringDao.observeHiringItems().first()
		assertTrue(allItems.isEmpty())
	}

}