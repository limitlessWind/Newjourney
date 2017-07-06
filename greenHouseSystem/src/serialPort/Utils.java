package serialPort;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6122351378927881753L;
	
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}  
	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	 private static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	} 
	 /**
	  * 数组转换成十六进制字符串
	  * @param byte[]
	  * @return HexString
	  */
	 public static final String bytesToHexString(byte[] bArray) {
	  StringBuffer sb = new StringBuffer(bArray.length);
	  String sTemp;
	  for (int i = 0; i < bArray.length; i++) {
		   sTemp = Integer.toHexString(0xFF & bArray[i]);
		   if (sTemp.length() < 2)
			   sb.append(0);
		   sb.append(sTemp);
	 }
	  return sb.toString();
	 }
/*	public static byte[] hexString2Bytes(String src) {  	//16-10
        int l = src.length() / 2;  
        byte[] ret = new byte[l];  
        for (int i = 0; i < l; i++) {  
            ret[i] = (byte) Integer  
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();  
        }  
        return ret;  
    } */ 

	public static byte[] hexString2HexBytes(String src) {  	//16进制字符串-byte[]
        int l = src.length() / 2;  
        byte[] ret = new byte[l];  
        for (int i = 0; i < l; i++) {  
            ret[i] = Integer  
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();  
        }  
        return ret;  
    } 
	
	/** 
     * 把byte转为字符串的bit 
     */  
    public static String byteToBit(byte b) {  
        return ""  
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)  
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)  
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)  
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);  
    } 
	
	public static String hexBytes2String(byte[] bytes) { 	//10-16
		BigInteger bi = new BigInteger(bytes);
		return bi.toString(16);
	}
	
	 public static String string2Hex(String s) {		// 转化为16进制
			BigInteger bi = new BigInteger(1, s.getBytes());			 
			 return bi.toString(16);			 
	}
		 
	 public static String generateZero(int len) {
			StringBuilder zeroBuilder = new StringBuilder();
			int i = 0;
			while ( i < len) {
				zeroBuilder.append("00");
				i++;
			}
			return zeroBuilder.toString();
		}
	 
	 public static String addSpace(String param) {
	     String regex = "(.{2})";
	     param = param.replaceAll (regex, "$1 ");
		 return param;
	 }

	 public static String time() {
		 Date date=new Date();
		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String time=format.format(date);
		 return time;
	 }
	 
	 //"14-15" --- [14, 15]
	 public static int[] location2Ints(String location) {
		 String[] locals = location.split("-");
		 int[] arr = new int[locals.length];
		 for (int i = 0; i < locals.length; i++) {
			 arr[i] = Integer.parseInt(locals[i]);
		}
		 return arr;
	 }
}
