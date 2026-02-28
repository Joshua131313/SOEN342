package ca.soen342.taskmanager.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ca.soen342.taskmanager.domain.Tag;

public class TagsService {
    private Map<String, Tag> tags = new HashMap<>();
    public Tag getOrCreateTag(String name) {
        String normalized = name.trim().toLowerCase();
        if(tags.containsKey(normalized)) {
            return tags.get(normalized);
        }
        Tag newTag = new Tag(normalized);
        tags.put(normalized, newTag);
        return newTag;
    }
    public Collection<Tag> getAllTags() {
        return tags.values();
    }
}
