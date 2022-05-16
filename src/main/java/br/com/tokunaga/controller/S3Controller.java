package br.com.tokunaga.controller;

import br.com.tokunaga.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service service;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @GetMapping
    public ResponseEntity get() {
        List<String> files = service.get(bucketName);
        if (!isNull(files)) {
            return ResponseEntity.ok(files);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{file}")
    public ResponseEntity get(@PathVariable String file) throws IOException {
        ByteArrayResource ba = service.get(bucketName, file);
        if (!isNull(ba)) {
            return ResponseEntity
                    .ok()
                    .contentLength(ba.getByteArray().length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                    .body(ba);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{file}")
    public ResponseEntity delete(@PathVariable String file) {
        service.delete(bucketName, file);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity delete(@RequestParam(value = "file") MultipartFile file) throws IOException {
        service.put(bucketName, file);
        return ResponseEntity.ok().build();
    }
}
