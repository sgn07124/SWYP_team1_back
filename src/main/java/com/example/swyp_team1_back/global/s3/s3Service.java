package com.example.swyp_team1_back.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class s3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3Client amazonS3Client;



    /**
     * S3에 이미지를 업로드
     * 이미지 저장 안하는 경우도 처리 필요, 확장자 다른 경우 처리 필요 !!!
     * @param file 업로드할 이미지 파일
     * @return S3에 저장된 이미지의 URL
     * @throws IOException 파일 업로드 중 발생한 예외
     */

    @Transactional
    public String uploadFile(MultipartFile file, String filePrePath) throws IOException {
        // 저장될 파일의 경로 설정
        String storedFileName = filePrePath + generateFileName(file);

        try {
            // S3에 파일 업로드
            amazonS3Client.putObject(bucket, storedFileName, file.getInputStream(), getObjectMetadata(file));

            // 업로드된 이미지의 URL 반환
            return amazonS3Client.getUrl(bucket, storedFileName).toString();

        } catch (SdkClientException e) {
            throw new IOException("Error uploading file to S3", e);
        }
    }

    /**
     * s3에 저장된 이미지 객체 삭제하는 메소드
     * 이미지가 저장되어 있지 않는 경우 처리 필요!!
     * @param fileUrl
     * @throws IOException
     */
    @Transactional
    public void fileDelete(String fileUrl) throws IOException {
        try {
            amazonS3Client.deleteObject(bucket, getFileKeyFromUrl(fileUrl));
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
            System.exit(1);
        }
        log.info(String.format("[%s] deletion complete", fileUrl));
    }

    /**
     * 업로드된 파일의 메타데이터를 생성
     *
     * @param file 업로드된 파일
     * @return 파일의 메타데이터
     */
    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentDisposition(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    /**
     * 고유한 파일명을 생성
     *
     * @param file 업로드된 파일
     * @return 고유한 파일명
     */
    private String generateFileName(MultipartFile file) {
        log.info("file = " + file.getOriginalFilename());
        return UUID.randomUUID() + "-" + file.getOriginalFilename();
    }

    /**
     * DB에 저장된 객체 URL에서 키 값을 추출하는 메소드
     * 추가적으로 저장된 이미지의 URL은 DB와 S3 객체 URL에 인코딩 되어 저장되는데,
     * 키 값은 인코딩 되지 않고 저장된다, 따라서 키 값을 조회하려면 디코딩 진행 후 조회해야 함.
     * @param fileUrl
     * @return
     * @throws UnsupportedEncodingException
     */

    public String getFileKeyFromUrl(String fileUrl) throws UnsupportedEncodingException {
        // S3 버킷 이름과 리전을 이용해 동적으로 URL 생성
        String s3Url = "https://" + bucket + ".s3." + region + ".amazonaws.com/";

        // 생성된 URL을 제거해 파일 키 추출
        String fileKey = fileUrl.replace(s3Url, "");
        fileKey = URLDecoder.decode(fileKey, StandardCharsets.UTF_8);

        return fileKey;
    }

}
