package com.awsserver.awsserverapi;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.awsserver.awsserverapi.repository.PostRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.awsserver.awsserverapi.model.Post;



@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = {"http://3.107.23.106","http://localhost:5173"})
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping(produces = "application/json;charset=UTF-8")
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll(Sort.by("createdAt").descending());
        return posts;
    }

    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
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
    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<Post> createPost(@RequestBody Post post) { // <-- @RequestBody로 요청 본문의 JSON 데이터를 Post 객체로 받음
        // 데이터베이스에 저장 (save() 메서드는 저장 후 저장된 엔티티를 반환)
        // created_at은 DB에서 자동으로 채워지므로 별도 설정 불필요
        Post savedPost = postRepository.save(post);
        // 201 Created 상태 코드와 함께 저장된 게시글 데이터를 반환
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}") // <-- {id} 부분은 URL 경로에서 ID 값을 받겠다는 의미
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        // 1. 주어진 ID로 게시글을 데이터베이스에서 찾음
        Optional<Post> existingPostOptional = postRepository.findById(id);

        // 2. 게시글이 존재하면 업데이트 진행
        if (existingPostOptional.isPresent()) {
            Post existingPost = existingPostOptional.get();

            // 3. 요청 본문에서 받은 데이터로 기존 게시글의 필드를 업데이트
            // (ID, createdAt은 변경하지 않음)
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
            existingPost.setAuthor(updatedPost.getAuthor()); // 작성자도 변경 가능하게
            // updated_at은 @UpdateTimestamp가 자동으로 처리

            // 4. 업데이트된 게시글을 데이터베이스에 저장 (save()는 ID가 있으면 업데이트, 없으면 삽입)
            Post savedPost = postRepository.save(existingPost);

            // 5. 200 OK 응답과 함께 업데이트된 게시글 데이터를 반환
            return new ResponseEntity<>(savedPost, HttpStatus.OK);
        } else {
            // 6. 게시글이 존재하지 않으면 404 Not Found 응답을 반환
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 게시글 삭제 API (새로 추가할 코드)
    @DeleteMapping("/{id}") // <-- {id} 부분은 URL 경로에서 ID 값을 받겠다는 의미
    public ResponseEntity<Void> deletePost(@PathVariable Long id) { // <-- @PathVariable로 ID 값을 받음
        // 1. 주어진 ID로 게시글이 존재하는지 확인 (선택 사항이지만 안전을 위해 권장)
        if (postRepository.existsById(id)) { // existsById()는 해당 ID의 엔티티가 존재하는지 boolean 반환
            // 2. 게시글이 존재하면 삭제
            postRepository.deleteById(id);
            // 3. 204 No Content 응답 반환 (삭제 성공 시 본문 없이 응답)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // 4. 게시글이 존재하지 않으면 404 Not Found 응답 반환
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
}
