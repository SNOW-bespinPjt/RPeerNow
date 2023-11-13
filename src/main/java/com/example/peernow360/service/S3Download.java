package com.example.peernow360.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
@Component
public class S3Download {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Object getObject(String storedFileName) throws IOException {
//        try (S3Object o = amazonS3Client.getObject(bucket, key);
//             S3ObjectInputStream file = o.getObjectContent();
//             FileOutputStream fos = new FileOutputStream(new File(key))) {
//
//            byte[] read_buf = new byte[1024];
//            int read_len = 0;
//
//            while ((read_len = file.read(read_buf)) > 0) {
//                fos.write(read_buf, 0, read_len);
//            }
//
//        } catch (IOException e) {
//            log.error("Failed to download file from S3", e);
//            throw e; // or handle the exception appropriately
//        }

        S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, storedFileName));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        httpHeaders.setContentLength(bytes.length);
//        httpHeaders.setContentDispositionFormData("attachment", fileName);

        Map map= new HashMap();
        map.put("image",bytes);

        return map;
    }

}
