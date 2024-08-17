package app.service;

import app.dao.LikeDao;
import app.dao.PostDao;
import app.dto.request.post.PostCreateRequest;
import app.dto.request.post.PostUpdateRequest;
import app.dto.response.like.LikeResponse;
import app.dto.response.post.PostResponse;
import app.entities.Like;
import app.entities.Post;
import app.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostDao postDao;
    @Autowired
    @Lazy
    private LikeService likeService;
    @Autowired
    private UserService userService;


    public PostService(PostDao postDao,UserService userService) {
        this.postDao = postDao;
        this.userService = userService;
    }

    public void setLikeService(LikeService likeService){
        this.likeService = likeService;
    }

    // birbirini çağırıp infinite loopa girmesin diye böyle bir setter yaptık

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;

        if(userId.isPresent()){
            list = postDao.findByUserId(userId.get());
        }else{
        list = postDao.findAll();
        }
        return list.stream().map( p-> {
            List <LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null),Optional.of(p.getId()));
           return new PostResponse(p,likes);
        }).collect(Collectors.toList());
    }

    public Post getOnePostById(Long postId) {
        return postDao.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
     User user =  userService.getOneUserById(newPostRequest.getUserId());
     if(user == null) return null;

     Post toSave = new Post();
     toSave.setId(newPostRequest.getId());
     toSave.setText(newPostRequest.getText());
     toSave.setTitle(newPostRequest.getTitle());
     toSave.setUser(user);

        return postDao.save(toSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) {
        Optional<Post> post = postDao.findById(postId);
        if(post.isPresent()){
            Post toUpdate = post.get();
            toUpdate.setText(updatePost.getText());
            toUpdate.setTitle(updatePost.getTitle());
             postDao.save(toUpdate);
             return toUpdate;
        }
        return null;
    }

    public void deleteOnePostById(Long postId) {
        postDao.deleteById(postId);
    }
}
