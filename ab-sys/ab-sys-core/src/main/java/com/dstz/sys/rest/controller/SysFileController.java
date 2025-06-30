package com.dstz.sys.rest.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.AESUtil;
import com.dstz.base.common.utils.AbRequestUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.sys.api.constant.SysApiCodes;
import com.dstz.sys.api.dto.SysFileDownloadDto;
import com.dstz.sys.core.entity.SysFile;
import com.dstz.sys.core.manager.SysFileManager;
import com.dstz.sys.rest.model.dto.OperateOnlineDocDTO;
import com.dstz.sys.rest.model.dto.UpdateFileDTO;
import com.dstz.sys.rest.model.vo.CreateAndOpenVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * System accessories front-end controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/sysFile")
public class SysFileController extends AbCrudController<SysFile> {

    private Logger logger = LoggerFactory.getLogger(SysFileController.class);

    private static final Integer FILE_NAME_MAX_LENGTH = 60;
    //Default dictionary categories for file uploads
    private static final String DEFAULT_DIC_CODE = "mrfl";

    @Autowired
    private  SysFileManager sysFileManager;

    /**
     * <pre>
     * </pre>
     *
     * @param file    The file itself
     * @param dicCode File dictionary classification code
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file, String dicCode) throws IOException {
        if (file.getOriginalFilename().length() > FILE_NAME_MAX_LENGTH) {
            throw new BusinessMessage(SysApiCodes.FILE_NAME_LENGTH_ERROR);
        }
        if (StrUtil.isEmpty(dicCode)) {
            dicCode = DEFAULT_DIC_CODE;
        }
        SysFile sysFile = sysFileManager.upload(file.getInputStream(), file.getOriginalFilename(), dicCode);
        return ApiResponse.success(sysFile.getId()).withMessage("上传成功");
    }

    /**
     * Browse files, support pictures
     *
     * @param fileId   File ID
     * @param response Response
     */
    @GetMapping(value = "/view/{fileId}")
    public void viewFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        SysFile sysFile = sysFileManager.getById(fileId);
        String mineType;
        if (Objects.isNull(sysFile) || StrUtil.isEmpty(mineType = FileUtil.getMimeType(sysFile.getName()))) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        InputStream inputStream = sysFileManager.download(fileId);
        if (inputStream == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        final Date nowDateTime = new Date();
        final Date expireDateTime = DateUtils.addYears(nowDateTime, 1);
        response.addHeader(HttpHeaders.EXPIRES, DatePattern.JDK_DATETIME_FORMAT.format(expireDateTime));
        response.addHeader(HttpHeaders.CACHE_CONTROL, String.format("max-age=%d", TimeUnit.MICROSECONDS.toSeconds(expireDateTime.getTime() - nowDateTime.getTime())));
        ServletUtil.write(response, inputStream, mineType);
    }

