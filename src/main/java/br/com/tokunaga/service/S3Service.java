package br.com.tokunaga.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    public List<String> get(String bucketName) {
        return amazonS3.listObjectsV2(bucketName).getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    public ByteArrayResource get(String bucketName, String fileName) throws IOException {
        S3Object data = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream objectContent = data.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectContent);
        ByteArrayResource resource = new ByteArrayResource(bytes);
        objectContent.close();
        return resource;
    }

    public void put(String bucketName, MultipartFile file) throws IOException {
        String tempFileName = UUID.randomUUID() + file.getName();
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + tempFileName);
        file.transferTo(tempFile);
        amazonS3.putObject(bucketName, UUID.randomUUID() + file.getName(), tempFile);
        tempFile.deleteOnExit();

    }

    public void delete(String bucketName, String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }
}
