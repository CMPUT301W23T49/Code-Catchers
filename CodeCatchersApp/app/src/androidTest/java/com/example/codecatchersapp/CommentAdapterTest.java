package com.example.codecatchersapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentAdapterTest {

    private CommentAdapter commentAdapter;

    @Mock
    private List<Comment> mockCommentList;

    @Mock
    private Comment mockComment;

    @Before
    public void setUp() {
        commentAdapter = new CommentAdapter(mockCommentList);
    }

    @Test
    public void getItemCount() {
        when(mockCommentList.size()).thenReturn(5);
        assertEquals(5, commentAdapter.getItemCount());
    }
}
