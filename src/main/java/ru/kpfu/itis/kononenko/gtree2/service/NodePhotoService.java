package ru.kpfu.itis.kononenko.gtree2.service;

import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.kononenko.gtree2.entity.NodePhoto;

import java.util.List;

public interface NodePhotoService {
    List<NodePhoto> getPhotos(Long nodeId);
    NodePhoto add(Long nodeId, MultipartFile file);
    NodePhoto updateDescription(Long id, String description);
    void delete(Long id);
    void deleteAllByNodeId(Long nodeId);
}