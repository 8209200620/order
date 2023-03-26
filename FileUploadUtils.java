package edu.canteen.order.system.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtils {
	public static File saveFile(String path, MultipartFile file) throws IllegalStateException, IOException {
		String originalFilename = file.getOriginalFilename();
		File dest = new File(path, originalFilename);
		if(!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		file.transferTo(dest);
		return dest;
	}
	
}
	
