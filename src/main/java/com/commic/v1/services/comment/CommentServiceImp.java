package com.commic.v1.services.comment;

import com.commic.v1.entities.Comment;
import com.commic.v1.repositories.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImp implements ICommentServices {
    @Autowired
    ICommentRepository commentRepository;


    @Override
    public void deleteByChapterId(Integer id) {
        List<Comment> comments = commentRepository.findByChapterId(id);
        for (Comment comment : comments) {
            comment.setIsDeleted(true);
        }
        commentRepository.saveAllAndFlush(comments);
    }
    
}
