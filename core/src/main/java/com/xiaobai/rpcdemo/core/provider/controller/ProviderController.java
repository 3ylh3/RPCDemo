package com.xiaobai.rpcdemo.core.provider.controller;

import com.xiaobai.rpcdemo.core.common.ResultDTO;
import com.xiaobai.rpcdemo.core.common.TransferDTO;
import com.xiaobai.rpcdemo.core.enums.MessageCodeEnum;
import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.lang.reflect.Method;

/**
 * provider controller
 *
 * @author yinzhaojing
 * @date 2021-04-28 20:06:28
 */
@RestController
public class ProviderController {

    private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

    @RequestMapping("/provider")
    public String aa(@RequestBody String request) {
        ResultDTO resultDTO = new ResultDTO();
        FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
        try {
            logger.info("----------------------------------");
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] bytes = base64Decoder.decodeBuffer(request);
            //使用fst反序列化
            TransferDTO transferDTO = (TransferDTO)conf.asObject(bytes);
            String className = transferDTO.getClassName();
            String methodName = transferDTO.getMethodName();
            logger.info("accept remote call:{}-{}.{}", transferDTO.getConsumerName(), className, methodName);
            Class<?>[] parameterTypes = transferDTO.getParameterTypes();
            Object[] params = transferDTO.getParams();
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            Object result = method.invoke(clazz.newInstance(), params);
            logger.info("invoke method success");
            resultDTO.setMessageCode(MessageCodeEnum.SUCCESS.getCode());
            resultDTO.setMessage("success");
            resultDTO.setResult(result);
        } catch (Exception e) {
            logger.error("process remote call exception:{}", e.toString());
            resultDTO.setMessageCode(MessageCodeEnum.ERROR.getCode());
            resultDTO.setMessage(e.toString());
        }
        //序列化返回对象
        byte[] resultBytes = conf.asByteArray(resultDTO);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(resultBytes);
    }
}
