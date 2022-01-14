package com.example.houserentproject;

import java.io.Serializable;

public class ReviewModel implements Serializable {

    String name;
    String review;

    public ReviewModel() {
    }

    public ReviewModel(String name, String review) {
        this.name = name;
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
