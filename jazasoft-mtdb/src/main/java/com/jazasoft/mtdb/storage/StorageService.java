package com.jazasoft.mtdb.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Created by mdzahidraza on 29/11/17.
 */
public interface StorageService {

    void init(String location);

    Path getLocation();

    void store(MultipartFile file, CopyOption... copyOptions);

    void store(MultipartFile file, String filename, CopyOption... copyOptions);

    void store(InputStream inputStream, String filename, CopyOption... copyOptions);

    Path load(String filename);

    Resource loadAsResource(String filename);

    Stream<Path> loadAll();

    void delete(String filename);

    void deleteAll();

}
