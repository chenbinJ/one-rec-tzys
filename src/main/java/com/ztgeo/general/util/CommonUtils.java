package com.ztgeo.general.util;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;




/**
* 工具方法
*/
public class CommonUtils {
    private static final Logger logger = LogManager.getLogger(CommonUtils.class);
    /**
    * 判断字符串是否为空
    * @param str 需要判断是否为空的字符串参数
    * @return 为空或者null返回true
    * @author hey
    * @Date    2017年8月14日上午11:46:14
    * @version 1.00
    */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
	/**
	* 对参数进行Base64解码
	* @param param 待解析的字符串
	* @return 返回解析的byte数组
	* @throws IOException
	* @author hey
	* @Date    2017年9月1日下午4:44:34
	* @version 1.00
	*/
	public static byte[] decodeBase64(String param) throws IOException {
		// 对base64数据进行解码 生成 字节数组，不能直接用Base64.decode（）；进行解密
		byte[] photoimg = new BASE64Decoder().decodeBuffer(param);
		for (int i = 0; i < photoimg.length; ++i) {
			if (photoimg[i] < 0) {
				// 调整异常数据
				photoimg[i] += 256;
			}
		}
		return photoimg;
	}

    /**
     * 模板关系
     * @param ral
     * @param steps
     */
    public static void dealStep2(List<SJ_Act_Step> ral, List<SJ_Act_Step> steps) {
        if(steps!=null && steps.size()>1) {
            for(int i=0;i<steps.size();i++) {
                SJ_Act_Step step_i = steps.get(i);
                if(step_i.getStepType().equals("ParalleGateway")) {
                    continue;
                }else {
                    if(i<steps.size()-2) {
                        for(int j=i+1;j<steps.size();j++) {
                            SJ_Act_Step step_j = steps.get(j);
                            if(step_j.getStepType().equals("ParalleGateway")) {
                                if(j<steps.size()-1) {
                                    for(int k=j+1;k<steps.size();k++) {
                                        SJ_Act_Step step_k = steps.get(k);
                                        if(step_k.getStepType().equals("ParalleGateway")) {
                                            break;
                                        }else {
                                            SJ_Act_Step str = new SJ_Act_Step();
                                            str.setParentStepMouldId(step_i.getStepMouldId());
                                            str.setStepMouldId(step_k.getStepMouldId());
                                            ral.add(str);
                                        }
                                    }
                                }
                                break;
                            }else {
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }


    
    /**
    * 对参数param进行lang类型的转换
    * @param param 一个可以装换为long类型的数字
    * @return 如果没有出现异常会返回一个long类型数字，如果出现异常会抛出NumberFormatException
    * @author hey
    * @Date    2017年9月1日下午4:35:17
    * @version 1.00
    */
    public static Long convertLong(String param){
    	try {
    		return Long.valueOf(param);
		} catch (NumberFormatException e) {
			logger.info("转换失败，在调用通用方法CommonUilts.convertLong对参数：{}进行Long类型转换的时候，出现了异常，请确认传入的是数字");
			throw new RuntimeException(e);
		}
    }

    /**
    * 判断一个字符串中是否包含“#”
    * @param param 需要判断的字符串
    * @return 如果此字符串包含 param，则返回 true，否则返回 false
    * @author hey
    * @Date    2017年9月1日下午3:23:49
    * @version 1.00
    */
    public static boolean isComma(String param){
    	return param.contains("#");
    }
    
    /**
    * 判断集合是否为空
    * @param collection 需要判断的集合
    * @return 为空或者null返回true
    * @author hey
    * @Date    2017年8月14日上午11:47:27
    * @version 1.00
    */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() < 1;
    }

    /**
    * 判断Map是否为空的方法
    * @param map 需要判断的map
    * @return 为空或者null返回true
    * @author hey
    * @Date    2017年8月14日上午11:48:02
    * @version 1.00
    */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Map map) {
        return map == null || map.size() < 1;
    }

    /**
    * 判断字符串是否满足指定值
    * @param str 需要判断的字符串
    * @param length 需要满足的值
    * @return 字符串不为null且满足指定长度返回true
    * @author hey
    * @Date    2017年8月14日上午11:48:40
    * @version 1.00
    */
    public static boolean isLengthEnough(String str, int length) {
        if (str == null) {
            return false;
        }
        return str.length() >= length;
    }

    /**
    * 计算一个字符串的MD5值
    * @param s 
    * @return 返回MD5值
    * @author hey
    * @Date    2017年8月14日上午11:52:24
    * @version 1.00
    */
    public final static String calculateMD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * 用一个随机数加UUID码，生成一个RC4对称密钥
    * @param param 随机数
    * @param UUID
    * @return 返回一个RC4对称密钥
    * @author hey
    * @Date    2017年8月21日下午5:00:11
    * @version 1.00
    */
    public static String HloveyRC4(String param, String UUID) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];
        for (int i = 0; i < 256; i++)
            iS[i] = i;
        int j = 1;
        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) UUID.charAt((i % UUID.length()));
        }
        j = 0;
        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }
        int i = 0;
        j = 0;
        char[] iInputChar = param.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }
        return new String(iOutputChar);
    }
    
    /**
    * 将字符串转化为16进制
    * @param param   
    * @return 返回16进制字符串
    * @author hey
    * @Date    2017年8月21日下午4:49:16
    * @version 1.00
    */
    public static String toHexString(String param) {  
       String str = "";  
       for (int i = 0; i < param.length(); i++) {  
        int ch = (int) param.charAt(i);  
        String s4 = Integer.toHexString(ch);  
        str = str + s4;  
       }  
       return str;  
    }  
    
    /**
    * 获取一个UUID字符串
    * @return 返回一个去掉“-”，以及全部字母都为大写的UUID字符串
    * @author hey
    * @Date    2017年8月21日下午5:03:39
    * @version 1.00
    */
    public static String getUUID(){
    	return UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
    }

    /**
     * 获取一个UUID字符串
     * @return 返回包含“-”，以及全部字母都为小写的UUID字符串
     * @author hey
     * @Date    2017年8月21日下午5:03:39
     * @version 1.00
     */
    public static String getUUIDWith_(){
        return UUID.randomUUID().toString().toLowerCase();
    }
    
	/**
	* 校验ip地址是否正确
	* @param ip 需要校验的ip地址
	* @return 正确返回true
	* @author hey
	* @Date    2017年10月12日上午10:03:00
	* @version 1.00
	*/
	public static boolean isIpCorrent(String ip){
		String str="\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
		return ip.matches(str);
	}
	
    /**
    * 生成一个14位邀请码
    * @return 返回一个14位字符串邀请码
    * @author hey
    * @Date    2017年8月21日下午5:10:58
    * @version 1.00
    */
    public static String getInviteCode() {
        String UUID = CommonUtils.getUUID();
        String random = new Random().nextInt(9000000) + 1000000 + "";
        String rc4 = CommonUtils.HloveyRC4(random, UUID);
        return CommonUtils.toHexString(rc4).toUpperCase();
    }

    /**
     * 生成code 001 002 003递增
     * @param code
     * @return
     */
    public static String getCode(String code){
        String someCoede=null;
        try {
            int number = Integer.parseInt(code);
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            if(code.substring(0, 1).endsWith("0")){
                numberFormat.setMinimumIntegerDigits(3);
            }else{
                numberFormat.setMinimumIntegerDigits(code.length());
            }
            numberFormat.setGroupingUsed(false);
            String s = numberFormat.format(number);
            number = Integer.parseInt(s);
            number++;
            someCoede=numberFormat.format(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return  someCoede;
    }

    /**
     * 用于返回map对象
     * @param flag
     * @param msg
     * @return
     */
    public static Map<String,Object> getMsgForRet(Boolean flag, Object msg){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("flag",flag);
        returnMap.put("msg",msg);
        return returnMap;
    }

    /**
     * 获取项目路径
     * @return{绝对路径,相对路径}
     */
    public static String[] getRealPath(){
        try {
            //获取跟目录
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists())
                path = new File("");
            System.out.println("path:"+path.getAbsolutePath());
            //如果上传目录为/static/images/upload/，则可以如下获取：
            File upload = new File(path.getAbsolutePath(),"static/");
            if(!upload.exists())
                upload.mkdirs();
            //在开发测试模式时，得到的地址为：{项目跟目录}/target/static/images/upload/
            // 在打包成jar正式发布时，得到的地址为：{发布jar包目录}/static/images/upload/
            System.out.println("upload absolutePathUrl:"+upload.getAbsolutePath());
            System.out.println("upload url:"+upload.getAbsolutePath());
            return new String[]{upload.getAbsolutePath() + File.separator,
                    (upload.getAbsolutePath() + File.separator)
                            .substring((upload.getAbsolutePath() + File.separator).indexOf("static"))};
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static   String uploadPicture(MultipartFile file,String id) throws IOException{
        String  fileSpace="D:"+File.separator+"priture"+File.separator+id;
        String uoloaddb="";
        FileOutputStream fileOutputStream=null;
        InputStream inputStream=null;
        try {
            if (file!=null){
                String name=file.getOriginalFilename();//名称
                if (StringUtils.isNoneBlank(name)){
                    //路径
                    String  priture=fileSpace+"/"+name;
                    //设置数据库保存路径
                    uoloaddb=File.separator+id+File.separator+name;
                    File outfile=new File(priture);
                    if (outfile.getParentFile()!=null || outfile.getParentFile().isDirectory()){
                        outfile.getParentFile().mkdir();
                    }
                    //输出
                    fileOutputStream =new FileOutputStream(outfile);
                    inputStream=file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            }
        }catch (Exception e){
            e.getMessage();
        }finally {
            if (fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        return uoloaddb;
    }


    /**
     * 获取项目路径 IP:端口
     * @return
     */
    public static String getRealPathIPAndPort(){
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();//IP地址
            System.out.println("upload url:"+InetAddress.getLocalHost().getHostAddress());
            return "http://" + ip + ":" /*+ new ServiceUtil().getPort()*/;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }



    /**
     *
     * @param id
     *            fileId
     * @param name
     *            pdf名称
     *
     */
//    public static String toPdf(String id,String name) {
//        String imagePath = null;
//        String path=null;
//        try {
//            // 图片文件夹地址
//            String    imageFolderPath = "D:"+File.separator+"priture"+File.separator+id+File.separator;
//            // 图片地址
//            // PDF文件保存地址
//            String   pdfPath ="D:"+File.separator+"priture"+File.separator+id+File.separator+name;
//            path=File.separator+id+File.separator+name;
//            File outfile=new File(pdfPath);
//            if (outfile.getParentFile()!=null || outfile.getParentFile().isDirectory()){
//                outfile.getParentFile().mkdir();
//            }
//            // 输入流
//            FileOutputStream fos = new FileOutputStream(pdfPath);
//
//            // 创建文档
//            Document doc = new Document(null, 0, 0, 0, 0);
//            //doc.open();
//            // 写入PDF文档
//            PdfWriter.getInstance(doc, fos);
//            // 读取图片流
//            BufferedImage img = null;
//            // 实例化图片
//            Image image = null;
//            // 获取图片文件夹对象
//            File file = new File(imageFolderPath);
//            File[] files = file.listFiles();
//            // 循环获取图片文件夹内的图片
//            for (File file1 : files) {
//                if (file1.getName().endsWith(".png")
//                        || file1.getName().endsWith(".jpg")
//                        || file1.getName().endsWith(".gif")
//                        || file1.getName().endsWith(".jpeg")
//                        || file1.getName().endsWith(".tif")) {
//                    // System.out.println(file1.getName());
//                    imagePath = imageFolderPath +File.separator+ file1.getName();
//                    System.out.println(file1.getName());
//                    // 读取图片流
//                    img = ImageIO.read(new File(imagePath));
//                    // 根据图片大小设置文档大小
//                    doc.setPageSize(new Rectangle(img.getWidth(), img
//                            .getHeight()));
//                    // 实例化图片
//                    image = Image.getInstance(imagePath);
//                    // 添加图片到文档
//                    doc.open();
//                    doc.add(image);
//                }
//            }
//            doc.open();
//            // 关闭文档
//            doc.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (BadElementException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//        return path;
//    }




 /*   *//**
     * 获取当前用户信息
     * @param request
     * @return
     *//*
    public static Employee getUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("user");
        if(user==null){
            String userId = request.getParameter("userId");
            EmployeeMapper employeeMapper = SpringContextUtils.getBean(EmployeeMapper.class);
            user = employeeMapper.findById(userId);
            if(user==null){
                throw new RuntimeException(MsgManager.USER_INFO_IS_NULL);
            }
            //获取部门
            user = employeeMapper.findLogin(user.getuCode(),user.getuPwd());
            if(user==null){
                throw new RuntimeException(MsgManager.USER_INFO_IS_NULL);
            }
        }
        *//*Employee user = new Employee("1","admin","1","管理员","000000",
                "超级管理员","","",null,"admin",
                null,"admin","0","");*//*
        logger.info("获取用户信息(myUserInfo):" + user.getuCode()+"  "+user.getdName()+"  "+user.getdCode()+"  "+user.getuName());
        return user;
    }*/


}
