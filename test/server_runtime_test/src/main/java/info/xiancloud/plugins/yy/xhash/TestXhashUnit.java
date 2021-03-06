package info.xiancloud.plugins.yy.xhash;

import info.xiancloud.plugin.*;
import info.xiancloud.plugin.message.UnitResponse;
import info.xiancloud.plugin.message.UnitRequest;
import info.xiancloud.plugin.test.TestGroup;
import info.xiancloud.plugin.util.LOG;

/**
 * @author happyyangyuan
 */
public class TestXhashUnit implements Unit {
    @Override
    public String getName() {
        return "testXhash";
    }

    @Override
    public UnitMeta getMeta() {
        return UnitMeta.create("验证xhashSender").setPublic(false);
    }

    @Override
    public Input getInput() {
        return new Input()
                .add("x", String.class, "xhash属性，现在设计了一个用例，" +
                        "用来去定位入参是否稳定的按照一致性哈希来进行分配接收节点，" +
                        "请传入固定值abc-123-efg和ABC-456-EFG", REQUIRED, XHASH)
                .add("y", int.class, "现在暂时只验证单个xhash入参，不需要支持多个xhash入参"/*, REQUIRED, XHASH*/);
    }

    private static volatile String x;

    @Override
    public UnitResponse execute(UnitRequest msg) {
        String inputX = msg.get("x", String.class);
        initXOnlyOnce(inputX);
        if (!x.equals(inputX)) {
            LOG.error(new Throwable("验证不通过，xhash不按照预期运行，期望参数：" + x + "；实际参数：" + inputX));
            return UnitResponse.failure(null, "验证不通过，xhash不按照预期运行，期望参数：" + x + "；实际参数：" + inputX);
        }
        return UnitResponse.success(x);
    }

    @Override
    public Group getGroup() {
        return TestGroup.singleton;
    }

    synchronized private static void initXOnlyOnce(String xInput) {
        if (x == null) {
            x = xInput;
        }
    }

}
