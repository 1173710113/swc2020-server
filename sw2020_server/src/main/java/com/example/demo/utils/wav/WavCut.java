package com.example.demo.utils.wav;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.*;
import java.nio.ByteBuffer;

import org.springframework.stereotype.Component;

import com.example.demo.domain.MyFile;
import com.example.demo.exception.MyException;

/**
 * FileName: WavCut Author: xuan zongjun Date: 2018/10/18 10:45 Description:
 * wav视频切割
 */

@Component
public class WavCut {
	/**
	 * @param sourcefile
	 * @param targetfile
	 * @param start
	 * @param end
	 * @return 
	 * @return
	 * @throws IOException 
	 * @throws MyException 
	 */
	public MyFile  cut(MyFile sourceFile, int start, int end) throws IOException, MyException {
		try {
			if (!sourceFile.getFileName().toLowerCase().endsWith(".wav")) {
				throw new MyException("不是wav文件");
			}
			File wav = new File(sourceFile.getFilePath());
			if (!wav.exists()) {
				throw new MyException("源文件不存在");
			}
			long t1 = getTimeLen(wav); // 总时长(秒)
			if (start < 0 || end <= 0 || start >= t1 || end > t1 || start >= end) {
				throw new MyException("文件切割位置错误:total:" + t1 + ",start:" + start +",end:" + end);
			}
			FileInputStream fis = new FileInputStream(wav);
			long wavSize = wav.length() - 44; // 音频数据大小（44为128kbps比特率wav文件头长度）
			long splitSize = (wavSize / t1) * (end - start); // 截取的音频数据大小
			long skipSize = (wavSize / t1) * start; // 截取时跳过的音频数据大小
			int splitSizeInt = Integer.parseInt(String.valueOf(splitSize));
			int skipSizeInt = Integer.parseInt(String.valueOf(skipSize));

			ByteBuffer buf1 = ByteBuffer.allocate(4); // 存放文件大小,4代表一个int占用字节数
			buf1.putInt(splitSizeInt + 36); // 放入文件长度信息
			byte[] flen = buf1.array(); // 代表文件长度
			ByteBuffer buf2 = ByteBuffer.allocate(4); // 存放音频数据大小，4代表一个int占用字节数
			buf2.putInt(splitSizeInt); // 放入数据长度信息
			byte[] dlen = buf2.array(); // 代表数据长度
			flen = reverse(flen); // 数组反转
			dlen = reverse(dlen);
			byte[] head = new byte[44]; // 定义wav头部信息数组
			fis.read(head, 0, head.length); // 读取源wav文件头部信息
			for (int i = 0; i < 4; i++) { // 4代表一个int占用字节数
				head[i + 4] = flen[i]; // 替换原头部信息里的文件长度
				head[i + 40] = dlen[i]; // 替换原头部信息里的数据长度
			}
			byte[] fbyte = new byte[splitSizeInt + head.length]; // 存放截取的音频数据
			for (int i = 0; i < head.length; i++) { // 放入修改后的头部信息
				fbyte[i] = head[i];
			}
			byte[] skipBytes = new byte[skipSizeInt]; // 存放截取时跳过的音频数据
			fis.read(skipBytes, 0, skipBytes.length); // 跳过不需要截取的数据
			fis.read(fbyte, head.length, fbyte.length - head.length); // 读取要截取的数据到目标数组
			fis.close();

			String targetFilePath = sourceFile.getFilePath().substring(0, sourceFile.getFilePath().length() - sourceFile.getFileName().length());
			String targetFileName = start + "_" + end + ".wav";
			File target = new File(targetFilePath + targetFileName);
			if (target.exists()) { // 如果目标文件已存在，则删除目标文件
				target.delete();
			}
			FileOutputStream fos = new FileOutputStream(target);
			fos.write(fbyte);
			fos.flush();
			fos.close();
			return new MyFile(null, targetFilePath + targetFileName, targetFileName, sourceFile.getClassId());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private long getTimeLen(File file) {
		long tlen = 0;
		if (file != null && file.exists()) {
			Encoder encoder = new Encoder();
			try {
				MultimediaInfo m = encoder.getInfo(file);
				long ls = m.getDuration();
				tlen = ls / 1000;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return tlen;
	}

	private byte[] reverse(byte[] array) {
		byte temp;
		int len = array.length;
		for (int i = 0; i < len / 2; i++) {
			temp = array[i];
			array[i] = array[len - 1 - i];
			array[len - 1 - i] = temp;
		}
		return array;
	}

}
