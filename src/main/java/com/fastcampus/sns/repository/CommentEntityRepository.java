package com.fastcampus.sns.repository;

import com.fastcampus.sns.model.entity.CommentEntity;
import com.fastcampus.sns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    // jpa에서 기본제공하는 findAllById는, id pk 인덱스가 걸리지만, 이렇게 수동으로 만들어준 메소드에는 인덱스가 걸리지 않음. 그래서, CommentEntity에 post_id 인덱스 추가했음
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE CommentEntity entity SET deleted_at = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);

}
