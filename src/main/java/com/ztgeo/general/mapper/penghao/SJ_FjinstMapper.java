package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjinst;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SJ_FjinstMapper {
    int deleteByPrimaryKey(String cid);

    /**
     * 查询是否有子级，有子级不得删除
     * @param pid
     * @return
     */
    Integer selectByPid(String pid);





    /**
     * 根据条目Id查询流程实例是否有数据
     * @param entrId
     * @return
     */
    List<SJ_Fjinst> findFjinstFjtmId(String entrId);

    /**
     * 子级重复名称
     * @param cname
     * @param cid
     * @return
     */
    Integer selectCountNameByPid(@Param("cname") String cname,@Param("pid") String pid,@Param("cid") String cid);

    /**
     * 根据父级获得最大排序数
     * @param pid
     * @return
     */
    Integer selectMaxByPid(String pid);

    void deleteByFileId(String fileId);

    int insert(SJ_Fjinst record);

    int insertSelective(SJ_Fjinst record);

    /**
     * 根据模板，实列,职务查询条目附件
     * @param
     * @param list
     * @param processId
     * @return
     */
    List<SJ_Fjinst> findSjFjinstByPosition(@Param("list") List<String> list,@Param("processId") String processId);


    List<SJ_Fjinst> findSjFjinstByAdmin(@Param("processId") String processId);

    /**
     * 循环新增
     * @param list
     */
    void insertList(List<SJ_Fjinst> list);

    SJ_Fjinst selectByPrimaryKey(String cid);

    int updateByPrimaryKeySelective(SJ_Fjinst record);

    int updateByPrimaryKey(SJ_Fjinst record);

    public Integer selectCountOfEntryFile(@Param("pnode") String pnode, @Param("entryId")String entryId);
}
