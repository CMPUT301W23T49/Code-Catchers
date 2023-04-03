package com.example.codecatchersapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * The CommentTest class contains JUnit tests for the Comment class.
 */
public class CommentTest {
    /**
     * Tests the getUserName method of the Comment class.
     * Creates a Comment object with a user name of "Kyle" and comment text of "This is a test comment."
     * Checks that getUserName returns "Kyle".
     */
    @Test
    public void testGetUserName() {
        Comment comment = new Comment("Kyle", "This is a test comment.");
        assertEquals("Kyle", comment.getUserName());
    }

    /**
     * Tests the getCommentText method of the Comment class.
     * Creates a Comment object with a user name of "Alice" and comment text of "This is another test comment."
     * Checks that getCommentText returns "This is another test comment.".
     */
    @Test
    public void testGetCommentText() {
        Comment comment = new Comment("Alice", "This is another test comment.");
        assertEquals("This is another test comment.", comment.getCommentText());
    }

    /**
     * Tests the setCommentText method of the Comment class.
     * Creates a Comment object with a user name of "Charlie" and comment text of "This is yet another test comment."
     * Sets the comment text to "This is an updated test comment." using setCommentText
     * Checks that getCommentText returns the updated text.
     */
    @Test
    public void testSetCommentText() {
        Comment comment = new Comment("Charlie", "This is yet another test comment.");
        comment.setCommentText("This is an updated test comment.");
        assertEquals("This is an updated test comment.", comment.getCommentText());
    }
}
