package com.awsserver.awsserverapi;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.awsserver.awsserverapi.repository.PostRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.awsserver.awsserverapi.model.Post;



@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = {"http://3.107.23.106","http://localhost:5173"})
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll(Sort.by("createdAt").descending());
        return posts;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        // PostRepository의 findById() 메서드를 사용해서 ID로 게시글을 찾음
        // findById()는 Optional<Post>를 반환 (게시글이 없을 수도 있으므로)
        Optional<Post> post = postRepository.findById(id);

        // 게시글이 존재하면 200 OK 응답과 함께 게시글 데이터를 반환
        if (post.isPresent()) {
            return new ResponseEntity<>(post.get(), HttpStatus.OK);
        } else {
            // 게시글이 존재하지 않으면 404 Not Found 응답을 반환
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //게시글 작성 API (새로 추가할 코드)
    @PostMapping // <-- "/api/posts" 경로로 POST 요청이 오면 이 메서드를 실행
    public ResponseEntity<Post> createPost(@RequestBody Post post) { // <-- @RequestBody로 요청 본문의 JSON 데이터를 Post 객체로 받음
        // 데이터베이스에 저장 (save() 메서드는 저장 후 저장된 엔티티를 반환)
        // created_at은 DB에서 자동으로 채워지므로 별도 설정 불필요
        Post savedPost = postRepository.save(post);
        // 201 Created 상태 코드와 함께 저장된 게시글 데이터를 반환
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }
    
    
}
