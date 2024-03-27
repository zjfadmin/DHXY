package org.come.serviceImpl;

import org.apache.ibatis.annotations.Param;
import org.come.entity.Fly;
import org.come.mapper.FlyMapper;
import org.come.redis.RedisCacheUtil;
import org.come.redis.RedisControl;
import org.come.redis.RedisParameterUtil;
import org.come.service.FlyService;
import org.come.until.MybatisUntil;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.List;

public class FlylServiceImpl implements FlyService {

    private FlyMapper flyMapper;


    public FlylServiceImpl() {
        ApplicationContext ctx = MybatisUntil.getApplicationContext();
        // id为类名且首字母小写才能被自动扫描扫到
        flyMapper = (FlyMapper) ctx.getBean("flyMapper");
    }
    @Override
    public List<Fly> selectFlyByRoleID(BigDecimal roleID) {
        List<Fly> fly = RedisControl.getS(RedisParameterUtil.FLY, roleID.toString(), Fly.class);
        return fly;
    }

    @Override
    public Fly selectFlyByFID(String fid) {
        Fly fly = RedisControl.getV(RedisParameterUtil.FLY, fid, Fly.class);
        return fly;
    }

    @Override
    public void deleteFlyByMid(BigDecimal roleID) {

    }

    @Override
    public void updateFly(Fly fly) {
        Fly fly2=RedisControl.getV(RedisParameterUtil.FLY, fly.getFid().toString(), Fly.class);
        if (fly2!=null) {
            RedisControl.insertKeyT(RedisParameterUtil.FLY, fly2.getFid().toString(),fly2);
            // 加入操作表
            RedisControl.insertController(RedisParameterUtil.FLY, fly2.getFid().toString(), 2+"");
        }
    }

    @Override
    public void updateFlyRedis(Fly fly) {

    }

    @Override
    public void insertFly(Fly fly) {
        fly.setFid(RedisCacheUtil.getFly_pk());
        RedisControl.insertKeyT(RedisParameterUtil.FLY, fly.getFid().toString(), fly);
        // 插入人物坐骑
        RedisControl.insertListRedis(RedisParameterUtil.FLY, fly.getRoleid().toString(), fly.getFid().toString());
        // 加入操作表
        RedisControl.insertController(RedisParameterUtil.FLY, fly.getFid().toString(), 1 + "");
    }

    @Override
    public void saveBatch(List<Fly> addList) {
        flyMapper.saveBatch(addList);
    }

    @Override
    public List<Fly> selectAllFlys() {
        return flyMapper.selectAllFlys();
    }

    @Override
    public Integer insertFlyToSql(Fly fly) {
        return flyMapper.insertFlyToSql(fly);
    }

    @Override
    public Integer deleteFlysByFidList(List<BigDecimal> delList) {
        return flyMapper.deleteFlysByFidList(delList);
    }

    @Override
    public Integer deleteFlyByMidsql(BigDecimal fid) {
        return flyMapper.deleteFlysByFid(fid);
    }

    @Override
    public Integer updateFlyList(List<Fly> addList) {
        return flyMapper.updateFlyList(addList);
    }

    @Override
    public Integer updateFlyToSql(Fly fly) {
        return flyMapper.updateFly(fly);
    }

    @Override
    public void removeByIds(List<BigDecimal> delList) {
        flyMapper.deleteFlysByFidList(delList);
    }

    @Override
    public void removeById(BigDecimal fid) {
        flyMapper.deleteFlysByFid(fid);
    }

    @Override
    public void updateById(@Param("fly") Fly fly) {
        flyMapper.updateFly(fly);
    }


}
