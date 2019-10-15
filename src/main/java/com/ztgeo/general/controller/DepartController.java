package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.TreeUtil;
import com.ztgeo.general.biz.DepartBiz;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.MyFileUtil;
import com.ztgeo.general.vo.DepartTree;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/depart")
public class DepartController extends BaseController<DepartBiz, Depart> {

    @Autowired
    private DepartBiz departBiz;
    @Value("${receipt.departImgUrl}")
    private String savePath;

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<DepartTree> getTree() {
        List<Depart> departs = this.baseBiz.selectListAll();
        List<DepartTree> trees = new ArrayList<>();
        departs.forEach(dictType -> {
            trees.add(new DepartTree(dictType.getId(), dictType.getParentId(), dictType.getName(),dictType.getCode()));
        });
        return TreeUtil.bulid(trees, "-1", null);
    }

    @RequestMapping(value = "user",method = RequestMethod.GET)
    public TableResultResponse<User> getDepartUsers(String departId, String userName){
        return this.baseBiz.getDepartUsers(departId,userName);
    }

    @RequestMapping(value = "user",method = RequestMethod.POST)
    public ObjectRestResponse<Boolean> addDepartUser(String departId, String userIds){
        this.baseBiz.addDepartUser(departId,userIds);
        return new ObjectRestResponse<>().data(true);
    }

    @RequestMapping(value = "findDepartOrPosition",method = RequestMethod.POST)
    public Object findDepartOrPosition(){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(departBiz.findDepartOrPosition());
    }

    @RequestMapping(value = "user",method = RequestMethod.DELETE)
    public ObjectRestResponse<Boolean> delDepartUser(String departId, String userId){
        this.baseBiz.delDepartUser(departId,userId);
        return new ObjectRestResponse<>().data(true);
    }

    @RequestMapping(value = "uploadDepartPicture",method = RequestMethod.POST)
    public ObjectRestResponse<String> uploadDepartPicture(MultipartHttpServletRequest multiRequest){
        String departId = multiRequest.getParameter("id");
        MultipartFile file = multiRequest.getFile("file");
        System.out.println(departId+" and "+file);
        try {
//        String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
//            System.out.println(fileName);
            //扩展名截取
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            fileName = UUID.randomUUID().toString() + "." + prefix;
            String filePath = savePath + fileName;

            //以获取到的文件名命名文件，生成文件对象
            File destFile = new File(filePath);
            //文件对象读获取到的字节数组
            InputStream file_is = file.getInputStream();
            byte[] fileData = new byte[(int) file.getSize()];
            file_is.read(fileData);
            //使用aphache工具处理文件
            FileUtils.writeByteArrayToFile(destFile, fileData);
            Depart depart = new Depart();
            depart.setId(departId);
            depart.setSavePath(filePath);
            this.departBiz.updateSelectiveById(depart);
        } catch (IOException e){
            e.printStackTrace();
            throw new ZtgeoBizException("图片解析异常");
        } catch (Exception ee){
            ee.printStackTrace();
            throw new ZtgeoBizException("数据更新异常");
        }
        return new ObjectRestResponse<String>().data("上传成功");
    }

    @RequestMapping(value = "loadPicture", method = RequestMethod.GET)
    public ObjectRestResponse<String> loadPicture(@RequestParam("id") String id){
        Depart depart = this.departBiz.selectById(id);
        String path = depart.getSavePath();
        String base64 = "" ;
        if(StringUtils.isNotBlank(path)) {
            try {
                byte[] data = MyFileUtil.getImgByURL(path);
                //对字节数组Base64编码
                BASE64Encoder encoder = new BASE64Encoder();
                if (data != null) {
                    base64 = encoder.encode(data);
                } else {
                    throw new ZtgeoBizException("加载的图片已被删除");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ZtgeoBizException("图片加载失败");
            }
        }
        return new ObjectRestResponse<String>().data(base64);
    }

    @RequestMapping(value = "deletePicture", method = RequestMethod.POST)
    public ObjectRestResponse<String> deletePicture(@RequestParam("id") String id){
        Depart depart = this.departBiz.selectById(id);
        String path = depart.getSavePath();
        try{
            MyFileUtil.deleteByUrl(path);
        } catch (IOException e){
            e.printStackTrace();
        }
        depart.setSavePath(null);
        this.departBiz.updateById(depart);
        return new ObjectRestResponse<String>().data("删除成功");
    }

}
