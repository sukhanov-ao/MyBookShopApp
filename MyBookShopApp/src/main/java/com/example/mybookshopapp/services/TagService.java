package com.example.mybookshopapp.services;

import com.example.mybookshopapp.annotations.MethodExecutionTimeLoggable;
import com.example.mybookshopapp.data.tag.Tag;
import com.example.mybookshopapp.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @MethodExecutionTimeLoggable
    public List<Tag> getTagData() {
        return tagRepository.findAll();
    }

    @MethodExecutionTimeLoggable
    public Tag getTag(Integer tagId) {
        return tagRepository.getOne(tagId);
    }

    @MethodExecutionTimeLoggable
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
