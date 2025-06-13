package ru.kpfu.itis.kononenko.gtree2.service.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.kononenko.gtree2.config.property.MinioProperties;
import ru.kpfu.itis.kononenko.gtree2.entity.NodePhoto;
import ru.kpfu.itis.kononenko.gtree2.exception.NotFoundException;
import ru.kpfu.itis.kononenko.gtree2.repository.NodePhotoRepository;
import ru.kpfu.itis.kononenko.gtree2.repository.NodeRepository;
import ru.kpfu.itis.kononenko.gtree2.service.NodePhotoService;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NodePhotoServiceImpl implements NodePhotoService {

    private final NodePhotoRepository photoRepository;
    private final NodeRepository nodeRepository;
    private final MinioClient minioClient;
    private final MinioProperties properties;

    @Override
    public List<NodePhoto> getPhotos(Long nodeId) {
        return photoRepository.findByNodeId(nodeId);
    }

    @Override
    public NodePhoto add(Long nodeId, MultipartFile file) {
        nodeRepository.findById(nodeId)
                .orElseThrow(() -> new NotFoundException("Node not found id=" + nodeId));
        try (InputStream is = file.getInputStream()) {
            String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .stream(is, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            String url = properties.getPublicBaseUrl() + "/" + objectName;
            NodePhoto photo = NodePhoto.builder()
                    .nodeId(nodeId)
                    .photoUrl(url)
                    .description("Новое фото")
                    .build();
            return photoRepository.save(photo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NodePhoto updateDescription(Long id, String description) {
        NodePhoto photo = photoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Photo not found id=" + id));
        photo.setDescription(description);
        return photoRepository.save(photo);
    }

    @Override
    public void delete(Long id) {
        NodePhoto photo = photoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Photo not found id=" + id));
        String objectName = photo.getPhotoUrl().replaceFirst(properties.getPublicBaseUrl() + "/", "");
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .build());
        } catch (Exception ignored) {}
        photoRepository.delete(photo);
    }
}