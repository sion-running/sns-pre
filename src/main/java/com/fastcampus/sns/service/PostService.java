package com.fastcampus.sns.service;

import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.model.AlarmArgs;
import com.fastcampus.sns.model.AlarmType;
import com.fastcampus.sns.model.Comment;
import com.fastcampus.sns.model.Post;
import com.fastcampus.sns.model.entity.*;
import com.fastcampus.sns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity userEntity = getUserEntityOrException(userName);
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        UserEntity userEntity = getUserEntityOrException(userName);

        // 포스트 존재여부
        PostEntity postEntity = getPostEntityOrException(postId);

        // 작성자인지 확인
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with post %s", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    public void delete(String userName, Integer postId) {
        UserEntity userEntity = getUserEntityOrException(userName);

        // 포스트 존재여부
        PostEntity postEntity = getPostEntityOrException(postId);

        // 작성자인지 확인
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with post %s", userName, postId));
        }

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrException(userName);

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {
        // 포스트와 유저 존재여부 확인
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        // 같은 게시물에 중복으로 누르는 건 아닌 지 체크
         likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
             throw new SnsApplicationException(ErrorCode.ALREDAY_LIKED, String.format("userName %s already liked post %s", userName, postId));
         });

         likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));

         // 알람
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public int likeCount(Integer postId) {
        // 포스트 존재여부
        PostEntity postEntity = getPostEntityOrException(postId);

        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment) {
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        // comment save
        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));

        // alarm save
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity postEntity = getPostEntityOrException(postId);
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    private PostEntity getPostEntityOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not found", postId)));
    }

    private UserEntity getUserEntityOrException(String userName) {
        return userEntityRepository.findByUserName(userName).orElseThrow(()
                -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not found", userName)));
    }
}
