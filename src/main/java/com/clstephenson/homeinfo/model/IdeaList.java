package com.clstephenson.homeinfo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IdeaList implements Serializable {

    private List<Idea> ideas;

    public IdeaList() {
        ideas = new ArrayList<>();
    }

    public IdeaList(List<Idea> ideas) {
        this.ideas = ideas;
    }

    public List<Idea> getIdeas() {
        return ideas;
    }

    public void setIdeas(List<Idea> ideas) {
        this.ideas = ideas;
    }
}
