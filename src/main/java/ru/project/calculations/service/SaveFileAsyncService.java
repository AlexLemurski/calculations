package ru.project.calculations.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.enums.DocumentIndex;
import ru.project.calculations.repository.DocumentResourceRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static ru.project.calculations.util.ExcelFileReaderUtil.fileOutputStream;

@Service
public class SaveFileAsyncService {

	private static String generateKey(String name) {
		return DigestUtils.md5Hex(name + LocalDateTime.now());
	}

	@Async
	public void saveAllDataDocumentResource(long id,
												   String folderName,
												   DocumentResourceRepository documentResourceRepository,
												   DocumentIndex documentIndex,
												   MultipartFile file) throws IOException {
		String key = generateKey(file.getName());
		fileOutputStream(folderName, file, key);
		String size = String.format("%.3f Мб", (double) file.getSize() / 1_000_000);
		documentResourceRepository.createDocumentResource(
			file.getOriginalFilename(),
			file.getContentType(),
			key,
			size,
			documentIndex,
			id);
	}

}