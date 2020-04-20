package com.example.demo.service.Imp;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.AlignSentence;
import com.example.demo.dao.AlignSentenceMapper;
import com.example.demo.exception.TransferException;
import com.example.demo.service.WaveProcessService;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;
import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;

@Service
public class WaveProcessServiceImp implements WaveProcessService{

	@Autowired
	private AlignSentenceMapper alignSentenceMapper;
	
	@Override
	public String waveConvertToText(String filePath) throws TransferException {
		final LfasrType type = LfasrType.LFASR_STANDARD_RECORDED_AUDIO;
		// 等待时长（秒）
		final int sleepSecond = 20;
		// 初始化LFASRClient实例
				LfasrClientImp lc = null;
				try {
					lc = LfasrClientImp.initLfasrClient("5e476b22", "43a516319eb4c30d92d3b200d80f43bd");
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

}
