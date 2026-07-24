package com.blackcatz.android.hnews.data.mapper

import com.blackcatz.android.hnews.data.itemResponse
import com.blackcatz.android.hnews.data.storyEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class StoryMapperTest {

    @Test
    fun `toStoryEntity maps all fields when fully populated`() {
        val response = itemResponse(
            id = 42,
            title = "Some title",
            by = "carmack",
            score = 100,
            kids = listOf(1, 2, 3),
            url = "https://example.com",
        )

        val entity = response.toStoryEntity(position = 5)

        assertEquals(42L, entity.id)
        assertEquals(5, entity.position)
        assertEquals("Some title", entity.title)
        assertEquals("carmack", entity.author)
        assertEquals(100, entity.score)
        assertEquals(3, entity.totalComment)
        assertEquals("https://example.com", entity.url)
    }

    @Test
    fun `toStoryEntity maps null title to empty string`() {
        val entity = itemResponse(title = null).toStoryEntity(position = 0)
        assertEquals("", entity.title)
    }

    @Test
    fun `toStoryEntity maps null score to zero`() {
        val entity = itemResponse(score = null).toStoryEntity(position = 0)
        assertEquals(0, entity.score)
    }

    @Test
    fun `toStoryEntity maps null kids to zero comments`() {
        val entity = itemResponse(kids = null).toStoryEntity(position = 0)
        assertEquals(0, entity.totalComment)
    }

    @Test
    fun `toStoryEntity counts kids rather than storing them`() {
        val entity = itemResponse(kids = listOf(1, 2, 3)).toStoryEntity(position = 0)
        assertEquals(3, entity.totalComment)
    }

    @Test
    fun `toStoryEntity maps null url to empty string`() {
        val entity = itemResponse(url = null).toStoryEntity(position = 0)
        assertEquals("", entity.url)
    }

    @Test
    fun `toStory maps well-formed url to its host`() {
        val story = storyEntity(url = "https://example.com/path").toStory()
        assertEquals("example.com", story.domain)
    }

    @Test
    fun `toStory keeps www prefix verbatim`() {
        val story = storyEntity(url = "https://www.example.com/path").toStory()
        assertEquals("www.example.com", story.domain)
    }

    @Test
    fun `toStory excludes port from domain`() {
        val story = storyEntity(url = "https://example.com:8080/path").toStory()
        assertEquals("example.com", story.domain)
    }

    @Test
    fun `toStory maps blank url to empty domain`() {
        val story = storyEntity(url = "").toStory()
        assertEquals("", story.domain)
    }

    @Test
    fun `toStory maps whitespace-only url to empty domain`() {
        val story = storyEntity(url = "   ").toStory()
        assertEquals("", story.domain)
    }

    @Test
    fun `toStory maps malformed url to empty domain without throwing`() {
        val story = storyEntity(url = "not a url").toStory()
        assertEquals("", story.domain)
    }

    @Test
    fun `toStory passes through other fields unchanged`() {
        val entity = storyEntity(
            id = 7L,
            title = "Title",
            author = "author",
            score = 50,
            totalComment = 12,
        )

        val story = entity.toStory()

        assertEquals(7L, story.id)
        assertEquals("Title", story.title)
        assertEquals("author", story.author)
        assertEquals(50, story.noOfVotes)
        assertEquals(12, story.totalComment)
    }
}
