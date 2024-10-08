package org.example.expert.domain.aws;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/s3")
public class S3Controller {
    private final S3Service s3Uploader;

    public S3Controller(S3Service s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    // 파일 업로드
    @PostMapping("/image/upload")
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile file) {
        try {
            String imageUrl = s3Uploader.imageUpload(file);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 파일 다운로드
    @PostMapping("/image/download/{filename}")
    public ResponseEntity<UrlResource> downloadImage(@PathVariable String filename) {
        ResponseEntity<UrlResource> response = s3Uploader.imageDownload(filename);
        return response;
    }


    // 파일 삭제
    @PostMapping("/image/delete")
    public ResponseEntity<String> deleteImage(@RequestParam("url") String fileUrl) {
        try {
            s3Uploader.imageDelete(fileUrl);
            return ResponseEntity.ok("이미지 삭제 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}