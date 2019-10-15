package com.ztgeo.general.biz;

import com.ace.cache.api.impl.CacheRedis;
import com.alibaba.fastjson.JSONArray;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.dicFinal.FinalStr;
import com.ztgeo.general.entity.DicItem;
import com.ztgeo.general.exception.NoDateException;
import com.ztgeo.general.mapper.DicItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典-字典字项表
 *
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
@Service
public class DicItemBiz extends BusinessBiz<DicItemMapper,DicItem> {

    private static final String PIXCACHE="pubserver:";

    @Autowired
    private CacheRedis cacheRedis;




    //根据父级目录和字典的key定位字典值
//    @Cache(key = "pubserver{1}{2}")
    public DicItem getDicWithPandC(String parParam, String childParam) {
        //先查找父级菜单有没有被r缓存
        List<DicItem> dicItems=new ArrayList<>();
        DicItem dicItem=null;
        String s = cacheRedis.get(PIXCACHE + parParam);
        if(s!=null){
            //反序列化
            System.out.println("命中了父节点");
            dicItems = JSONArray.parseArray(s, DicItem.class);
        }else{
            System.out.println("未命中父节点，调用了父节点的获取方法");
            dicItems=getDicWithPandC(parParam);
        }
        //从父节点中遍历取值
        List<DicItem> collect = dicItems.stream().filter(item -> childParam.equals(item.getItemname())).collect(Collectors.toList());
        if (collect==null||collect.size()==0){
            throw new NoDateException(FinalStr.NO_DATA_MESSAGE+",params: "+parParam+"  "+childParam);
        }
        return collect.get(0);
    }

    //根据父级目录和字典的key定位字典值
    //@Cache(key = "pubserver{1}")
    public List<DicItem> getDicWithPandC(String parParam) {

        List<DicItem> dicItemList= new ArrayList<>();
        String s = cacheRedis.get(PIXCACHE + parParam);
        if( s==null){//没有 则查库 并加入缓存
            System.out.println("没有父级缓存，查询并加入了缓存");
            dicItemList =mapper.getDicWithP(parParam);
            cacheRedis.set(PIXCACHE+parParam,dicItemList,720,parParam);
        }else{
            System.out.println("匹配了父级缓存，取出了数据并返回");
            dicItemList =JSONArray.parseArray(cacheRedis.get(PIXCACHE+parParam),DicItem.class);

        }
        if (dicItemList.size()==0){
            throw new NoDateException(FinalStr.NO_DATA_MESSAGE+",params: "+parParam);
        }

        return dicItemList;
    }

    //当item更新时 移除所有缓存（这样做不合理 但是字典的修改属于极少数情况，为了降低缓存复杂度）
//    @CacheClear(key = "pubserver")
    @Override
    public void updateSelectiveById(DicItem entity) {
        super.updateSelectiveById(entity);
        cacheRedis.removeByPre("pubserver");
    }

    @Override
    public void insertSelective(DicItem entity) {
        super.insertSelective(entity);
        cacheRedis.removeByPre("pubserver");
    }

    @Override
    public void deleteById(Object id) {
        super.deleteById(id);
        cacheRedis.removeByPre("pubserver");
    }

    //根据dicCode获取字典列表
    public List<DicItem> getDicWithDicCode(String dicCode) {
        List<DicItem> dicItemList=  mapper.getDicWithDicCode(dicCode);
//        if (dicItemList.size()==0){
//            throw new NoDateException(FinalStr.NO_DATA_MESSAGE+",params: "+dicCode);
//        }

        return dicItemList;
    }
}