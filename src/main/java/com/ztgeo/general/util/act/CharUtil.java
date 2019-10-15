package com.ztgeo.general.util.act;

import java.util.Random;

public class CharUtil {
	
	 private final static String[] hexDigits = { "a", "b", "c", "d", "e", 
		 "f", "g", "h", "i", "j", "k", "l","m", "n", "o", "p", "q", "r","s",
		 "t", "u", "v", "w", "x", "y","z"}; 
	 
	 private final static int charNum=3;
	 
	 /**
	  * 随机生成charNum个小写字母组成的字符
	  * @return
	  */
	 public static String getRandomString(){

		 StringBuffer sb = new StringBuffer();
		 Random random=new Random();	 
		 for(int i=0;i<charNum;i++){
			 int pos=random.nextInt(26);
			 sb.append(hexDigits[pos]);
		 }

		return sb.toString();	 
	 }

}
