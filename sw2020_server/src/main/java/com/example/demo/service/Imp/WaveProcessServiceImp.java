package com.example.demo.service.Imp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.AlignSentence;
import com.example.demo.domain.Schedule;
import com.example.demo.config.ScheduleExtractorConfiguration;
import com.example.demo.dao.AlignSentenceMapper;
import com.example.demo.dao.ScheduleMapper;
import com.example.demo.exception.TransferException;
import com.example.demo.service.WaveProcessService;
import com.example.demo.utils.CorrespondUtil;
import com.example.demo.utils.SyntaxAnalyzer;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;
import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WaveProcessServiceImp implements WaveProcessService {

	@Autowired
	private AlignSentenceMapper alignSentenceMapper;

	@Autowired
	private ScheduleExtractorConfiguration scheduleExtractorConfiguration;
	
	@Autowired
	private ScheduleMapper scheduleMapper;
	
	@Override
	public String waveConvertToText(String filePath) throws TransferException {
		final LfasrType type = LfasrType.LFASR_STANDARD_RECORDED_AUDIO;
		// 等待时长（秒）
		final int sleepSecond = 20;
		// 初始化LFASRClient实例
		LfasrClientImp lc = null;
		try {
			lc = LfasrClientImp.initLfasrClient("5e9d9785", "592188a4f3196b42f1d4864f01375891");
		} catch (LfasrException e) {
			// 初始化异常，解析异常描述信息
			Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
			throw new TransferException("unknown", initMsg.getErr_no(), initMsg.getFailed());
		}

		// 获取上传任务ID
		String task_id = "";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("has_participle", "true"); // 是否分词
		params.put("has_seperate", "false");
		try {
			// 上传音频文件
			Message uploadMsg = lc.lfasrUpload(filePath, type, params);

			// 判断返回值
			int ok = uploadMsg.getOk();
			if (ok == 0) {
				// 创建任务成功
				task_id = uploadMsg.getData();
				System.out.println("task_id=" + task_id);
			} else {
				// 创建任务失败-服务端异常
				throw new TransferException("unknown", uploadMsg.getErr_no(), uploadMsg.getFailed());
			}
		} catch (LfasrException e) {
			// 上传异常，解析异常描述信息
			Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
			throw new TransferException("unknown", uploadMsg.getErr_no(), uploadMsg.getFailed());
		}

		// 循环等待音频处理结果
		while (true) {
			try {
				// 等待20s在获取任务进度
				Thread.sleep(sleepSecond * 1000);
				System.out.println("waiting ...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				// 获取处理进度
				Message progressMsg = lc.lfasrGetProgress(task_id);

				// 如果返回状态不等于0，则任务失败
				if (progressMsg.getOk() != 0) {

					throw new TransferException(task_id, progressMsg.getErr_no(), progressMsg.getFailed());
				} else {
					ProgressStatus progressStatus = JSON.parseObject(progressMsg.getData(), ProgressStatus.class);
					if (progressStatus.getStatus() == 9) {
						// 处理完成
						System.out.println("task was completed. task_id:" + task_id);
						break;
					} else {
						// 未处理完成
						System.out.println(
								"task is incomplete. task_id:" + task_id + ", status:" + progressStatus.getDesc());
						continue;
					}
				}
			} catch (LfasrException e) {
				// 获取进度异常处理，根据返回信息排查问题后，再次进行获取
				Message progressMsg = JSON.parseObject(e.getMessage(), Message.class);
				throw new TransferException(task_id, progressMsg.getErr_no(), progressMsg.getFailed());
			}
		}
		// 获取任务结果
		try {
			Message resultMsg = lc.lfasrGetResult(task_id);
			// 如果返回状态等于0，则获取任务结果成功
			if (resultMsg.getOk() == 0) {
				// 打印转写结果
				return resultMsg.getData();
			} else {
				// 获取任务结果失败
				throw new TransferException(task_id, resultMsg.getErr_no(), resultMsg.getFailed());
			}
		} catch (LfasrException e) {
			// 获取结果异常处理，解析异常描述信息
			Message resultMsg = JSON.parseObject(e.getMessage(), Message.class);
			throw new TransferException(task_id, resultMsg.getErr_no(), resultMsg.getFailed());
		}
	}

	@Override
	public void extractAlignResult(String filePath, String text, String classId) {

		StringBuilder textBf = new StringBuilder();
		// 匹配每句话的正则表达式
		Pattern pattern = Pattern
				.compile("\"bg\":\"(\\d+)\",\"ed\":\"(\\d+)\",\"onebest\":\"(.*?)\",\"speaker\":\"(\\d+)\""
						+ ",\"wordsResultList\":\\[(.*?)\\]\\}");
		// 匹配一句话中的分词信息的正则表达式
		Pattern wordsPat = Pattern.compile(
				"\"alternativeList\":\\[\\],\"wc\":\".*?\",\"wordBg\":\".*?\",\"wordEd\":\".*?\",\"wordsName\":\"(.*?)\",\"wp\":\"(.*?)\"");
		Matcher m = pattern.matcher(text);

		while (m.find()) { // 匹配每一句话
			int beginTime = Integer.parseInt(m.group(1)); // 句子开始时间
			int endTime = Integer.parseInt(m.group(2)); // 结束时间
			StringBuilder sentence = new StringBuilder();
			String words = m.group(5); // 分词信息
			Matcher wordsMac = wordsPat.matcher(words);
			while (wordsMac.find()) { // 匹配每个分词项
				String wordsName = wordsMac.group(1); // 分词内容
				String wp = wordsMac.group(2); // 词性
				if (!wp.equals("s")) { // 去除语气词
					sentence.append(wordsName);
				}
			}

			textBf.append(sentence);
			String temp = "";
			if (!(temp = sentence.toString()).equals("")) {
				AlignSentence alignSentence = new AlignSentence(null, temp, beginTime, endTime, classId);
				alignSentenceMapper.addAlignSentence(alignSentence);
			}
		}
		alignSentenceMapper.addAlignTotalText(textBf.toString(), classId);

	}

	@Override
	public void extractSchedule(String classId) throws UnknownHostException, IOException, ParseException {
		log.info("server:" + scheduleExtractorConfiguration.getServer() + " port:" + scheduleExtractorConfiguration.getServerPort());
		log.info("client:" + scheduleExtractorConfiguration.getClient() + " port:" + scheduleExtractorConfiguration.getClientPort());
		List<String> list = new ArrayList<String>();
		List<AlignSentence> alignSentences = alignSentenceMapper.queryAlignSentenceByClass(classId);
		for(AlignSentence alignSentence : alignSentences) {
			list.add(alignSentence.getText());
		}
		CorrespondUtil corres = new CorrespondUtil(InetAddress.getByName(scheduleExtractorConfiguration.getClient()),
				InetAddress.getByName(scheduleExtractorConfiguration.getServer()),
				scheduleExtractorConfiguration.getServerPort(), scheduleExtractorConfiguration.getClientPort());

		log.info(JSON.toJSONString(list));
		JSONObject jsonObject = corres.sendJson(list);
		
		 SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			if(recognize(analyze.getString("" + i))) {
				Schedule schedule = new Schedule(null, latestTime, analyze.getString("" + i), classId);
				scheduleMapper.addSchedule(schedule);
			}
		}
	}
	
	private boolean recognize(String text) throws FileNotFoundException {
	    SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer();
	    List<Set<String>> analyzeList = syntaxAnalyzer.analyze(text);
	    Set<String> vOBSet = new HashSet<String>();
	    Set<String> dBLSet = new HashSet<String>();
	    Set<String> cOOSet = new HashSet<String>();
	    Set<String> sBVSet = new HashSet<String>();
	    //DBL --> 同学、
	    dBLSet.add("同学");
	    dBLSet.add("学生");
	    //VOB --> 集合、实验、思考、交流、作业、预习、内容、
	    //小测、报告、审核、参加、回去、改进、改、参与
	    BufferedReader vobReader = new BufferedReader(new FileReader("./VOB.txt"));
	    String vobString;
	    try {
	      while ((vobString = vobReader.readLine()) != null) {
	        String[] vobStrings = vobString.split("、");
	        for (int i = 0; i < vobStrings.length; i++) {
	          vOBSet.add(vobStrings[i]);
	        }
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    //COO --> 交、留给、想到、改、推迟、提前、安装、预习、回去、举行、领
	    BufferedReader cooReader = new BufferedReader(new FileReader("./COO.txt"));
	    String cooString;
	    try {
	      while ((cooString = cooReader.readLine()) != null) {
	        String[] cooStrings = cooString.split("、");
	        for (int i = 0; i < cooStrings.length; i++) {
	          cOOSet.add(cooStrings[i]);
	        }
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    //SBV --> 同学、作业、日期、教室、地点
	    sBVSet.add("同学");
	    sBVSet.add("学生");
	    sBVSet.add("作业");
	    sBVSet.add("功课");
	    sBVSet.add("任务");
	    sBVSet.add("日期");
	    sBVSet.add("限期");
	    sBVSet.add("时间");
	    sBVSet.add("教室");
	    sBVSet.add("地点");
	    
//	    Set<String> lessVOBSet = new HashSet<String>();
//	    for(String vob : analyzeList.get(0)) {
//	      if (vob.length() > 2) {
//	        vob = vob.substring(0, 2);
//	      }
//	      lessVOBSet.add(vob);
//	    }
	    
	    analyzeList.get(0).retainAll(vOBSet);
	    analyzeList.get(1).retainAll(dBLSet);
	    analyzeList.get(2).retainAll(cOOSet);
	    analyzeList.get(3).retainAll(sBVSet);
//	    lessVOBSet.retainAll(vOBSet);
	    
	    String results;
	    if (analyzeList.get(0).size() > 0) {//vob类型含有关键词
	      for (String vob: analyzeList.get(0)) {
	        results = syntaxAnalyzer.fatherMap.get(vob) + " " + vob + " ";
	        return true;
	      }
	    }
	    //SBV和COO类型含有关键词
	    if (analyzeList.get(3).size() > 0 && analyzeList.get(2).size() > 0) {
	      for (String sbv: analyzeList.get(3)) {
	        for (String coo: analyzeList.get(2)) {
	          results = sbv + " " + coo + " ";
	          return true;
	        }
	      }
	    } 
	    //DBL和COO类型含有关键词
	    if (analyzeList.get(1).size() > 0 && analyzeList.get(2).size() > 0) {
	      for (String dbl: analyzeList.get(1)) {
	        for (String coo: analyzeList.get(2)) {
	          results = dbl + " " + coo + " ";
	          return true;
	        }
	      }
	    }
	   return false;
	}

}
