package com.dstz.sys.sdk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.sys.rest.model.dto.AutoTranslateDTO;
import com.dstz.sys.rest.model.vo.AutoTranslateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Baidu translation client
 *
 */
public class BaiduFanYiClient {

	private static final Logger logger = LoggerFactory.getLogger(BaiduFanYiClient.class);

	private static final String TRANS_API = "https://fanyi-api.baidu.com/api/trans/vip/translate";

	/**
	 * Control concurrent number 100
	 */
	private static final Semaphore SEMAPHORE = new Semaphore(100);


	/**
	 * Translate multiple languages
	 *
	 * @param translateDTO translateDTO
	 * @return Translation results
	 */
	public static List<AutoTranslateVO> translateMulti(AutoTranslateDTO translateDTO) throws InterruptedException {
		if (CollUtil.isEmpty(translateDTO.getToLanguages())) {
			return new ArrayList<>();
		}

		// Get the parameters used
		final String appId = PropertyEnum.BAIDU_SDK_APPID.getPropertyValue(String.class);
		Assert.notBlank(appId, "System property [baidu.sdk.app-id] is not configured");
		final String secret = PropertyEnum.BAIDU_SDK_SECRET.getPropertyValue(String.class);
		Assert.notBlank(secret, "System property [baidu.sdk.secret] is not configured");

		List<AutoTranslateVO> translateVoList = new ArrayList<>(translateDTO.getToLanguages().size());
		CountDownLatch countDownLatch = new CountDownLatch(translateDTO.getToLanguages().size());

		for (String toLanguage : translateDTO.getToLanguages()) {
			AutoTranslateVO autoTranslateVO = new AutoTranslateVO();
			autoTranslateVO.setLanguage(toLanguage);
			// Control concurrency 100
			SEMAPHORE.acquire();
			GlobalThreadPool.execute(() -> {
				try {
					String dstText = invokeApi(autoTranslateVO.getLanguage(), buildParams(appId, secret, translateDTO.getChineseText(), autoTranslateVO.getLanguage()));
					autoTranslateVO.setDstText(dstText);
				} catch (FanYiException e) {
					autoTranslateVO.setErrorMsg(e.getMessage());
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
					autoTranslateVO.setErrorMsg("Translation failed, please check the background log");
				} finally {
					SEMAPHORE.release();
					countDownLatch.countDown();
				}
			});
			translateVoList.add(autoTranslateVO);
		}
		countDownLatch.await();
		return translateVoList;
	}


	public String translate(String chineseText, String toLanguage) {
		// Get the parameters used
		final String appId = PropertyEnum.BAIDU_SDK_APPID.getYamlValue(String.class);
		Assert.notBlank(appId, "System property [baidu.sdk.app-id] is not configured");
		final String secret = PropertyEnum.BAIDU_SDK_SECRET.getYamlValue(String.class);
		Assert.notBlank(secret, "System property [baidu.sdk.secret] is not configured");

		Map<String, Object> params = buildParams(appId, secret, chineseText, toLanguage);

		return invokeApi(toLanguage, params);
	}

	/**
	 * Calling interface
	 *
	 * @param params Interface parameters
	 * @return Translated content
	 */
	private static String invokeApi(String toLanguage, Map<String, Object> params) {
		StopWatch stopWatch = null;
		if (logger.isInfoEnabled()) {
			stopWatch = new StopWatch();
			stopWatch.start();
			logger.info("Call Baidu Translate request parameters: {}", JsonUtils.toJSONString(params));
		}
		Map<String, Object> responseMap;
		try (HttpResponse response = HttpUtil.createPost(TRANS_API)
				.contentType("application/x-www-form-urlencoded;charset=UTF-8")
				.form(params)
				.timeout(100000)
				.executeAsync()) {
			String bodyString = response.body();
			if(logger.isInfoEnabled()) {
				stopWatch.stop();
				logger.info("Call Baidu Translate request response, time consuming:{}, stateCode: {}, body：{}", stopWatch.shortSummary(TimeUnit.MILLISECONDS), response.getStatus(), bodyString);
			}
			Assert.isTrue(response.getStatus() == 200, "Baidu translation service did not return data normally, status code: {}, response data: {}", response.getStatus(), bodyString);
			responseMap = CastUtils.cast(JsonUtils.parseObject(bodyString, Map.class));
		}
		if (responseMap == null || responseMap.isEmpty()) {
			throw new IllegalStateException("The Baidu translation service did not return data normally!");
		}
		handleErrorCode(toLanguage, MapUtil.getStr(responseMap, "error_code"), MapUtil.getStr(responseMap, "error_msg"));
		Map<String, String> transResult = CollUtil.getFirst(CastUtils.<List<Map<String, String>>>cast(responseMap.get("trans_result")));
		return transResult.get("dst");
	}

	private static void handleErrorCode(String toLanguage, String errorCode, String errorMsg) {
		// Empty or 52000 indicates success
		if (CharSequenceUtil.isEmpty(errorCode) || CharSequenceUtil.equals(errorCode, "52000")) {
			return;
		}
		// Access frequency is limited
		if (CharSequenceUtil.equals(errorCode, "54003")) {
			throw new FanYiException("The call frequency is too high, please try again later");
		}
		// The translation language direction is not supported
		if (CharSequenceUtil.equals(errorCode, "58001")) {
			throw new FanYiException(String.format("Language translation not supported: %s", toLanguage));
		}
		throw new IllegalStateException(String.format("An error occurred while calling Baidu Translate, error code: %s, error message: %s", errorCode, errorMsg));
	}

	private static String convertLanguage(String language) {
		if (CharSequenceUtil.equalsAnyIgnoreCase(language, "zh-CHT", "zh-TW")) {
			// Simplified Chinese
			return "cht";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "en-")) {
			// English
			return "en";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "de-")) {
			// German
			return "de";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "ja-")) {
			// Japanese
			return "jp";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "fr-")) {
			// French
			return "fra";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "ru-")) {
			// Russian
			return "ru";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "pt-")) {
			// Portuguese
			return "pt";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "es-")) {
			// Spanish
			return "spa";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "ar-")) {
			// Arabic
			return "ara";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "nl-")) {
			// Dutch
			return "nl";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "th-")) {
			// Thai
			return "th";
		} else if (CharSequenceUtil.startWithIgnoreCase(language, "vi-")) {
			// Vietnamese
			return "vie";
		}
		logger.warn("Unable to map language [{}] to Baidu Translate language\n"
				+ "Unable to map language [{}] to Baidu Translate language", language);
		throw new FanYiException(String.format("Unsupported translation language: %s", language));
	}

	private static Map<String, Object> buildParams(String appId, String secret, String chineseText, String toLanguage) {
		final String salt = Long.toString(System.currentTimeMillis());

		Map<String, Object> params = MapUtil.newHashMap();
		params.put("q", chineseText);
		params.put("from", "zh");
		params.put("to", convertLanguage(toLanguage));
		params.put("appid", appId);
		params.put("salt", salt);
		params.put("sign", SecureUtil.md5(CharSequenceUtil.concat(true, appId, chineseText, salt, secret)));
		return params;
	}

}
