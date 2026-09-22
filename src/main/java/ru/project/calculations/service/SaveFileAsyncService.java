package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.project.calculations.enums.DocumentIndex;
import ru.project.calculations.repository.DocumentResourceRepository;
import ru.project.calculations.repository.UsersRepository;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

import static ru.project.calculations.util.ExcelFileReaderUtil.fileOutputStream;

@Service
@RequiredArgsConstructor
public class SaveFileAsyncService {

	private final UsersRepository usersRepository;

	private static String generateKey(String name) {
		return DigestUtils.md5Hex(name + LocalDateTime.now());
	}

	@Async
	public void saveAllDataDocumentResource(long id,
											String folderName,
											DocumentResourceRepository documentResourceRepository,
											DocumentIndex documentIndex,
											Principal principal,
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
			id,
			usersRepository.findUsersByUserName(principal.getName()).orElseThrow().getId(),
			LocalDateTime.now());
	}

}