package com.example.demo.utils;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.AlignResult;
import com.example.demo.exception.TransferException;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;
import com.iflytek.msp.cpdb.lfasr.model.ProgressStatus;

/**
 * 调用语音转写服务，获取转写结果
 * 
 * @author xjy
 *
 */
public class WavToTextUtil {

	/*
	 * 转写类型选择：标准版和电话版(旧版本, 不建议使用)分别为： LfasrType.LFASR_STANDARD_RECORDED_AUDIO 和
	 * LfasrType.LFASR_TELEPHONY_RECORDED_AUDIO
	 */
	private static final LfasrType type = LfasrType.LFASR_STANDARD_RECORDED_AUDIO;

	// 等待时长（秒）
	private static int sleepSecond = 20;

	/**
	 * 使用讯飞服务获取转写结果
	 * 
	 * @param wavPath 文件路径
	 * @return
	 * @throws TransferException
	 */
	private static String getMessage(String wavPath) throws TransferException {
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
			Message uploadMsg = lc.lfasrUpload(wavPath, type, params);

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

	/**
	 * 调用讯飞转写服务, 对wavPath路径的.wav文件进行转写, 并对齐转写结果
	 * 
	 * @param wavPath .wav文件路径
	 * @return 已对齐的转写结果
	 * @throws TransferException 当服务器有问题、录音有问题等时候, 抛出该改转写异常, 包含任务id、错误码等
	 */
	public static AlignResult getAignResult(String wavPath) throws TransferException {
		return new AlignResult(wavPath, getMessage(wavPath));
	}

	// .....Test....
	public static void main(String[] args) {

		String msg = "";
		try {
			msg = getMessage("./resource/audio/xwlb.wav");
			System.out.println(msg);
		} catch (TransferException e) {
			e.printStackTrace();
		}

		AlignResult ar = new AlignResult("xwlb", msg);

		for (int i = 0; i < ar.getNumOfSentence(); i++) {
			System.out.println("begin: " + ar.getBeginTime(i));
			System.out.println("end: " + ar.getEndTime(i));
			System.out.println(ar.getSentence(i));
		}

		System.out.println("The text is: " + ar.getText());

	}
}
