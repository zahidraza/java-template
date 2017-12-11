package com.jazasoft.mtdb.storage;

import com.jazasoft.mtdb.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * Created by mdzahidraza on 29/11/17.
 */
public class AbstractStorageService implements StorageService {
    private final Logger logger = LoggerFactory.getLogger(AbstractStorageService.class);

    private final Path rootPath;
    private Path location;

    public AbstractStorageService() {
        String root = Utils.getAppHome() + File.separator + "content";
        this.rootPath = Paths.get(root);
    }

    @Override
    public void init(String path) {
        this.location = rootPath.resolve(path);
        try {
            Files.createDirectories(location);
        } catch (IOException e) {
            logger.error("Unable to create directory - {}. error = {}", location.toString(), e.getMessage());
        }
    }

    @Override
    public Path getLocation() {
        return this.location;
    }

    @Override
    public void store(MultipartFile file, CopyOption... copyOptions) {
        String filename = file.getOriginalFilename();
        store(file, filename, copyOptions);
    }

    @Override
    public void store(MultipartFile file, String filename, CopyOption... copyOptions) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to Store empty file.");
        }
        try {
            InputStream inputStream = file.getInputStream();
            store(inputStream, filename, copyOptions);
        } catch (IOException e) {
            throw new StorageException("Unable to read file. error = " + e.getMessage());
        }
    }

    @Override
    public void store(InputStream inputStream, String filename, CopyOption... copyOptions) {
        Path path = location.resolve(filename);
        try {
            Files.copy(inputStream, path, copyOptions);
        } catch (FileAlreadyExistsException e) {
            logger.error("File Already Exists. file = {}", filename);
        } catch (IOException e) {
            logger.error("Failed to store file - {}, error = {}", filename, e.getMessage());
        }
    }

    @Override
    public Path load(String filename) {
        return location.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        Path path = load(filename);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file = " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageException("invalid filename = " + filename + ", error = " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(location, 0);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StorageException("Unable to laod files. error = " + e.getMessage());
        }
    }

    @Override
    public void delete(String filename) {
        Path path = location.resolve(filename);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new StorageException("Unable to delete File - " + filename);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(location.toFile());
    }
}
