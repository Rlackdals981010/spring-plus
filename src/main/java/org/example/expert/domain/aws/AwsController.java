package org.example.expert.domain.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class AwsController {
    private final BoardService boardService;
    private final FileService fileService;
    private final S3Uploader s3Uploader;

    /**
     * 생성
     */
    @PostMapping("")
    public BaseResponse<PostBoardRes> create(
            @RequestPart(value = "postBoardReq") PostBoardReq postBoardReq,
            @RequestPart(value = "boardImg", required = false) MultipartFile multipartFile){

        String fileName = "";
        if(multipartFile != null){ // 파일 업로드한 경우에만

            try{// 파일 업로드
                fileName = s3Uploader.upload(multipartFile, "images"); // S3 버킷의 images 디렉토리 안에 저장됨
                System.out.println("fileName = " + fileName);
            }catch (IOException e){
                return new BaseResponse<>(FAIL_FILE_CHANGE);
            }
        }
}
