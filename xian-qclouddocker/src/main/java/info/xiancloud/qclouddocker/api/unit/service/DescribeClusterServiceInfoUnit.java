package info.xiancloud.qclouddocker.api.unit.service;

import info.xiancloud.plugin.Input;
import info.xiancloud.plugin.message.UnitResponse;
import info.xiancloud.plugin.UnitMeta;
import info.xiancloud.plugin.message.UnitRequest;
import info.xiancloud.qclouddocker.api.unit.QCloudBaseUnit;

/**
 * 查询服务详情
 *
 * @author yyq
 */
public class DescribeClusterServiceInfoUnit extends QCloudBaseUnit {
    @Override
    public String getName() {
        return "describeClusterServiceInfo";
    }

    @Override
    public UnitMeta getMeta() {
        return UnitMeta.create("查询服务详情");
    }

    @Override
    public Input getInput() {
        return new Input()
                .add("clusterId", String.class, "集群ID，可通过查询集群接口反回字段中的 clusterId获取", REQUIRED)
                .add("serviceName", String.class, "服务名", REQUIRED)
                .add("namespace", String.class, "命名空间,默认为default");
    }

    @Override
    public UnitResponse execute(UnitRequest msg) {
        return super.execute(msg);
    }

    @Override
    public String getAction() {
        return "DescribeClusterServiceInfo";
    }

    @Override
    public String getAPIHost() {
        return "ccs.api.qcloud.com";
    }

}
