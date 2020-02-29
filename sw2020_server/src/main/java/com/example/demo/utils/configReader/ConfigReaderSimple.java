package com.example.demo.utils.configReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.utils.inputstrategy.InputStrategy;

public class ConfigReaderSimple implements Configreader {
    private final Map<String, String> configs;

    /**
     * 新建一个配置文件读取器，配置简单地以“config = value”的格式组成，每行一条配置，注释以#开头
     * 
     * @param filePath 配置文件的路径
     * @throws FileNotFoundException 当文件没有找到
     */
    public ConfigReaderSimple(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        try {
        InputStrategy ip = InputStrategy.input(file);
        String tmpConfig;
        configs = new HashMap<String, String>();
        while ((tmpConfig = ip.nextLine()) != null) {
            if (!tmpConfig.startsWith("#")) {
                String[] oneConfig = tmpConfig.split(" = ");
                configs.put(oneConfig[0], oneConfig[1]);
            }
        }
        } catch (FileNotFoundException e) {
          throw new FileNotFoundException("No config file exists!");
        }
    }
    
    /**
     * 直接返回值
     */
    public String readItem(String itemName) {
        return configs.getOrDefault(itemName, null);
    }
}
