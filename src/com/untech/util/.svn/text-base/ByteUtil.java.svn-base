 package com.untech.util;

/**
 * 字节数组转换工具类
 * 
 * @version 1.5 2010-11-10
 * @author yangw
 */

public class ByteUtil {

	/**
	 * short转换为字节数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] short2byte(short n) {
		byte b[] = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}
	
	/**
	 * int转换为字节数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] int2byte(int n) {
		byte b[] = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) n;
		return b;
	}

	/**
	 * long转换为字节数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] long2byte(long n) {
		byte b[] = new byte[8];
		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;
	}

	/**
	 * byte数组转int
	 * 
	 * @param b
	 *            源byte数组
	 * @param offset
	 *            起始索引
	 * @return
	 */
	public static int byte2int(byte b[], int offset) {
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8
				| (b[offset + 1] & 0xff) << 16 | (b[offset] & 0xff) << 24;
	}

	/**
	 * byte数组转int
	 * 
	 * @param b
	 *            源4位byte数组
	 * @return
	 */
	public static int byte2int(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16
				| (b[0] & 0xff) << 24;
	}

	/**
	 * byte数组转long
	 * 
	 * @param b
	 *            源8位byte数组
	 * @return
	 */
	public static long byte2long(byte b[]) {
		return (long) b[7] & (long) 255 | ((long) b[6] & (long) 255) << 8
				| ((long) b[5] & (long) 255) << 16
				| ((long) b[4] & (long) 255) << 24
				| ((long) b[3] & (long) 255) << 32
				| ((long) b[2] & (long) 255) << 40
				| ((long) b[1] & (long) 255) << 48 | (long) b[0] << 56;
	}

	/**
	 * byte数组转long
	 * 
	 * @param b
	 *            源byte数组
	 * @param offset
	 *            起始索引
	 * @return
	 */
	public static long byte2long(byte b[], int offset) {
		return (long) b[offset + 7] & (long) 255
				| ((long) b[offset + 6] & (long) 255) << 8
				| ((long) b[offset + 5] & (long) 255) << 16
				| ((long) b[offset + 4] & (long) 255) << 24
				| ((long) b[offset + 3] & (long) 255) << 32
				| ((long) b[offset + 2] & (long) 255) << 40
				| ((long) b[offset + 1] & (long) 255) << 48
				| (long) b[offset] << 56;
	}

	/**
	 * 将字符串转化成特定长度的byte[],如果value的长度小于idx,则右补零。 比如
	 * getText(5,"1"),结果为{49,0,0,0,0}; 如果value的长度大于idx,则截取掉一部分。 比如
	 * getText(2,"11111"),结果为{49,49};
	 * 
	 * @param idx
	 *            转化后byte[]的长度
	 * @param value
	 *            要转化的字符串
	 * @return byte[]
	 */
	public static byte[] getBytes(int idx, String value) {
		byte[] b1 = new byte[idx];
		int i = 0;
		if (value != null && !value.equals("")) {
			byte[] b2 = value.getBytes();
			while (i < b2.length && i < idx) {
				b1[i] = b2[i];
				i++;
			}
		}
		while (i < b1.length) {
			b1[i] = 0;
			i++;
		}
		return b1;
	}

	/**
	 * 取字节的16进制字符串
	 * 
	 * @param bs
	 * @return
	 */
	public static String getHexStr(byte bs) {
		String retStr = "";
		if (Integer.toHexString((int) bs).length() > 1) {
			retStr += Integer.toHexString((int) bs).substring(
					Integer.toHexString((int) bs).length() - 2);
		} else {
			retStr += "0"
					+ Integer.toHexString((int) bs).substring(
							Integer.toHexString((int) bs).length() - 1);
		}
		return retStr;
	}

	/**
	 * 取字节数组的16进制字符串
	 * 
	 * @param bs
	 * @return
	 */
	public static String getHexStr(byte[] bs) {
		StringBuffer sb = new StringBuffer("");
		if(bs!=null){
			for (int i = 0; i < bs.length; i++) {
				sb.append(getHexStr(bs[i]));
			}
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 16进制字符串转字节数组
	 */
	public static byte[] getHexByte(String byteStr) {
		if (byteStr.length() % 2 != 0) {
			byteStr = "0" + byteStr;
		}
		byte[] retByte = new byte[byteStr.length() / 2];

		for (int i = 0; i < byteStr.length() / 2; i++) {
			byte[] b = new byte[1];
			b[0] = int2byte(Integer.parseInt(byteStr
					.substring(2 * i, 2 * i + 2), 16))[3];

			retByte[i] = int2byte(Integer.parseInt(byteStr.substring(2 * i,
					2 * i + 2), 16))[3];
		}
		return retByte;
	}
	
	/**
	 * @author zkl
	 * @return zpkzak
	 * @param  前置返回地48域16进制数据
	 * */
	public static void main(String args[]){
		String zpkzakdata = "343345373145374335413445303536463043304537333232313939354344353945433641374138453030324346334533444335434538414446324232323644363941453835453032423344364532354334303432393943434137423841413433";
		byte [] bytes = ByteUtil.getHexByte(zpkzakdata);
		String zpkzak = new String(bytes);
		System.out.println("zpkzak = " + zpkzak);
	}
	
	
	
	/**
	 * 将字节数组按二进制的方式转换为字符串
	 */
	 public static String getBinaryStringFromByteArr(byte [] bitmap)
	 {
		 StringBuffer sb=new StringBuffer("");
		 for (int i = 0; i < bitmap.length; i++) 
		 {
			int decimal=bitmap[i];
			if(decimal<0)
			{
				decimal=(128+decimal)+1+127;
			}
			String binaryString="0000000"+Integer.toBinaryString(decimal);
			sb.append(binaryString.substring(Math.abs(binaryString.length()-8)));
		}
		 return sb.toString();
	 }
}
