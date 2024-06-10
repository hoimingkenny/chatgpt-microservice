package com.openai.chatgpt.interfaces;

import cn.bugstack.chatglm.model.ChatCompletionRequest;
import cn.bugstack.chatglm.model.ChatCompletionSyncResponse;
import cn.bugstack.chatglm.model.Model;
import cn.bugstack.chatglm.model.Role;
import cn.bugstack.chatglm.session.Configuration;
import cn.bugstack.chatglm.session.OpenAiSession;
import cn.bugstack.chatglm.session.OpenAiSessionFactory;
import cn.bugstack.chatglm.session.defaults.DefaultOpenAiSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/telegram/portal")
public class TelelgramPortalController {
    private final OpenAiSession openAiSession;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    // 存放OpenAi返回结果数据
    private final Map<String, String> openAiDataMap = new ConcurrentHashMap<>();
    // 存放OpenAi调用次数数据
    private final Map<String, Integer> openAiRetryCountMap = new ConcurrentHashMap<>();

    public TelelgramPortalController() {
        // 1. 配置文件；智谱Ai申请你的 ApiSecretKey 教程；https://bugstack.cn/md/project/chatgpt/sdk/chatglm-sdk-java.html
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://open.bigmodel.cn/");
        configuration.setApiSecretKey("a35ea516bfcdbf17adfd5d0a156413dd.A9oReubhRwkjY3CQ");
        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
        log.info("开始 openAiSession");
    }

    @PostMapping(path="/post")
    public String post(String request) {
        doChatGPTTask(request);
        return "";
    }

    public void doChatGPTTask(String content) {
        openAiDataMap.put(content, "NULL");
        taskExecutor.execute(() -> {
            // 入参；模型、请求信息；记得更新最新版 ChatGLM-SDK-Java
            ChatCompletionRequest request = new ChatCompletionRequest();
            request.setModel(Model.GLM_3_5_TURBO); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
            request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
                private static final long serialVersionUID = -7988151926241837899L;
                {
                    add(ChatCompletionRequest.Prompt.builder()
                            .role(Role.user.getCode())
                            .content(content)
                            .build());
                }
            });
            // 同步获取结果
            try {
                // 2.1 提供的官网方法
                ChatCompletionSyncResponse response = openAiSession.completionsSync(request);
                // 保存数据
                openAiDataMap.put(content, response.getChoices().get(0).getMessage().getContent());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
