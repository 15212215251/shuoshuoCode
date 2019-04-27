package com.untech.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.untech.jpush.JPushObject;
import com.untech.service.OrderService;
import com.untech.util.EncodeDecodeUtil;
import com.untech.util.GenerateQrCodeUtil;
import com.untech.util.OrderId;
import com.untech.util.RandomId;
import com.untech.util.RandomUtil;
import com.untech.util.RedisUtil;
import com.untech.util.ReturnUtil;
import com.untech.util.StringUtil;

/**
 * 订单Controller
 * 
 * @author wuxw
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Scope("prototype")
@Controller
public class OrderController {

	/** 日志实例 */
	private static final Logger logger = Logger.getLogger(OrderController.class
			.getName());
	private JSONObject jsonObject = new JSONObject();

	@Autowired
	private OrderService orderService;

	/**
	 * 生成交易号
	 * 
	 * @param map
	 * @return id=949494674dc2704f014dc27197130000, DeviceId=865072024523659,
	 *         sid=865072024523659, VERSION_RELEASE=4.4.4, MODEL=MI 3W,
	 *         CPU_ABI=armeabi, encrypt=111111, ANDROID_ID=3c7a44aa7948b073,
	 *         MANUFACTURER=Xiaomi, MAC=64:b4:73:2a:6c:4b, type=android,
	 *         key=12345678, SDK=19
	 */
	@RequestMapping(value = "/bornTransactionNo")
	@ResponseBody
	public Object bornTransactionNo(@RequestBody Map<String, Object> map) {
		logger.info("bornTransactionNo start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		logger.info("请求交易号报文:" + map);
		System.out.println("请求交易号报文=>" + map);

		if (StringUtil.isEmpty(map.get("id"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "用户ID为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("sid"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "手机ID为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("type"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "手机类型为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}

		String transactionNo = RandomId.getOrderNo();
		Map<String, String> redisMap = new HashMap<String, String>();
		redisMap.put("id", map.get("id") + "");
		redisMap.put("sid", map.get("sid") + "");
		redisMap.put("type", map.get("type") + "");
		redisMap.put("transactionNo", transactionNo);
		// 手机的配置信息
		redisMap.put("DeviceId", map.get("DeviceId") + "");
		redisMap.put("VERSION_RELEASE", map.get("VERSION_RELEASE") + "");
		redisMap.put("MODEL", map.get("MODEL") + "");
		redisMap.put("CPU_ABI", map.get("CPU_ABI") + "");
		redisMap.put("ANDROID_ID", map.get("ANDROID_ID") + "");
		redisMap.put("MANUFACTURER", map.get("MANUFACTURER") + "");
		redisMap.put("MAC", map.get("MAC") + "");
		redisMap.put("SDK", map.get("SDK") + "");

		boolean returnFlag = RedisUtil.setRedis(redisMap);
		if (returnFlag) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "交易号请求成功");
		} else {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "交易号请求失败");
		}
		jsonObject.put("transactionNo", transactionNo);
		System.out.println("响应请求交易号报文=>" + jsonObject);
		logger.info("响应请求交易号报文=>" + jsonObject);
		return ReturnUtil.getReturn(jsonObject, key);

	}

	/**
	 * 售货机发起订单生成接口
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/bornOrder")
	@ResponseBody
	public Object bornOrder(@RequestBody Map<String, Object> map) {
		logger.info("bornOrder start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		logger.info("售货机发起订单生成报文:" + map);
		System.out.println("售货机发起订单生成报文=>" + map);

		if (StringUtil.isEmpty(map.get("transactionNo"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "交易号为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("androidId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "安卓主机ID为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("mid"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "售货机ID为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("detail"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单详情为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("payamt"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "总金额为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}

		String transactionNo = map.get("transactionNo").toString();
		Map<String, String> redisMap = RedisUtil.getRedis(transactionNo);
		if (redisMap == null) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "获取交易号信息失败");
		} else if (redisMap.get("id") == null) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "交易号已失效，请重新购买");

		} else {
			String orderId = OrderId.getOrderNo(); // 生成唯一编号
			map.put("orderId", orderId);
			map.put("userId", redisMap.get("id"));
			map.put("type", redisMap.get("type"));
			map.put("accountno", null == redisMap.get("MANUFACTURER") ? ""
					: redisMap.get("MANUFACTURER"));
			map.put("mobileinfo", redisMap.toString());
			if (orderService.insertOrder(map)) {
				System.out.println("111111111"+map);
				jsonObject.put("orderId", map.get("orderId").toString());
				// 向android手机推送交易详情,目前使用第一种，只推送订单号
				// Map<String,Object> pushMap = new HashMap<String, Object>();
				// pushMap.put("orderId", orderId);
				// boolean flag = JPushObject.sendCustomerByAlias("",
				// redisMap.get("type"),
				// JSONObject.fromObject(pushMap).toString(),
				// redisMap.get("id"));
				// 推送详情
				boolean flag = JPushObject.sendCustomerByAlias(
						"",
						redisMap.get("type"),
						JSONObject.fromObject(
								orderService.queryOrderDetail(map)).toString(),
						redisMap.get("id"));
				if (flag) {
					jsonObject.put("resCode", "1001");
					jsonObject.put("resMsg", "售货机发起订单生成成功");
				} else {
					jsonObject.put("resCode", "1002");
					jsonObject.put("resMsg", "推送订单失败");
				}

			} else {
				jsonObject.put("resCode", "1002");
				jsonObject.put("resMsg", "售货机发起订单生成失败，插入数据库错误");
			}
		}
		System.out.println();
		logger.info("响应售货机发起订单生成报文=>" + jsonObject);
		System.out.println("响应售货机发起订单生成报文=>" + jsonObject);
		return ReturnUtil.getReturn(jsonObject, key);

	}

	/**
	 * 根据(交易号)订单号获取订单信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryOrderDetail")
	@ResponseBody
	public Object queryOrderDetail(@RequestBody Map<String, Object> map) {
		logger.info("queryOrderDetail start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			return jsonObject;
		}
		logger.info("根据(交易号)订单号获取订单信息请求报文:" + map);
		System.out.println("根据(交易号)订单号获取订单信息请求报文=>" + map);

		if (StringUtil.isEmpty(map.get("orderId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单号为空");
			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			System.out.println("响应根据(交易号)订单号获取订单信息报文=>" + jsonObject);
			logger.info("响应根据(交易号)订单号获取订单信息报文=>" + jsonObject);
			return jsonObject;
		}

		map = orderService.queryOrderDetail(map);
		if (null == map || map.isEmpty()) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "没有该订单号的订单信息");
		} else {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "根据(交易号)订单号获取订单信息成功");
		}
		jsonObject.put("data", map);
		jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);

		System.out.println("响应根据(交易号)订单号获取订单信息报文=>" + jsonObject);
		logger.info("响应根据(交易号)订单号获取订单信息报文=>" + jsonObject);
		return jsonObject;

	}

	/**
	 * 银行卡列表和电子钱包金额查询接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryBankcardList")
	@ResponseBody
	public Object queryBankcardList(@RequestBody Map<String, Object> map) {
		logger.info("queryBankcardList start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			return jsonObject;
		}

		logger.info("银行卡列表和电子钱包金额查询请求报文:" + map);
		System.out.println("银行卡列表和电子钱包金额查询请求报文=>" + map);

		if (StringUtil.isEmpty(map.get("id"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "用户ID为空");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			System.out.println("响应银行卡列表和电子钱包金额查询报文=>" + jsonObject);
			logger.info("响应银行卡列表和电子钱包金额查询报文=>" + jsonObject);
			return jsonObject;
		}

		Map returnMap = orderService.queryBankcardList(map);
		// returnMap.put("key", map.get("key").toString());
		// returnMap.put("encrypt", map.get("encrypt").toString());

		jsonObject.put("resCode", "1001");
		jsonObject.put("resMsg", "根银行卡列表和电子钱包金额查询成功");
		jsonObject.put("data", returnMap);
		jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);

		System.out.println("响应银行卡列表和电子钱包金额查询报文=>" + jsonObject);
		logger.info("响应银行卡列表和电子钱包金额查询报文=>" + jsonObject);
		return jsonObject;
	}

	/**
	 * 安卓主板支付成功或失败的回掉接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pushOrderResult")
	@ResponseBody
	public Object pushOrderResult(@RequestBody Map<String, Object> map) {
		logger.info("pushOrderResult start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			return jsonObject;
		}

		logger.info("安卓主板支付成功或失败的回掉接口请求报文:" + map);
		System.out.println("安卓主板支付成功或失败的回掉接口请求报文=>" + map);

		if (StringUtil.isEmpty(map.get("androidId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "安卓主板ID为空");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			System.out.println("响应安卓主板支付成功或失败的回掉接口报文=>" + jsonObject);
			logger.info("响应安卓主板支付成功或失败的回掉接口报文=>" + jsonObject);
			return jsonObject;
		}
		if (StringUtil.isEmpty(map.get("orderId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单号为空");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			System.out.println("响应安卓主板支付成功或失败的回掉接口报文=>" + jsonObject);
			logger.info("响应安卓主板支付成功或失败的回掉接口报文=>" + jsonObject);
			return jsonObject;
		}
		if (StringUtil.isEmpty(map.get("status"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单状态为空");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			System.out.println("响应安卓主板支付成功或失败的回掉接口报文=>" + jsonObject);
			logger.info("响应安卓主板支付成功或失败的回掉接口报文=>" + jsonObject);
			return jsonObject;
		}

		if (orderService.pushOrderResult(map)) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "安卓主板支付成功或失败的回掉接口成功");
		} else {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "安卓主板支付成功或失败的回掉接口失败");
		}
		jsonObject.put("data", "");
		jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);

		System.out.println("响应安卓主板支付成功或失败的回掉接口报文=>" + jsonObject);
		logger.info("响应安卓主板支付成功或失败的回掉接口报文=>" + jsonObject);
		return jsonObject;
	}

	/**
	 * 安卓主板推送吐货结果接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pushShipmentResult")
	@ResponseBody
	public Object pushShipmentResult(@RequestBody Map<String, Object> map) {
		logger.info("pushShipmentResult start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			return jsonObject;
		}

		logger.info("安卓主板推送吐货结果接口请求报文:" + map);
		System.out.println("安卓主板推送吐货结果接口请求报文=>" + map);

		if (StringUtil.isEmpty(map.get("androidId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "安卓主板ID为空");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			System.out.println("响应安卓主板推送吐货结果接口报文=>" + jsonObject);
			logger.info("响应安卓主板推送吐货结果接口报文=>" + jsonObject);
			return jsonObject;
		}
		if (StringUtil.isEmpty(map.get("orderId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单号为空");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			System.out.println("响应安卓主板推送吐货结果接口报文=>" + jsonObject);
			logger.info("响应安卓主板推送吐货结果接口报文=>" + jsonObject);
			return jsonObject;
		}
		if (StringUtil.isEmpty(map.get("status"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单状态为空");

			jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
			System.out.println("响应安卓主板推送吐货结果接口报文=>" + jsonObject);
			logger.info("响应安卓主板推送吐货结果接口报文=>" + jsonObject);
			return jsonObject;
		}

		if (orderService.pushOrderResult(map)) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "安卓主板推送吐货结果接口成功");
		} else {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "安卓主板推送吐货结果接口失败");
		}
		jsonObject.put("data", "");

		jsonObject = (JSONObject) ReturnUtil.getReturn(jsonObject, key);
		// 推送支付成功
		Map m = orderService.queryOrderDetail(map);
		String amt = m.get("TOTAL").toString();
		logger.info("start  push pushShipmentResult ");
		boolean flag = false;
		if ("2004".equals(map.get("status").toString())) {
			flag = JPushObject.sendMoneyChange(m.get("USERID").toString(),
					"您好，您通过\"我家亳州\"APP上购买饮料成功，消费金额为：" + amt + "，欢迎再次使用！", m
							.get("MOBILETYPE").toString());
		} else {
			flag = JPushObject.sendMoneyChange(m.get("USERID").toString(),
					"您好，您通过\"我家亳州\"APP上购买饮料失败，请联系客服！", m.get("MOBILETYPE")
							.toString());
		}
		if (flag) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "吐货状态更新成功");
		} else {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "吐货状态更新失败");
		}

		System.out.println("响应安卓主板推送吐货结果接口报文=>" + jsonObject);
		logger.info("响应安卓主板推送吐货结果接口报文=>" + jsonObject);

		return jsonObject;
	}

	/**
	 * 银行卡和电子钱包支付接口
	 * 
	 * @return
	 */
	@RequestMapping(value = "/payment")
	@ResponseBody
	public Object payment(@RequestBody Map<String, Object> map) {
		logger.info("payment start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		logger.info("银行卡和电子钱包支付接口请求报文:" + map);
		System.out.println("银行卡和电子钱包支付接口请求报文=>" + map);

		if (StringUtil.isEmpty(map.get("id"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "用户ID为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单号为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("totalamt"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单总金额为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}

		if (StringUtil.isEmpty(map.get("paytype"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "支付类型为空");
			return ReturnUtil.getReturn(jsonObject, key);
		} else {
			if (map.get("paytype").toString().equals("02")) {// 余额支付
				if (StringUtil.isEmpty(map.get("payMoney"))) {
					jsonObject.put("resCode", "1002");
					jsonObject.put("resMsg", "余额支付金额为空");
					return ReturnUtil.getReturn(jsonObject, key);
				}

			} else if (map.get("paytype").toString().equals("01")) {
				if (StringUtil.isEmpty(map.get("cardNo"))) {
					jsonObject.put("resCode", "1002");
					jsonObject.put("resMsg", "卡号为空");
					return ReturnUtil.getReturn(jsonObject, key);
				}
				if (StringUtil.isEmpty(map.get("mobile"))) {
					jsonObject.put("resCode", "1002");
					jsonObject.put("resMsg", "手机号码为空");
					return ReturnUtil.getReturn(jsonObject, key);
				}
				if (StringUtil.isEmpty(map.get("cardMoney"))) {
					jsonObject.put("resCode", "1002");
					jsonObject.put("resMsg", "卡支付金额为空");
					return ReturnUtil.getReturn(jsonObject, key);
				}
			} else {
				jsonObject.put("resCode", "1002");
				jsonObject.put("resMsg", "支付类型错误");
				return ReturnUtil.getReturn(jsonObject, key);
			}
		}
		if (StringUtil.isEmpty(map.get("payEncrypt"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "支付密码为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		BigDecimal totalamt = new BigDecimal(map.get("totalamt") + "");
		BigDecimal payMoney = new BigDecimal(map.get("payMoney") + "");
		BigDecimal cardMoney = new BigDecimal(map.get("cardMoney") + "");
		if (payMoney.add(cardMoney).compareTo(totalamt) != 0) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "支付金额参数不符");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		// 支付
		try {
			Map payMap = orderService.queryOrderDetail(map);
			if (payMap.get("FLAG").toString().equals("0")) {
				if (map.get("paytype").toString().equals("01")) {// 银行卡支付
					if (totalamt.compareTo(cardMoney) != 0) {
						jsonObject.put("resCode", "1002");
						jsonObject.put("resMsg", "银行卡金额参数不符");
						return ReturnUtil.getReturn(jsonObject, key);
					}
					jsonObject = (JSONObject) orderService.cardPayment(map);
				} else if (map.get("paytype").toString().equals("02")) {// 余额支付
					if (totalamt.compareTo(payMoney) != 0) {
						jsonObject.put("resCode", "1002");
						jsonObject.put("resMsg", "电子钱包金额参数不符");
						return ReturnUtil.getReturn(jsonObject, key);
					}
					jsonObject = (JSONObject) orderService.balancePayment(map);
				}
			} else {
				jsonObject.put("resCode", "1002");
				jsonObject.put("resMsg", "该订单已支付");
				return ReturnUtil.getReturn(jsonObject, key);
			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "支付异常");
		}

		jsonObject.put("data", "");
		return ReturnUtil.getReturn(jsonObject, key);
	}

	/**
	 * 退费
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/returnFee")
	@ResponseBody
	public Object returnFee(@RequestBody Map<String, Object> map) {
		logger.info("returnFee start :" + map);
		String key = RandomUtil.generateNumber(16);
		// 加解密处理
		// map = EncodeDecodeUtil.decrypt(map);
		// if(null == map){
		// jsonObject.put("resCode", "1002");
		// jsonObject.put("resMsg", "解密验证失败");
		// return ReturnUtil.getReturn(jsonObject, key);
		// }
		logger.info("退费接口请求报文:" + map);
		System.out.println("退费接口请求报文=>" + map);

		if (StringUtil.isEmpty(map.get("orderId"))
				|| StringUtil.isEmpty(map.get("orderId").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[orderId]");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType")) || StringUtil.isEmpty(map.get("feeType").toString().trim())) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "参数缺失[feeType]");
			return ReturnUtil.getReturn(jsonObject, key);
		}

		jsonObject = (JSONObject) orderService.backFee(map);
		if ("9999".equals(jsonObject.get("resCode"))) {
			map = orderService.queryOrderDetail(map);
			map.put("oldOrderId", map.get("OLDORDERID").toString());
			map.put("totalAmt", map.get("TOTALAMT").toString());
			if ("01".equals(map.get("PAYTYPE").toString().trim())) {
				jsonObject = (JSONObject) orderService.backCardMain(map);
			} else if ("02".equals(map.get("PAYTYPE").toString().trim())) {
				jsonObject = (JSONObject) orderService.backBalanceMain(map);
			}
		}
		jsonObject.put("data", "");
		return ReturnUtil.getReturn(jsonObject, key);
	}

	@RequestMapping(value = "/cancelOrderNo")
	@ResponseBody
	public Object cancelOrderNo(@RequestBody Map<String, Object> map) {
		logger.info("cancelOrderNo start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		logger.info("请求取消订单报文:" + map);
		System.out.println("请求取消订单报文=>" + map);

		if (StringUtil.isEmpty(map.get("id"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "用户ID为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("orderId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单号为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("channel"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "取消订单渠道为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}

		int cnt = orderService.cancelOrder(map);

		if ("01".equals(map.get("channel").toString())) {
			Map ms = orderService.queryOrderDetail(map);
			Map<String, Object> pushMap = new HashMap<String, Object>();
			pushMap.put("ANDROIDID", ms.get("ANDROIDID").toString());
			pushMap.put("status", "2006");
			logger.info(" cardPaymentstart push --------------------" + pushMap);
			boolean flag = JPushObject.sendCustomerByAlias("", "android",
					JSONObject.fromObject(pushMap).toString(),
					ms.get("ANDROIDID").toString());
			if (flag && cnt == 1) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "取消订单成功");
			} else {
				jsonObject.put("resCode", "1002");
				jsonObject.put("resMsg", "取消订单失败");
			}
		} else {
			if (cnt == 1) {
				jsonObject.put("resCode", "1001");
				jsonObject.put("resMsg", "取消订单成功");
			} else {
				jsonObject.put("resCode", "1002");
				jsonObject.put("resMsg", "取消订单失败");
			}
		}
		return ReturnUtil.getReturn(jsonObject, key);

	}
	
	
	/**
	 * 售货机发起二维码生成，返回url信息
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/bornQrCode")
	@ResponseBody
	public Object bornQrCode(@RequestBody Map<String, Object> map) {
		logger.info("bornQrCode start :" + map);
		String key = EncodeDecodeUtil.getRandomKey();
		// 加解密处理
		map = EncodeDecodeUtil.decrypt(map);
		if (null == map) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "解密验证失败");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		logger.info("售货机发起二维码生成报文:" + map);
		System.out.println("售货机发起二维码生成报文=>" + map);
		if (StringUtil.isEmpty(map.get("androidId"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "安卓主机ID为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("mid"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "售货机ID为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("detail"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单详情为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("payamt"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "总金额为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("feeType"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "费用类型为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		if (StringUtil.isEmpty(map.get("flag"))) {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "订单初始状态为空");
			return ReturnUtil.getReturn(jsonObject, key);
		}
		Map<String,String> orderIdMap = OrderId.getQrCodeUrl(); // 生成唯一编号
		logger.info("bornQrCode orderIdMap :" + orderIdMap);
		map.put("orderId", orderIdMap.get("orderid"));
		map.put("type", "android");
		map.put("mobileinfo",orderIdMap.get("qrcodeurl") );
		if (orderService.insertQrCodeOrder(map)) {
			jsonObject.put("resCode", "1001");
			jsonObject.put("resMsg", "生成二维码url成功");
			jsonObject.put("orderId", orderIdMap.get("orderid"));
			jsonObject.put("qrcodeurl", orderIdMap.get("qrcodeurl").toString());
		} else {
			jsonObject.put("resCode", "1002");
			jsonObject.put("resMsg", "生成二维码url失败");
		}
		System.out.println();
		logger.info("售货机发起二维码生成报文=>" + jsonObject);
		return ReturnUtil.getReturn(jsonObject, key);

	}
	
	
	  @RequestMapping(value = "/assignCode")
	  @ResponseBody
	  public void assignCode(HttpServletRequest request, HttpServletResponse response ){
		 // HttpServletRequest request = ServletActionContext.getRequest();
		//  HttpServletResponse response = ServletActionContext.getResponse();
		  System.out.println("111111111111111");
		  String  code_url =  request.getParameter("code_url");
	      GenerateQrCodeUtil.encodeQrcode(code_url, response);
	     
	  }
		
	
}