    /**
     * <pre>
     * </pre>
     *
     * @param fileDto File id and token encrypted file id key
     * @return
     * @throws Exception
     */
    @PostMapping(value = "download")
    public void download(@RequestBody @Valid SysFileDownloadDto fileDto, HttpServletResponse response) throws Exception {
        String userId = UserContextUtils.getUserId();
        // The encryption key must be 16, 24, or 32 bits, otherwise an exception will be thrown, so the front-end and back-end uniformly take the first 16 bits of the token
        String key = AESUtil.encryptKey(fileDto.getId(), userId.substring(0, 16));
        if (!StrUtil.equals(key, fileDto.getKey())) {
            return;
        }
        SysFile sysFile = sysFileManager.getById(fileDto.getId());
        if (Objects.isNull(sysFile)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        InputStream inputStream = sysFileManager.download(fileDto.getId());
        if (inputStream == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
//        String mimeType = StrUtil.nullToDefault(FileUtil.getMimeType(sysFile.getName()), MediaType.APPLICATION_OCTET_STREAM_VALUE);
        ServletUtil.write(response, inputStream, MediaType.APPLICATION_OCTET_STREAM_VALUE, sysFile.getName());
    }

    @RequestMapping(value = "zip", method = RequestMethod.GET)
    public ResponseEntity<byte[]> zip(@RequestParam("fileIds") String fileIds) throws Exception {
        File zipDirectory = new File(StrUtil.join(FileUtils.getTempDirectoryPath(), File.separator, RandomUtil.randomString(6)));
        File zipFile = null;
        try {
            if (!zipDirectory.mkdir()) {
                throw new BusinessMessage(SysApiCodes.FILE_CREATE_DIR_ERROR.formatDefaultMessage(zipDirectory.getPath()));
            }
            downloadToDirectory(StrUtil.split(fileIds, CharUtil.COMMA), zipDirectory);
            zipFile = ZipUtil.zip(zipDirectory);
            final String zipName = DatePattern.PURE_DATETIME_FORMAT.format(new DateTime()) + ".zip";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(zipName, StandardCharsets.UTF_8.displayName()));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(Files.readAllBytes(zipFile.toPath()), headers, HttpStatus.OK);
        } finally {
            if (zipFile != null) {
                FileUtils.deleteQuietly(zipFile);
            }
            FileUtils.deleteDirectory(zipDirectory);
        }
    }

    /**
     * Download to the specified directory
     *
     * @param fileIds   File number set
     * @param directory Download catalog
     * @throws IOException The file does not exist or there is a read or write problem
     */
    private void downloadToDirectory(List<String> fileIds, File directory) throws IOException {
        for (String id : fileIds) {
            SysFile sysFile = sysFileManager.getById(id);
            if (sysFile == null) {
                throw new BusinessMessage(SysApiCodes.FILE_NOT_FOUND_ERROR.formatDefaultMessage(id));
            }
            try (InputStream downloadStream = sysFileManager.download(id)) {
                try (OutputStream fos = new FileOutputStream(new File(directory, sysFile.getName()))) {
                    IOUtils.copyLarge(downloadStream, fos);
                }
            }
        }
    }

    @RequestMapping(value = "del")
    public ApiResponse<String> del(@RequestParam("fileId") String fileId) throws Exception {
        sysFileManager.delete(fileId);
        return ApiResponse.success("Delete successfully");
    }

    @RequestMapping(value = "update")
    public ApiResponse update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileId");
        String userId = request.getParameter("userId");
        Part file = request.getPart("file");

        UpdateFileDTO updateFileDTO = new UpdateFileDTO();
        updateFileDTO.setFileId(fileId);
        updateFileDTO.setFileStream(file.getInputStream());
        // todo Use current username
        updateFileDTO.setUserName("test");
        updateFileDTO.setUserId(userId);
        int result = sysFileManager.update(updateFileDTO);

        return ApiResponse.success(result).withMessage("Update successful " + result + " items");
    }

    @Override
    protected String getEntityDesc() {
        return "System accessories";
    }
 

    private String getUrlPrefix(HttpServletRequest request) {

        List<String> pathStrList = StrUtil.split(request.getServletPath(), StrPool.SLASH);
        pathStrList.remove(pathStrList.size() - 1);

        String resultPath = CollUtil.join(pathStrList, StrPool.SLASH);
        String apiAgentPrefix = StrUtil.trimToEmpty(PropertyEnum.API_NGINX_AGENT_PREFIX.getPropertyValue(String.class));
        if (StrUtil.isNotEmpty(apiAgentPrefix)) {
            resultPath = apiAgentPrefix + resultPath;
        }

        if (logger.isDebugEnabled()){
            logger.debug("OnlineDocService :  requestUrl :  {}", request.getRequestURL());
            logger.debug("OnlineDocService :  path :  {}", resultPath);
            logger.debug("OnlineDocService :  url :  {}", StrUtil.removeSuffix(request.getRequestURL(), request.getServletPath()) + resultPath);
        }

        return StrUtil.removeSuffix(request.getRequestURL(), request.getServletPath()) + resultPath;
    }


}
