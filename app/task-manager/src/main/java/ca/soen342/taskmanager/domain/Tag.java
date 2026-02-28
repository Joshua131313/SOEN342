package ca.soen342.taskmanager.domain;

public class Tag {
    private String keyword;

    public Tag(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
}
