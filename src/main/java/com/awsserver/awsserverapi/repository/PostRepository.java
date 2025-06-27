package com.awsserver.awsserverapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.awsserver.awsserverapi.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
}
