package com.article.Controller;

import com.article.DTO.BlogRequestDTO;
import com.article.DTO.BlogResponseDTO;
import com.article.DTO.DetailedBlogResponseDTO;
import com.article.DTO.ErrorResponse;
import com.article.Service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Operation(summary = "Get all blogs")
    @ApiResponse(
            responseCode = "200",
            description = "Blogs retrieved successfully"
    )
    @GetMapping
    public ResponseEntity<List<BlogResponseDTO>> getAllBlogs(){
        return ResponseEntity.ok(blogService.getAllBlogs());
    }


    @Operation(summary = "Get a blog by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Blog found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("id/{id}")
    public ResponseEntity<DetailedBlogResponseDTO> getBlogById(@PathVariable String id){
        return ResponseEntity.ok(blogService.getBlogById(id));
    }


    @Operation(summary = "Get current user's blogs")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Blogs retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("my")
    public ResponseEntity<List<BlogResponseDTO>> getMyBlog(){
        return ResponseEntity.ok(blogService.getMyBlog());
    }


    @Operation(summary = "Get blogs by username")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Blogs retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("username/{username}")
    public ResponseEntity<List<BlogResponseDTO>> getBlogByUsername(
            @PathVariable String username){

        return ResponseEntity.ok(blogService.getBlogByUsername(username));
    }


    @Operation(summary = "Create a new blog")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Blog created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid image",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createBlog(
            @ModelAttribute BlogRequestDTO blogRequestDTO){

        return ResponseEntity.created(null)
                .body(blogService.createBlog(blogRequestDTO));
    }


    @Operation(summary = "Update a blog")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Blog updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid image",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateBlog(
            @PathVariable String id,
            @ModelAttribute BlogRequestDTO blogRequestDTO){

        return ResponseEntity.ok()
                .body(blogService.updateBlog(id, blogRequestDTO));
    }


    @Operation(summary = "Update blog title")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Title updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("title/{id}")
    public ResponseEntity<Void> updateTitle(
            @PathVariable String id,
            @RequestBody String title){

        return ResponseEntity.ok()
                .body(blogService.updateTitle(id, title));
    }


    @Operation(summary = "Update blog image")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Image updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid image",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping(
            value = "image/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> updateImage(
            @PathVariable String id,
            @RequestParam("image") MultipartFile image){

        return ResponseEntity.ok(blogService.updateImage(id, image));
    }


    @Operation(summary = "Update blog content")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Content updated successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("content/{id}")
    public ResponseEntity<Void> updateContent(
            @PathVariable String id,
            @RequestBody String content){

        return ResponseEntity.ok()
                .body(blogService.updateContent(id, content));
    }


    @Operation(summary = "Delete a blog")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Blog deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Blog not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable String id){
        return ResponseEntity.ok(blogService.deleteBlog(id));
    }
}