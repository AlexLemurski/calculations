package ru.project.calculations.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class FilePathResource {

    @Value("${myapp.file-storage.directory}")
    private String fileResource;

}