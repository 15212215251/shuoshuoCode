package com.untech.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class SignUtil {
    /**
     * 这里用静态块初始化一些加解密用到的东西，可以避免抛出如下的异常
     *  java.lang.NullPointerException
        at org.apache.xml.security.algorithms.JCEMapper.translateURItoJCEID(Unknown Source)
        at org.apache.xml.security.algorithms.MessageDigestAlgorithm.getDigestInstance(Unknown Source)
     */
    static {
        org.apache.xml.security.Init.init();
        try {
            //设置Signature标签的前缀,这个前缀是不影响签名校验结果的。
            Constants.setSignatureSpecNSprefix("ds");
        } catch (XMLSecurityException e) {
            System.err.println("org.apache.xml.security.utils.Constants初始化出错！");
            throw new RuntimeException(e);
        }
    }

    /**
     * 对一段字符串进行签名，返回签名之后的字符串。
     * @param src
     * @return
     * @throws Exception
     */
    public static String sign(String src) throws Exception {
        String RSA_ALGORITHM_NAME = "RSA";
        SignManagerImpl signManager = new SignManagerImpl();
        PrivateKey priKey = KeyUtil.getMOCKPriKey();

        String sign = signManager.sign(src, RSA_ALGORITHM_NAME, priKey);
        return sign;
    }

    /**
     * 对一段签名之后的字符串进行校验，返回校验的结果。
     * @param src
     * @return
     * @throws Exception
     */
    public static boolean check(String src, String sign) throws Exception {
        String RSA_ALGORITHM_NAME = "RSA";
        SignManagerImpl signManager = new SignManagerImpl();
        PublicKey pubKey = KeyUtil.getMOCKPubKey();

        boolean isSuccess = signManager.check(sign, src, RSA_ALGORITHM_NAME, pubKey);
        return isSuccess;
    }

    public static boolean check(Document doc, PublicKey pubKey) {
        try {
            Element nscontext = XMLUtils.createDSctx(doc, "ds", Constants.SignatureSpecNS);
            Element signElement = (Element) XPathAPI.selectSingleNode(doc, "//ds:Signature[1]",
                nscontext);

            if (signElement == null) {
                return false;
            }

            XMLSignature signature = new XMLSignature(signElement, doc.getDocumentURI());
            return signature.checkSignatureValue(pubKey);
        } catch (Exception e) {
            System.out.println("验证签名的时候发生了异常啊!");
            e.printStackTrace();
        }
        return false;
    }

    public static boolean check(String xml, PublicKey pubKey) throws Exception {
        Document doc = DocumentUtil.getDocFromString(xml);
        return check(doc, pubKey);
    }

    /**
     * xml报文签名
     *
     */
    public static String sign(Document doc, PrivateKey privateKey, String msgType) {
        try {

            XMLSignature sig = new XMLSignature(doc, doc.getDocumentURI(),
                XMLSignature.ALGO_ID_SIGNATURE_RSA);
            sig.getSignedInfo().addResourceResolver(new OfflineResolver());

            Node messageNode = doc.getElementsByTagName("Message").item(0);
            messageNode.appendChild(sig.getElement());

            Transforms transforms = new Transforms(doc);

            //            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            //            transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);

            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
             

            sig.addDocument("#" + msgType, transforms, Constants.ALGO_ID_DIGEST_SHA1);

            // 签名

            sig.sign(privateKey);

            // 将签名好的XML文档写出
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            //XMLUtils.outputDOMc14nWithComments(doc, os);
            XMLUtils.outputDOM(doc, os);
            return os.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String xml, PrivateKey privateKey, String msgType) throws Exception {
        Document doc = DocumentUtil.getDocFromString(xml);
        return sign(doc, privateKey, msgType);
    }

	public static String createDirBankRes(String plain,String signature) {
		
		StringBuffer tmp = new StringBuffer();
		tmp.append("<directBank>")
			.append(plain)
			.append("<secret>")
			.append("<signType>")
			.append("2")
			.append("</signType>")
			.append("<signMsg>")
			.append(signature)
			.append("</signMsg>")
			.append("</secret>")
			.append("</directBank>");
		
		return tmp.toString();
	}
	
	/**
     * 对文件内容做验签
     * @param xml
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static boolean checkFileSignature(String xml, PublicKey pubKey, String filepath) throws Exception {
    	Document resdoc = DocumentUtil.getDocFromString(xml);
		String filename =resdoc.getElementsByTagName("fileName").item(0).getTextContent();
		String merchantId =resdoc.getElementsByTagName("merchantId").item(0).getTextContent();
		String signStr =resdoc.getElementsByTagName("signature").item(0).getTextContent();
		byte signByte[] =Hex2Byte.hex2Byte(signStr); 
		if("".equals(filename)){
			return false;
		}
		//读取源文件
		filepath = filepath.endsWith("/") ? filepath : filepath+"/";
		FileInputStream fis = new FileInputStream(filepath + merchantId + "/" + filename);
		byte content[] = new byte[fis.available()];
		fis.read(content);
		fis.close();
		
		//对文件内容作摘要
		MessageDigest messageDigest = MessageDigest.getInstance("sha1");
		messageDigest.update(content);
		byte digest[] = new String(Hex.encodeHex(messageDigest.digest())).toUpperCase().getBytes();
		
		//该方法取的摘要信息也可以用作验签来用-不建议使用
		//byte digest[] =resdoc.getElementsByTagName("digest").item(0).getTextContent().getBytes();
		
		//验签
		Signature signature = Signature.getInstance("sha1withRSA");
		signature.initVerify(pubKey);
		signature.update(digest);
		return signature.verify(signByte);
    }
}
