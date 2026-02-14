package com.training.some_social_network.controller;

import com.training.some_social_network.dto.PostDto;
import com.training.some_social_network.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/post", "/api/v2/post"})
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * Creating a user post
     *
     * @param postText post text
     * @return post data
     */
    @PostMapping("/create")
    public PostDto createPost(@RequestBody String postText) {
        return postService.create(postText);
    }

    /**
     * Updating a user's post
     *
     * @param postDto post data
     */
    @PutMapping("/update")
    public void updatePost(@RequestBody PostDto postDto) {
        postService.update(postDto);
    }

    /**
     * Deleting a user's post
     *
     * @param postId post id
     */
    @PutMapping("/delete/{id}")
    public void deletePost(@PathVariable("id") String postId) {
        postService.delete(postId);
    }

    /**
     * Getting a user's post
     *
     * @param postId post id
     * @return user data
     */
    @GetMapping("/get/{postId}")
    public PostDto getPost(@PathVariable("postId") String postId) {
        return postService.get(postId);
    }

    /**
     * Getting a user's feed
     *
     * @param offset    initial post
     * @param limit     number of posts
     * @return  feed of posts
     */
    @GetMapping("/feed")
    public List<PostDto> getFeed(@RequestParam Integer offset, @RequestParam Integer limit) {
        return postService.getCurrentUserFeed(offset, limit);
    }
}
