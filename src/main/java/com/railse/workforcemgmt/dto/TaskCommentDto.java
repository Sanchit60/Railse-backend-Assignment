package com.railse.workforcemgmt.dto;

public class TaskCommentDto {
    private String author;
    private String commentText;

    public TaskCommentDto() {}

    public TaskCommentDto(String author, String commentText) {
        this.author = author;
        this.commentText = commentText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
