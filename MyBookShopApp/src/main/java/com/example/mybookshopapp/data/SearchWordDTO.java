package com.example.mybookshopapp.data;

public class SearchWordDTO {
    private String example;

    public SearchWordDTO(String example) {
        this.example = example;
    }

    public SearchWordDTO() {
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
