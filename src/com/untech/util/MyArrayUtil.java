package com.untech.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;


/**
 * @Description: 数组工具类
 * @author jin.congshan
 * @date 2014-9-18 下午4:14:14
 */
public class MyArrayUtil {
	
 
	
	

	/**
	 * @Description ：读取byte数组中指定长度组成值的数组
	 * @author ：jin.congshan
	 * @date ：2014-9-18 下午4:14:58
	 */
	public static String[] getFixedValues(byte[] byteValues, int[] valueLength) {
		int start = 0;
		String[] values = new String[valueLength.length];
		for (int i = 0; i < valueLength.length; i++) {
			if (start >= ((byteValues.length - 1)) || (start + valueLength[i]) > (byteValues.length)) {
				values[i] = "";
				continue;
			}
			byte[] dis = new byte[valueLength[i]];
			System.arraycopy(byteValues, start, dis, 0, valueLength[i]);
			try {
				values[i] = new String(dis, "gbk");
			} catch (Exception e) {
				e.printStackTrace();
			}
			start += valueLength[i];
		}
		return values;
	}

	public static int byteToInt(byte[] bytes) {
		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = 0; i < bytes.length; i++) {
			n <<= 8;
			temp = bytes[i] & mask;
			n |= temp;
		}
		return n;
	}

	public static byte[] int2hexByte(int val, int byteSize) {
		String hex = Integer.toHexString(val);
		return hexString2byte(hex, byteSize);
	}
	
	public static byte[] hexString2byte(String hex) {
		return hexString2byte(hex, hex.length() / 2);
	}
	
	public static byte[] hexString2byte(String hex, int byteSize) {
		int size = byteSize * 2;
		Assert.notNull(hex);
		Assert.isTrue(hex.length() <= size);
		hex = StringUtils.leftPad(hex, size, '0');
		byte[] bytes = new byte[byteSize];
		String item;
		for (int i = 0; i < size; i += 2) {
			item = hex.substring(i, i + 2);
			bytes[i / 2] = (byte) Integer.parseInt(item, 16);
		}
		return bytes;
	}
	public static String byte2hexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer("");
		if(null == bytes || bytes.length == 0) {
			return null;
		}
		String hex;
		for (int i = 0; i < bytes.length; i++) {
			int v = bytes[i] & 0xFF;
			hex = Integer.toHexString(v);
			if (hex.length() < 2) {
				sb.append(0);
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	public static int hexBytes2int(byte[] bytes) {
		return Integer.valueOf(byte2hexString(bytes), 16);
	}
	public static byte byteArrayXor(byte[] bytes) {
		byte result = 0;
		for (byte b : bytes) {
			result ^= b;
		}
		return result;
	}
	
	public static String byteToBit(byte b) {
		return "" +(byte)((b >> 7) & 0x1) +
				(byte)((b >> 6) & 0x1) +
				(byte)((b >> 5) & 0x1) +
				(byte)((b >> 4) & 0x1) +
				(byte)((b >> 3) & 0x1) +
				(byte)((b >> 2) & 0x1) +
				(byte)((b >> 1) & 0x1) +
				(byte)((b >> 0) & 0x1);
	}
	
	public static String hexToBit(String hex) {
		StringBuffer sb = new StringBuffer();
		byte[] bytes = hexString2byte(hex, hex.length() / 2);
		for (byte b : bytes) {
			sb
			.append((byte)((b >> 7) & 0x1))
			.append((byte)((b >> 6) & 0x1))
			.append((byte)((b >> 5) & 0x1))
			.append((byte)((b >> 4) & 0x1))
			.append((byte)((b >> 3) & 0x1))
			.append((byte)((b >> 2) & 0x1))
			.append((byte)((b >> 1) & 0x1))
			.append((byte)((b >> 0) & 0x1));
		}
		return sb.toString();
	}
	public static String hextostr(String s){
		
		 byte[] baKeyword = new byte[s.length() / 2];  
		   for (int i = 0; i < baKeyword.length; i++) {  
		    try {  
		     baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(  
		       i * 2, i * 2 + 2), 16));  
		    } catch (Exception e) {  
		     e.printStackTrace();  
		    }  
		   }  
		   try {  
		    s = new String(baKeyword, "utf-8");// UTF-16le:Not  
		    System.out.println(s);
		   } catch (Exception e1) {  
		    e1.printStackTrace();  
		   } 
		   return s;
	}
	public static Integer[] getBitMap(String bitMapHexStr) {
		String bit = hexToBit(bitMapHexStr);
		char c;
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < bit.length();) {
			c = bit.charAt(i);
			i++;
			if(c == '1') {
				list.add(i);
			}
		}
		Integer[] bitMap = new Integer[list.size()];
		list.toArray(bitMap);
		return bitMap;
	}
	public static void main(String[] args) {
//		String s = "01[Pos No]";
//		byte[] b = s.getBytes();
//		String hex = byte2hexString(b);
//		System.out.println(s.length());
//		String len = MyArrayUtil.byte2hexString(MyArrayUtil.int2hexByte(s.length(), 2));
//		System.out.println(len+hex);
//		System.out.println(MyArrayUtil.hexBytes2int(MyArrayUtil.hexString2byte("0004")));
//		System.out.println(new String(MyArrayUtil.hexString2byte("000a30315b506f73204e6f5d")));
		//String s = "0020000000c00016";
		//System.out.println(Arrays.toString(getBitMap(s)));
		
		//System.out.println(Integer.valueOf("0063", 16));
		String str ="5B5468697369735469636B65744E6F5D5B4F7065724E6F5D5B506F73204E6F5D20202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020202020203030";
		System.out.println(MyArrayUtil.hextostr(str));
	}
}
