package app.service;

import app.dao.CommentDao;
import app.dto.request.comment.CommentCreateRequest;
import app.dto.request.comment.CommentUpdateRequest;
import app.entities.Comment;
import app.entities.Post;
import app.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private CommentDao commentDao;
    private UserService userService;
    private PostService postService;

    public CommentService(CommentDao commentDao, UserService userService, PostService postService) {
        this.commentDao = commentDao;
        this.userService = userService;
        this.postService = postService;
    }

    public List<Comment> getAllCommentsWithParam(Optional<Long> userId, Optional<Long> postId) {
        if(userId.isPresent() && postId.isPresent()){
            return commentDao.findByUserIdAndPostId(userId.get(),postId.get());
        }else if(userId.isPresent()){
            return commentDao.findByUserId(userId.get());
        }else if(postId.isPresent()){
            return commentDao.findByPostId(postId.get());
        }else {
            return commentDao.findAll();
        }
    }

    public Comment getOneCommentById(Long commentId) {
        return commentDao.findById(commentId).orElse(null);
    }

    public Comment createOneComment(CommentCreateRequest request) {
        User user = userService.getOneUserById(request.getUserId());
        Post post = postService.getOnePostById(request.getPostId());
        if(user != null && post != null){

            Comment commentToSave = new Comment();
            commentToSave.setId(request.getId());
            commentToSave.setPost(post);
            commentToSave.setUser(user);
            commentToSave.setText(request.getText());
            return commentDao.save(commentToSave);
        }else {
            return null;
        }
    }

    public Comment updateOneCommentById(Long commentId, CommentUpdateRequest request) {
        Optional<Comment> comment = commentDao.findById(commentId);
        if(comment.isPresent()){
            Comment commentToUpdate = comment.get();
            commentToUpdate.setText(request.getText());
           return commentDao.save(commentToUpdate);
        }else {
            return null;
        }
    }

    public void deleteOneCommentById(Long commentId) {
        commentDao.deleteById(commentId);
    }
}
