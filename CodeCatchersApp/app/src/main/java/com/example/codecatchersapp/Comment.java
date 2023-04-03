/**
 * a class
 * @author CMPUT301W23T49
 * @version 1.0
 * @since [Monday April 3]
 */
package com.example.codecatchersapp;
/**
 The Comment class represents a user's comment on a monster.
 May use the User class instead of userName string in the future
 */
public class Comment {
    String userName;
    String commentText;

    /**
     Constructs a new Comment object with the specified user name and comment text.
     @param userName the name of the user who made the comment
     @param commentText the text of the comment
     */

    public Comment(String userName, String commentText) {
        this.userName = userName;
        this.commentText = commentText;
    }

    /**
     Returns the username of the user who made the comment.
     @return the username of the user who made the comment
     */
    public String getUserName() {
        return userName;
    }

    /**
     Returns the text of the comment.
     @return the text of the comment
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     Sets the text of the comment.
     @param commentText the new text of the comment
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}

