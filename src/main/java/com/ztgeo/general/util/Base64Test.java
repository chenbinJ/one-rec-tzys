package com.ztgeo.general.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

public class Base64Test {

    public static void main(String[] args) throws Exception
    {
         String strImg ="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAASrUlEQVR42uWZCVCU55aGRQN0N2IUcYnGJZrE3cQtmhVkRxRcEEFFEBSUKyiE" +
                 "y9LQDJuCQA0gizDiUgKDCjqWikupXPXCAGIVxSLDsBUCA4WKNaASKNF33u/v9sbr3CQ3c5M7kxqrTnU39I/vc857zvd9/z9s2P/Lf8Z5U2b43Gr/2OcPkfr25979TTKMsMz+14Xh" +
                 "Jdh0pP67D7dl/cOwz2Lf+U0BDF+RuUDLLg9T992Ec34HjOLudhmahTv8piBGbvjnymGmmZj8+0pYFbzCprNPMNs19abOp85f/CYAxmzOT3ln/XlorbmMaf6VWJw5iFUFwFexdzHD" +
                 "8R+rR5uHRWibx36pvVKp9/a1cnPV8P91gLFb8w7obTwLLdurkDmWYvLeWnwU+x0WpL/EZzH/hkWRlZirrMBEr6L/1HcquCFbnRois4j+XGamHPnm35GZhQxnaP1dRCssw99h6EoA" +
                 "204X6jvfgrZTGXScKqHYVg0Dj/sYt4fhfR/jvWsxcV8NJuyrxujdldDeUoJhdtcwbFVB53CrrFxti4MOMlOl4d9HuEXYcCGcMUZhrtRRWEcvG+9xB/LNf6DwSozcXguZcw20N1Ms" +
                 "IeaqmmGS2Aa7jHZsONIO28MPYJrUjCX76zEtoAYj3e9Ca2MRhq0+1zvcMuOkjnnU17+KcLlFmBbFi6zrM8YrLELG6FmFzx/rerH+3Z1lULiUQ+FaA7lrLd73a8Ca9A4EX+xBXFEfk" +
                 "u/0IaX4KdJLnyKj9BmO3BWh/hxz4wk8TnVheUwD9AkzbN11aNnkFOuY7bf8JcUPZ+gQ4F2Kn6xnGTJBYRVtp+98qWuUx12KruB/Xo2Zyiasz+qE6vITxBQxbvYilgBJd54iVYgvfy" +
                 "1eHZn8nFH+9E+vB673wDq9Bfo7y6FlfwPv2Jws1DYJn/JLAOhS/FiKn0LxU+VWsV7amy4PKdzLId9egUm+dVh7pAt+F3oQcqUHoVefIPJ6Lw7eYvZLmGkKPHrvOU5WPUduTT9O1a" +
                 "ojt7ofJyqfI6viGdIIeKi4j699CLvyGAsj72MEe+Ud+8sD2hbxDn9L5nXlQrxV+Ad6lsoPZVYHA7Udr0J/Rxn0Pe7ho5AGuOQ+hNfZHvice4yASz0Iu/YE+4t6kUDbpJUx2xR/o" +
                 "vq5JLqgbgAXGgdwsXEQ5+sHkH+/H9n8nQBM53cTabe4W72MPpgeaoGOSyn7qQjaq9KVP9vzDG2GATM/nZmfJbeK+VbHieI9y2GwpxLvB9bD4Xg3tp96iJ35j+Bz4TECaZ/wG72IuU3riOzTKlk" +
                 "i+8z2KYotoOjzBLjQRICGAelzHsFOsDqZrERK2VPE8dpo2i+GSViZ3AKZWykHA6ecTbr/zwBQjaD40RQ/VWEZOkduGeGm63h5aNTuchjurcJ4vzp8mdSGLTndcM4jQAEB2LRBV7" +
                 "4HSCRA2l11BY4zyzkEOF3Xj3yKLqD4fMZpViRXABBQfC+VdhOV28/eCbv+hNXswdzweihYcZkzK2GVsPqnxZurtBgKAkxWWKrmKMxCrXQ35neN2n0X43yrMDmoHtNVTbDO7ITDyW" +
                 "5sFRU4+xjeBAgggIoA0QSIJ8AhCkqvoM+Z4eP0fzbFCpBcguTeV78/yZ8fFRUgQAq/n0iAhPIX2F8yhMhb/axwF8bu5Vj2ugtdp8v9OibhY35KvDaDTRv2scI8eKnMNuuavmcZxv" +
                 "tWYoqyDjPCmzF3fytsjnZiQ3Y3thDAnQBeBPAjgJIAEfRwjBBCXx9iFdIp7th9Zr0VKGDk8L0AOsYQcBls5jRaKIkWSql6CYe0Cpj7p8Mxqx6qon4siG7C2H2VEA7QsT2e/WM" +
                 "AIxgjmf0pnPXzZRYHfq9wK4KhRvxHkc2YTfEL4h7AOqsLawngeOoRXAngSYC9BAjkBFLRv1FsyNhSNUR6VT+SKgZgHnURtglFyKgdwrHaARymcAGXSvHJzH7yvX4moA+GK32wYEsob" +
                 "GMKsediL8xY7YmBXNn9KqG3/Sa0TSJn/RCAyP44js1ZcrPgr2X2p/7dYG8F3g/m/iayCfNiWzGf4j+Jb4M5Z74tARxYAWcC7LjIKrCJfQkQzCZU0UaRtFEMIdLrgBX+edD/1AHvmXvBKOI" +
                 "SMutfIYUASaxQAsUfZPYPMfv26RUYu8JZCgFhk1iONdm9mKZqxGTqMPCpYBWysv67eLPQ4QwFASbLzUPmySxjgvU9bpOci1REgyT+k4Q2LGLzLj7UDqMjnbAhwHoCbC54DFdOIc/LPfC+1g" +
                 "s/2iiYNlJxEdtf3o/w8kFMtPDDhK9dpVjmEYcd+S1IrX2BWIqPofhowsZXARbRV2CweKMUc52CpSqYHO3BrAMttG899VRB4Xy5T9dYqTvsz3eDoSMY73ICzZSbBi2Trz9ebOh7D9PDuKeJbc" +
                 "GniWrhy1I7sJxbhS8JYMkmtiOAA6fQVgK4FfZgF9cBHwL4ESCINoq82489Vx/D8Ct3TPhmuxRzt0fDJv4qYiqHEEXxERQfRtiIskE4ZFRg+ppgKWZtDJZ6YWlKFxYyeXNimjE1pJabwmLomB8" +
                 "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" +
                 "5BaqqTLT4A+0jQJnaxsFLdc1UdkqrKPd9O3i/cesTwpnRI5elxiibxv3O4V11CaZSZixjrFylu7K4Alys5AxCkuVgUa4nDFi2K/9TwMhQocAcpE5hiFBJoq72uLuhnQ4Mg15n40" +
                 "4RWainCxu2TAm8nsTKHg8hRpKDwuFVdQPDn994T9yU3i45r6qLiHErXlxg3gUY4z0nEEd4hnbKI1guUa0eHCoJeL/xFN7cXte84BES/OIasTrkB7Rqh/TammeNf9iov8LtDeoo+GAn88AA" +
                 "AAASUVORK5CYII=";


         String a="2019\\06\\18\\FINST-20190618-6CC150B2A7D.jpeg";
         String c=a.replaceAll("\\\\","/");


//            MultipartFile file=base64ToMultipart(strImg);
//            System.out.println(file.getOriginalFilename());
            System.out.println(c);
//            file.getOriginalFilename();

       /* System.out.println(strImg);
        String a=showURL();
        System.out.print(a);*/

    }

/*
    public static String showURL() throws IOException {
        URL url= Base64Test.class.getResource("");
        String p = url.getPath();
        System.out.println("方法二路径："+p);
        try {
            System.out.println("方法二解码路径："+ URLDecoder.decode(p, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return p;
    }*/

    //图片转化成base64字符串
    public static String GetImageStr()
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "D:\\tupian\\a.jpg";//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }

    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }





    //base64字符串转化成图片
    public static String GenerateImage(String imgStr, HttpServletRequest request)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            throw  new  RuntimeException("图片64位不存在");
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            String random=StringRandom.getStringRandom(8);
            //生成jpeg图片
            String basePath=CommonUtils.getRealPath()[0];//绝对路径
            String imgFilePath=basePath+"upload" + File.separator +random+".jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            String absolutePathPath=CommonUtils.getRealPath()[1];//相对路径
            String tp= absolutePathPath + "upload" + File.separator +random+".jpg";
            out.write(b);
            out.flush();
            out.close();
            return tp;
        }
        catch (Exception e)
        {
            return e.toString();
        }
    }
}
