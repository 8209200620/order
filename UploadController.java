package edu.canteen.order.system.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.canteen.order.system.pojo.Result;
import edu.canteen.order.system.util.FileUploadUtils;

@RestController
@RequestMapping("upload")
public class UploadController {
	@Value("${file.upload.path}")
	private String path;
	/**
	 * 上传文件
	 * @param file
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@PostMapping("file")
	public Result<String> upload(MultipartFile file) throws IllegalStateException, IOException{
		Result<String> result = new Result<String>();
		File uplaodFile = FileUploadUtils.saveFile(path, file);
		result.setCode(200);
		result.setData("上传成功");
		result.setData(uplaodFile.getName());
		
		return result;
	}
}
