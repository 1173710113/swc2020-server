package com.example.demo.domain;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.CorrespondUtil;
import com.example.demo.utils.configReader.Configreader;
import com.example.demo.utils.inputstrategy.InputStrategy;

/**
 * 将时间抽取服务的结果进一步整合
 * 
 * @author xjy
 *
 */
public class TimeExtractResult {

	private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<String> analyzeList = new ArrayList<>();
	
	private List<Date> dateList = new ArrayList<>();

	/**
	 * 根据时间抽取服务的结果, 获取一个本类对象
	 * 
	 * @param jsonObject 时间抽取服务的结果
	 * @throws ParseException
	 */
	public TimeExtractResult(JSONObject jsonObject) throws ParseException {
		JSONObject analyze = jsonObject.getJSONObject("analyze");
//		System.out.println(js.size());
		for (int i = 0; i < jsonObject.size() - 1; i++) {
			JSONObject oneAnalyze = jsonObject.getJSONObject(i + "");
			JSONObject result = oneAnalyze.getJSONObject("result");

			Date latestTime = new Date(0);
			for (int j = 0; j < result.size(); j++) {
				JSONObject oneTime = result.getJSONObject(j + "");
				JSONObject resolution = oneTime.getJSONObject("resolution");
				Date value, end;
				switch (resolution.getString("type")) {
				case "date":
					value = date.parse(resolution.getString("value"));
					if (value.compareTo(latestTime) > 0) {
						latestTime = value;
					}
					break;
				case "daterange":
					end = date.parse(resolution.getString("end"));
					if (end.compareTo(latestTime) > 0) {
						latestTime = end;
					}
					break;
				case "datetime":
					value = dateTime.parse(resolution.getString("value"));
					if (value.compareTo(latestTime) > 0) {
						latestTime = value;
					}
					break;
				case "datetimerange":
					end = dateTime.parse(resolution.getString("end"));
					if (end.compareTo(latestTime) > 0) {
						latestTime = end;
					}
					break;
				case "time":
					value = dateTime.parse(date.format(new Date()).toString() + " " + resolution.getString("value"));
					if (value.compareTo(latestTime) > 0) {
						latestTime = value;
					}
					break;
				case "timerange":
					end = dateTime.parse(date.format(new Date()).toString() + " " + resolution.getString("end"));
					if (end.compareTo(latestTime) > 0) {
						latestTime = end;
					}
					break;
				default:
				}
			}
			analyzeList.add(analyze.getString("" + i));
			dateList.add(latestTime);

		}

	}

	/**
	 * 获取第i个analyze的日期(从0开始)
	 * 
	 * @param numOfAnalyze
	 * @return
	 */
	public Date getDate(int numOfAnalyze) {
		return dateList.get(numOfAnalyze);
	}

	/**
	 * 获取第i个analyze的字符串(从0开始)
	 * 
	 * @param numOfAnalyze
	 * @return
	 */
	public String getAnalyze(int numOfAnalyze) {
		return analyzeList.get(numOfAnalyze);
	}

	public int getNumOfAnalyze() {
		return analyzeList.size();
	}

	public static void main(String[] args)
			throws NumberFormatException, UnknownHostException, IOException, ParseException {
		Configreader config = Configreader.reader("config.xml");
		CorrespondUtil corres = new CorrespondUtil(InetAddress.getByName(config.readItem("DateExtractorClient")),
				InetAddress.getByName(config.readItem("DateExtractorServer")),
				Integer.parseInt(config.readItem("DateExtractorServerPort")),
				Integer.parseInt(config.readItem("DateExtractorClientPort")));
		InputStrategy inputStrategy = InputStrategy.input(new File("../syntaxTest.txt"));
		List<String> list = new ArrayList<String>();
		String tmp;
		while ((tmp = inputStrategy.nextLine()) != null) {
			list.add(tmp);
		}
		JSONObject obj = corres.sendJson(list);

		TimeExtractResult ter = new TimeExtractResult(obj);

		for (int i = 0; i < ter.getNumOfAnalyze(); i++) {
			System.out.println("sentence = " + ter.getAnalyze(i));
			System.out.println("Date = " + ter.getDate(i));
		}

	}
}
