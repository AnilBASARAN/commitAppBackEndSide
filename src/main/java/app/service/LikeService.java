package app.service;



import app.dao.LikeDao;
import app.dto.request.comment.CommentCreateRequest;
import app.dto.request.post.LikeUpdateRequest;
import app.dto.response.like.LikeResponse;
import app.entities.Like;
import app.entities.Post;
import app.entities.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    private LikeDao likeDao;
    private UserService userService;
    private PostService postService;

    public LikeService(LikeDao likeDao, UserService userService, PostService postService) {
        this.likeDao = likeDao;
        this.userService = userService;
        this.postService = postService;
    }

    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        List<Like> list;
        if(userId.isPresent() && postId.isPresent()){
            list = likeDao.findByUserIdAndPostId(userId.get(),postId.get());
        }else if(userId.isPresent()){
            list = likeDao.findByUserId(userId.get());
        }else if(postId.isPresent()){
            list = likeDao.findByPostId(postId.get());
        }else {
            list = likeDao.findAll();
        }
   return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
    }

    public Like getOneLikeById(Long likeId) {
        return likeDao.findById(likeId).orElse(null);
    }

    public Like createOneLike(CommentCreateRequest request) {
        User user = userService.getOneUserById(request.getUserId());
        Post post = postService.getOnePostById(request.getPostId());
        if(user != null && post != null){

            Like likeToSave = new Like();
            likeToSave.setId(request.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);

            return likeDao.save(likeToSave);
        }else {
            return null;
        }
    }

    public Like updateOneLikeById(Long postId, LikeUpdateRequest request) {
        Optional<Like> like = likeDao.findById(postId);
        if(like.isPresent()){
            Like likeToUpdate = like.get();

            return likeDao.save(likeToUpdate);
        }else {
            return null;
        }
    }

    public void deleteOneLikeById(Long postId) {
        likeDao.deleteById(postId);
    }
}

