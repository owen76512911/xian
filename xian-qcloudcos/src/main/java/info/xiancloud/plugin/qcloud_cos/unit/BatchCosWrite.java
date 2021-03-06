package info.xiancloud.plugin.qcloud_cos.unit;

import info.xiancloud.plugin.Group;
import info.xiancloud.plugin.Input;
import info.xiancloud.plugin.message.UnitResponse;
import info.xiancloud.plugin.Unit;
import info.xiancloud.plugin.message.UnitRequest;
import info.xiancloud.plugin.qcloud_cos.sdk.CosFileWriter;
import info.xiancloud.plugin.thread_pool.ThreadPoolManager;
import info.xiancloud.plugin.util.Pair;
import info.xiancloud.plugin.util.thread.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author happyyangyuan
 */
public class BatchCosWrite implements Unit {
    @Override
    public String getName() {
        return "batchCosWrite";
    }

    @Override
    public Group getGroup() {
        return CosGroup.singleton;
    }

    private static final int DEFAULT_THREAD_COUNT = ThreadUtils.CPU_CORES * 15;
    private static final int MAX_PER_THREAD = 100;
    private static final int TIME_OUT_IN_SECONDED = 60;

    @Override
    public Input getInput() {
        return new Input()
                .add("files", Map.class, "文件名-内容的map", REQUIRED)
                .add("threadCount", int.class, "并行的线程数，建议设置在20以内，如果不传，默认为10");
    }

    @Override
    public UnitResponse execute(UnitRequest msg) {
        Map<String, String> files = msg.get("files", Map.class);
        int threadCount;
        if (msg.getArgMap().containsKey("threadCount")) {
            threadCount = msg.get("threadCount", Integer.class);
        } else {
            threadCount = DEFAULT_THREAD_COUNT;
        }
        int perThread = new Double(Math.ceil(files.size() / (double) threadCount)).intValue();
        if (perThread > MAX_PER_THREAD) {
            return UnitResponse.failure(null, "待处理文件数过多，请减小数量,fileSize=" + files.size() + "; 最大允许" + threadCount * MAX_PER_THREAD);
        }
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<String> paths = new ArrayList<>();
        paths.addAll(files.keySet());
        for (int i = 0; i < threadCount; i++) {
            final Pair<Integer, Integer> startEnd = getStartEnd(i, files.size(), perThread);
            ThreadPoolManager.execute(() -> {
                CosFileWriter writer = new CosFileWriter();
                try {
                    writer.getCosClient();
                    for (int j = startEnd.fst; j <= startEnd.snd; j++) {
                        String path = paths.get(j);
                        String data = files.get(path);
                        writer.forPath(path, data);
                    }
                } finally {
                    writer.close();
                    latch.countDown();
                }
            });
        }
        try {
            latch.await(TIME_OUT_IN_SECONDED, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return UnitResponse.success();
    }

    private Pair<Integer, Integer> getStartEnd(int i, int size, int perThread) {
        int start = i * perThread;
        int end = start + perThread - 1;
        if (end > size - 1) {
            end = size - 1;
        }
        return Pair.of(start, end);
    }

    public static void main(String[] args) {
        double tt = 101 / 100.0;
        int perThread = new Double(Math.ceil(tt)).intValue();
        System.out.println(perThread);
    }
}
