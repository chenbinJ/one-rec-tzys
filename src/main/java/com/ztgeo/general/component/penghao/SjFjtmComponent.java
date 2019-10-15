package com.ztgeo.general.component.penghao;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.extend.Base64Picture;
import com.ztgeo.general.entity.extend.Fjtm_Power;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjinst;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjtm;
import com.ztgeo.general.entity.service_data.pub_data.SjHandleFlow;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.FileManager;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.PositionUserMapper;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.penghao.SJ_FjfileMapper;
import com.ztgeo.general.mapper.penghao.SJ_FjinstMapper;
import com.ztgeo.general.mapper.penghao.SJ_FjtmMapper;
import com.ztgeo.general.mapper.penghao.SjHandleFlowMapper;
import com.ztgeo.general.util.Base64Test;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import groovy.util.IFileNameFinder;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class SjFjtmComponent {

    @Autowired
    private SJ_FjtmMapper sj_fjtmMapper;
    @Value("${entry.comparison}")
    private String comparison;
    @Autowired
    private SJ_FjinstMapper sj_fjinstMapper;
    @Autowired
    private ActivitiComponent activitiComponent;
    @Autowired
    private SjHandleFlowMapper sjHandleFlowMapper;
    @Autowired
    private ToFTPUploadComponent toFTPUploadComponent;
    @Autowired
    private SJ_FjfileMapper sj_fjfileMapper;
    @Autowired
    private PositionUserMapper positionUserMapper;
    @Autowired
    private UserMapper userMapper;


    /**
     * 删除条目判断条目实例是否含有条目
     * @param entryId
     * @return
     */
    public boolean findFjinstFjtmId(String entryId){
        List<SJ_Fjinst> fjinstList=sj_fjinstMapper.findFjinstFjtmId(entryId);
        if (fjinstList==null || fjinstList.size()==0){
            return  true;
        }
        return  false;
    }

    /**
     * 根据模板岗位查询条目集合
     * @param mid
     * @return
     */
    public Object findFjtmByPositionOrModelId(String mid){
        List<String> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        //判断用户是否为管理员
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())) {
            //管理员不需要判断权限
            List<Fjtm_Power> fileEntryVoList=sj_fjtmMapper.findFjtmPowerByPosition(null,mid);
            return  fileEntryVoList;
        }
        //获得用户对应职务(可多个)
        List<Position> positions=positionUserMapper.selectPositionByUid(UserUtil.getUerId());
        if (positions==null || positions.size()==0){
            return  new ZtgeoBizException(MsgManager.POSITION_USER_NULL);
        }
        for (Position position:positions) {
            list.add(position.getId());
        }
        List<Fjtm_Power> fileEntryVoList=sj_fjtmMapper.findFjtmPowerByPosition(list,mid);
        return fileEntryVoList;
    }




    /**
     * 根据实列,职务查询条目附件
     * @param
     * @param processId
     * @return
     */
    public  Object findFjtmByPosition(String processId){
        List<String> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        String mid=activitiComponent.getActivityByProcessId(processId);
        //判断用户是否为管理员
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())) {
            //管理员不需要判断权限
            List<SJ_Fjinst> sjFjinstList=slectAdminShu(processId);
            List<Fjtm_Power> fileEntryVoList=sj_fjtmMapper.findFjtmByAdmin(mid);
            for (Fjtm_Power power:fileEntryVoList) {
                power.setPermissionLevel(MsgManager.WRITE);
            }
            map.put("fileEntryVoList",fileEntryVoList);
            map.put("fileInstanceVoList",sjFjinstList);
            return  map;
        }
        //获得用户对应职务(可多个)
        List<Position> positions=positionUserMapper.selectPositionByUid(UserUtil.getUerId());
        if (positions==null || positions.size()==0){
            return  new ZtgeoBizException(MsgManager.POSITION_USER_NULL);
        }
        for (Position position:positions) {
            list.add(position.getId());
        }
        List<SJ_Fjinst> sjFjinstList=slectShu(list,processId);
        //通过职务权限展示条目
        List<Fjtm_Power> fileEntryVoList=sj_fjtmMapper.findFjtmPowerByPosition(list,mid);
        map.put("fileEntryVoList",fileEntryVoList);
        map.put("fileInstanceVoList",sjFjinstList);
        return map;
    }

    /**
     * 根据岗位实列展示树
     */
    private List<SJ_Fjinst> slectShu(List<String> list,String processId){
        List<SJ_Fjinst> fjinstList=new ArrayList<>();
        //遍历集合返回树状结构
        List<SJ_Fjinst> sjFjinsts=sj_fjinstMapper.findSjFjinstByPosition(list,processId);
        if (sjFjinsts==null || sjFjinsts.size()==0){
            throw  new  ZtgeoBizException(MsgManager.SJFJTM_PROCESS_NULL);
        }
        boolean flag=true;
        for (SJ_Fjinst sfjinst:sjFjinsts) {
            flag=true;
            for (SJ_Fjinst fjinst:sjFjinsts) {
                if (sfjinst.getCid().equals(fjinst.getPid())){
                    sfjinst.getChildren().add(fjinst);
                }
                if (fjinst.getCid().equals(sfjinst.getPid())){
                    flag=false;
                }
            }
            if (flag){
                fjinstList.add(sfjinst);
            }
        }
        return fjinstList;
    }


    /**
     * 根据岗位实列展示树
     */
    private List<SJ_Fjinst> slectAdminShu(String processId){
        List<SJ_Fjinst> fjinstList=new ArrayList<>();
        //遍历集合返回树状结构
        List<SJ_Fjinst> sjFjinsts=sj_fjinstMapper.findSjFjinstByAdmin(processId);
        if (sjFjinsts==null || sjFjinsts.size()==0){
            throw  new  ZtgeoBizException(MsgManager.SJFJTM_PROCESS_NULL);
        }
        boolean flag=true;
        for (SJ_Fjinst sfjinst:sjFjinsts) {
            flag=true;
            for (SJ_Fjinst fjinst:sjFjinsts) {
                if (sfjinst.getCid().equals(fjinst.getPid())){
                    sfjinst.getChildren().add(fjinst);
                }
                if (fjinst.getCid().equals(sfjinst.getPid())){
                    flag=false;
                }
            }
            if (flag){
                fjinstList.add(sfjinst);
            }
        }
        return fjinstList;
    }




    /**
     * 根据模板id查询条目新增到实列条目表
     * @param mid
     * @param processId
     * @return
     */
    public List<SJ_Fjinst> findFjtmByMid(String mid, String processId,List<SjHandleFlow> sjHandleFlows){
            SJ_Fjinst sj_fjinst=new SJ_Fjinst();//人脸识别条目实例
            List<SJ_Fjtm> sj_fjtms=this.sj_fjtmMapper.selectByMid(mid);
            SJ_Fjtm sjFjtm=null;
            for (SJ_Fjtm fjfile: sj_fjtms) {
                if (fjfile.getEntryName().equals(comparison) && fjfile.getProcessMouldId().equals(mid)){
                    sjFjtm=fjfile;
                }
            }
            if (sjFjtm==null){  //创建必选文件夹
                SJ_Fjtm sj_fjtm=new SJ_Fjtm();
                sj_fjtm.setEntryId(IDUtil.getEntryId());
                sj_fjtm.setEntryName(comparison);
                sj_fjtm.setProcessMouldId(mid);
                sj_fjtm.setNode(MsgManager.SJFJTM_NODE);
                sj_fjtm.setEntryType(MsgManager.BIXUAN);
                sj_fjtm.setCreateBy(UserUtil.checkAndGetUser());
                sj_fjtm.setCreateTime(new Date());
                sj_fjtm.setLastUpdate(new Date());
                if (sj_fjtmMapper.insertSelective(sj_fjtm)<1){
                    throw new ZtgeoBizException(MsgManager.ENTRY_INSERT_BAD);
                }
            }
            List<SJ_Fjtm> sjFjtmList=this.sj_fjtmMapper.selectByMid(mid);
            List<SJ_Fjinst> xmList=new ArrayList<>();
            List<SJ_Fjinst> list = getFjtmInst(sjFjtmList,mid,processId);
            Integer number=list.size();
            for (SJ_Fjinst fjinst:list) {
                //判断是否为人脸识别条目实例
                if (fjinst.getCname().equals(comparison) && fjinst.getPnode().equals(processId)){
                    sj_fjinst=fjinst;
                }
            }
            if (sj_fjinst==null ){
                throw new ZtgeoBizException(MsgManager.SJFJTM_NOT_COMPARISON);
            }

            if (sjHandleFlows.size()==0 || sjHandleFlows==null){
                throw  new ZtgeoBizException(MsgManager.SJFINST_PRITURE_NULL);
            }
            for (SjHandleFlow picture:sjHandleFlows) {
                number++;
                ///根据名称新增文件夹
                SJ_Fjinst fjinst=new SJ_Fjinst();
                fjinst.setCid(IDUtil.getFinstId());
                fjinst.setPid(sj_fjinst.getCid());
                fjinst.setCname(picture.getHandleName());
                fjinst.setPnode(processId);
                fjinst.setEntryId(sj_fjinst.getEntryId());
                fjinst.setCreateTime(new Date());
                fjinst.setOrderNumber(number);
                fjinst.setCreateBy(UserUtil.checkAndGetUser());
                fjinst.setCtype(sj_fjinst.getCtype());
                fjinst.setPnodeType(FileManager.SJFJINST_PROCESS);
                fjinst.setCkind(FileManager.SJFJINST_FILE);
                list.add(fjinst);
                xmList.add(fjinst);
            }
            sj_fjinstMapper.insertList(list);
          return xmList;
    }

    //处理附件条目实例的通用方法
    public List<SJ_Fjinst> getFjtmInst(List<SJ_Fjtm> sjFjtmList,String mid, String processId){
        List<SJ_Fjinst> list = new ArrayList<>();
        Integer number=0;
        String fid= IDUtil.getFinstId();
        //父级
        SJ_Fjinst parent=new SJ_Fjinst();
        parent.setCid(fid);
        parent.setPnode(processId);
        parent.setCreateTime(new Date());
        parent.setCreateBy(UserUtil.checkAndGetUser());
        parent.setPnodeType(FileManager.SJFJINST_PROCESS);
        if (sj_fjinstMapper.insertSelective(parent)<1){
            throw  new ZtgeoBizException(MsgManager.ENTRY_INSERT_FILE_TIAOMU_BAD);
        }
        for (SJ_Fjtm fjfile: sjFjtmList) {
            number++;
            //插入fjinst
            SJ_Fjinst fjinst=new SJ_Fjinst();
            fjinst.setCid(IDUtil.getFinstId());
            fjinst.setPid(fid);
            fjinst.setCname(fjfile.getEntryName());
            fjinst.setPnode(processId);
            fjinst.setEntryId(fjfile.getEntryId());
            fjinst.setCreateTime(new Date());
            fjinst.setOrderNumber(number);
            fjinst.setCreateBy(UserUtil.checkAndGetUser());
            fjinst.setCtype(fjfile.getEntryType());
            fjinst.setPnodeType(FileManager.SJFJINST_PROCESS);
            fjinst.setCkind(FileManager.SJFJINST_FILE);
            list.add(fjinst);
        }
        return list;
    }

    /**
     * 存放附件信息
     * @param
     * @param mid 模板id
     * @param processId 实列id
     * @return
     * @throws IOException
     */
    @Transactional(propagation = Propagation.REQUIRED ,isolation = Isolation.DEFAULT
            ,rollbackFor = Exception.class)
    public void   uploadCharacterComparison(String mid,String processId,List<SjHandleFlow> sjHandleFlows) throws IOException {
        try {
            //获得所有名称
            List<SJ_Fjinst> sj_fjinst= findFjtmByMid(mid,processId,sjHandleFlows);
            //循环读取
            for (SjHandleFlow prictures:sjHandleFlows) {
                for (SJ_Fjinst sj:sj_fjinst) {
                    if (prictures.getHandleName().equals(sj.getCname())){
                        Integer number=sj_fjinstMapper.selectMaxByPid(sj.getCid());
                        if (number==null){
                            number=0;
                        }
                        number++;
                        if (StringUtils.isNotEmpty(prictures.getPicInSite())){
                            MultipartFile file= Base64Test.base64ToMultipart(prictures.getPicInSite());
                            //新增条目附件和附件表
                            SJ_Fjinst fjinst=new SJ_Fjinst();
                            fjinst.setCid(IDUtil.getFinstId());
                            fjinst.setPid(sj.getCid());
                            fjinst.setCname(prictures.getHandleName()+MsgManager.PICINSITE);
                            fjinst.setPnode(processId);
                            fjinst.setEntryId(sj.getEntryId());
                            fjinst.setCreateTime(new Date());
                            fjinst.setOrderNumber(number);
                            fjinst.setCreateBy(UserUtil.checkAndGetUser());
                            fjinst.setCtype(sj.getCtype());
                            fjinst.setPnodeType(FileManager.SJFJINST_PROCESS);
                            fjinst.setCkind(FileManager.SJFJINST_WENJIAN);
                            fjinst.setFileId(IDUtil.getFinstId());
                            if (sj_fjinstMapper.insert(fjinst)<1){
                                throw  new ZtgeoBizException(MsgManager.ENTRY_INSERT_FILE_TIAOMU_BAD);
                            }
                            //附件表
                            SJ_Fjfile sj_fjfile=new SJ_Fjfile();
                            sj_fjfile.setFileId(fjinst.getFileId());
                            sj_fjfile.setFileExt(file.getOriginalFilename());
                            //取后缀
                            String hz=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                            sj_fjfile.setFileExt(hz);
                            sj_fjfile.setFileName(fjinst.getCname());
                            sj_fjfile.setContentType(FileManager.FILE_TP);
                            sj_fjfile.setFileSize(Long.toString(file.getSize()));
                            sj_fjfile.setFileSubmissionTime(new Date());
                            String returnValue=toFTPUploadComponent.uploadFile(file);
                            if (StringUtils.isEmpty(returnValue)){
                                throw new ZtgeoBizException(MsgManager.FILE_BAD);
                            }
                            if (StringUtils.isNotBlank(returnValue)){
                                sj_fjfile.setFtpPath(returnValue);
                            }
                            if (sj_fjfileMapper.insertSelective(sj_fjfile)<1){
                                throw  new ZtgeoBizException(MsgManager.ENTRY_INSERT_FILE_BAD);
                            }
                        }
                        if (StringUtils.isNotEmpty(prictures.getPicInIdentityCard())){
                            Integer maxNumber=sj_fjinstMapper.selectMaxByPid(sj.getCid());
                            if (maxNumber==null){
                                maxNumber=0;
                            }
                            maxNumber++;
                            MultipartFile file= Base64Test.base64ToMultipart(prictures.getPicInIdentityCard());
                            //新增条目附件和附件表
                            SJ_Fjinst fjinst=new SJ_Fjinst();
                            fjinst.setCid(IDUtil.getFinstId());
                            fjinst.setPid(sj.getCid());
                            fjinst.setCname(prictures.getHandleName()+MsgManager.PICINIDENTITYCARD);
                            fjinst.setPnode(processId);
                            fjinst.setEntryId(sj.getEntryId());
                            fjinst.setCreateTime(new Date());
                            fjinst.setOrderNumber(maxNumber);
                            fjinst.setCreateBy(UserUtil.checkAndGetUser());
                            fjinst.setCtype(sj.getCtype());
                            fjinst.setPnodeType(FileManager.SJFJINST_PROCESS);
                            fjinst.setCkind(FileManager.SJFJINST_WENJIAN);
                            fjinst.setFileId(IDUtil.getFinstId());
                            if (sj_fjinstMapper.insert(fjinst)<1){
                                throw  new ZtgeoBizException(MsgManager.ENTRY_INSERT_FILE_TIAOMU_BAD);
                            }
                            //附件表
                            SJ_Fjfile sj_fjfile=new SJ_Fjfile();
                            sj_fjfile.setFileId(fjinst.getFileId());
                            sj_fjfile.setFileExt(file.getOriginalFilename());
                            //取后缀
                            String hz=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                            sj_fjfile.setFileExt(hz);
                            sj_fjfile.setFileName(fjinst.getCname());
                            sj_fjfile.setContentType(FileManager.FILE_TP);
                            sj_fjfile.setFileSize(Long.toString(file.getSize()));
                            sj_fjfile.setFileSubmissionTime(new Date());
                            String returnValue=toFTPUploadComponent.uploadFile(file);
                            if (StringUtils.isNotBlank(returnValue)){
                                sj_fjfile.setFtpPath(returnValue);
                            }
                            if (sj_fjfileMapper.insertSelective(sj_fjfile)<1){
                                throw  new ZtgeoBizException(MsgManager.ENTRY_INSERT_FILE_BAD);
                            }
                        }
                    }
                }
                prictures.setHandleCreateBy(UserUtil.checkAndGetUser());
                prictures.setHandleCreateTime(new Date());
                prictures.setHandleId(IDUtil.getHandleId());
                prictures.setHandleProcess(processId);
            }
            sjHandleFlowMapper.insertList(sjHandleFlows);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer getCountOfEntryFile(String pnode,String entryId){
        return sj_fjinstMapper.selectCountOfEntryFile(pnode,entryId);
    }

}
