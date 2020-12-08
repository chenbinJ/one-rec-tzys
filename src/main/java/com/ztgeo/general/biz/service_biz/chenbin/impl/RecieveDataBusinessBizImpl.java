package com.ztgeo.general.biz.service_biz.chenbin.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.biz.service_biz.chenbin.RecieveDataBusinessBiz;
import com.ztgeo.general.component.chenbin.OtherComponent;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.component.penghao.StepComponent;
import com.ztgeo.general.component.pubComponent.ServiceAuthorizationComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.entity.service_data.sys_data.PermissionLevelResultEntity;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.chenbin.SJInfoManagerMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.mapper.chenbin.SvrManagerMapper;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.SysPubDataDealUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("recDataBiz")
@Transactional(rollbackFor = Exception.class)
public class RecieveDataBusinessBizImpl implements RecieveDataBusinessBiz {

    @Autowired
    private SJInfoManagerMapper sJInfoManagerMapper;
    @Autowired
    private StepComponent stepComponent;
    @Autowired
    private ServiceAuthorizationComponent svrAuthComponent;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ApproveComponent approveComponent;
    @Autowired
    private OtherComponent otherComponent;
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private SvrManagerMapper svrManagerMapper;

    @Override
    public String saveServiceData(SJ_Sjsq sjsq,String taskId) {

        if(StringUtils.isBlank(taskId)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SAVED_TASKID_IS_EMPTY);
        }

        /*
            鉴权（基础鉴权）
         */
        //验证 1. 是否本人签收并未提交，2. 是否具备流程步骤的写权限（包括空写）
        String stepId = checkUserAssignAndStepPower(taskId);
        //验证收件号是否为空
        if(sjsq.getReceiptNumber()==null || sjsq.getReceiptNumber().length()<=0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.RECEIPT_NUMBER_IS_NULL);
        }
        /*
            基础鉴权结束
         */

        //收件信息写入或更新
        SJ_Sjsq sj_sjsq_temp = sJInfoManagerMapper.selectSjsqBySjbh(sjsq.getReceiptNumber());
        if(sj_sjsq_temp!=null){ //更新
            if(BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin()) //管理员
                    || (
                            StringUtils.isNotBlank(sj_sjsq_temp.getReceiptMan()) &&
                            sj_sjsq_temp.getReceiptMan().equals(UserUtil.checkAndGetUser()) //收件人为当前登录用户(业务上的收件人判断)
                    )
                    || (
                            //用户是否具备特殊修改权限
                            svrAuthComponent.checkSpecialUpdatePower(UserUtil.getUerId())
                    )
            ) {
                sjsq.setReceiptMan(null);//设置不更新
                sJInfoManagerMapper.updateSjsq(sjsq);
                dealSaveExecuteDepart(sjsq.getExecuteDeparts());
            }
        }else{ //写入
            if(sjsq.getReceiptTime()==null){
                sjsq.setReceiptTime(new Date());
            }
            if(StringUtils.isBlank(sjsq.getReceiptMan())) {
                sjsq.setReceiptMan(UserUtil.checkAndGetUser());
            }
            sJInfoManagerMapper.insertSjsq(sjsq);
            dealSaveExecuteDepart(sjsq.getExecuteDeparts());
        }

        //不动产权利信息
        List<SJ_Info_Bdcqlxgxx> bdcqlxxs = sjsq.getImmovableRightInfoVoList();
        dealSaveCertifide(stepId,bdcqlxxs);

        //不动产抵押信息
        List<Sj_Info_Bdcdyxgxx> bdcdyxxs = sjsq.getImmovableCurrentMortgageInfoVoList();
        dealSaveBdcMortgage(stepId,bdcdyxxs);

        //交易合同信息
        Sj_Info_Jyhtxx jyhtxx = sjsq.getTransactionContractInfo();
        dealSaveJyhtxx(stepId,jyhtxx);

        //抵押合同信息
        Sj_Info_Dyhtxx dyhtxx = sjsq.getMortgageContractInfo();
        dealSaveDyhtxx(stepId,dyhtxx);

        //地税信息
        List<Sj_Info_Qsxx> qsxxs = sjsq.getTaxInfoVoList();
        dealSaveQsxx(stepId,qsxxs);

        //处理结果
        List<SJ_Info_Handle_Result> handleResults = sjsq.getHandleResultVoList();
        dealSaveHandleResults(stepId,handleResults);

        //无证不动产
        SJ_Info_Immovable immovable = sjsq.getImmovableSelf();
        dealSaveImmovable(stepId,immovable);

        //插入数据(存在数据时修改数据)
        return "保存成功";
    }

    //验证步骤服务权限
    private PermissionLevelResultEntity checkStepSvrPower(String stepId, SJ_Information info){
        PermissionLevelResultEntity powerlevel = svrAuthComponent.checkStepServicePowerWrite(stepId,info.getServiceCode());
        if(!powerlevel.isResult()){//验证步骤服务权限
            ZtgeoBizException e = new ZtgeoBizException(BizOrBizExceptionConstant.STEP_SERVICE_POWER_OUT);
            log.error(ErrorDealUtil.getErrorInfo(e));
            throw e;
        }
        return powerlevel;
    }
    //验证权证证书信息
    private void checkCertificateInfo(SJ_Info_Bdcqlxgxx bdcqlxx){
        if(StringUtils.isBlank(bdcqlxx.getImmovableCertificateNo())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.CERTIFICATE_NO_NOT_ENOUGH);//证书没有传入
        }
        if(
                StringUtils.isNotBlank(bdcqlxx.getImmovableCertificateNo())
                        && (StringUtils.isNotBlank(bdcqlxx.getHouseCertificateNo()) || StringUtils.isNotBlank(bdcqlxx.getLandCertificateNo()))
        ){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.CERTIFICATE_NO_TYPE_ERROR);//证书类型
        }
        if(StringUtils.isBlank(bdcqlxx.getCertificateType())){//证书类型为空
            throw new ZtgeoBizException(BizOrBizExceptionConstant.CERTIFICATE_TYPE_IS_NULL);
        }else if(
                !(
                        bdcqlxx.getCertificateType().equals(BizOrBizExceptionConstant.CERTIFICATE_TYPE_OF_BDC) ||
                        bdcqlxx.getCertificateType().equals(BizOrBizExceptionConstant.CERTIFICATE_TYPE_OF_FC) ||
                        bdcqlxx.getCertificateType().equals(BizOrBizExceptionConstant.CERTIFICATE_TYPE_OF_TD)
                )
        ){//超出合法定义的证书类型
            throw new ZtgeoBizException(BizOrBizExceptionConstant.CERTIFICATE_TYPE_NOT_LEGITIMATE);
        }
    }

    //验证不动产抵押信息
    private void checkCurrentMortgage(Sj_Info_Bdcdyxgxx bdcdyxgxx){
        String mortgageCertificateNo = bdcdyxgxx.getMortgageCertificateNo();
        if(StringUtils.isBlank(mortgageCertificateNo)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.MORTGAGE_NO_NOT_ENOUGH);
        }
    }

    //验证空写是否本人提交
    private boolean WriteNotNullSaveSelfOrNot(SJ_Information info){
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())) {//不是管理员
            if(!(info.getPreservationMan()!=null && info.getPreservationMan().equals(UserUtil.checkAndGetUser()))){//不是本人
                return false;
            }
        }
        return true;
    }

    //验证步骤任务签收及步骤服务是否具有权限
    private String checkUserAssignAndStepPower(String taskId){
        //是否本人签收提交保存
        if(!approveComponent.findPowerByTask(taskId)){//不是本人签收或任务已提交并且不是管理员
            throw new ZtgeoBizException(BizOrBizExceptionConstant.TASK_STATUS_EXCEPTION);
        }
        //获取任务之后
        String stepId = (String) otherComponent.getSteps(taskId);
        if(StringUtils.isBlank(stepId)){//未对应任务步骤信息
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SAVED_TASK_MAPPING_STEP_IS_EMPTY);
        }
        String userId = UserUtil.getUerId();
        if(!svrAuthComponent.checkStepUserStepPower(stepId,userId)){//不具备岗位步骤写权限
            throw new ZtgeoBizException(BizOrBizExceptionConstant.USER_STEP_POWER_OUT);
        }
        return stepId;
    }

    //保存执行部门信息
    private void dealSaveExecuteDepart(List<SJ_Execute_depart> departs){
        if(departs!=null){
            for(SJ_Execute_depart depart:departs){
                if(StringUtils.isBlank(depart.getExecuteDepart())){
                    continue;
                }
                if(StringUtils.isNotBlank(depart.getId())){
                    int count = sJInfoManagerMapper.updateExeDepart(depart);
                    if(count<1){
                        log.warn("申请更新的"+depart.getId()+"号，收件编号为"+depart.getReceiptNumber()+"，具体部门为"+depart.getExecuteDepart()+"的执行部门记录不存在");
                    }
                }else{
                    depart.setId(IDUtil.getCommonId());
                    sJInfoManagerMapper.insertExeDepart(depart);
                }
            }
        }
    }

    //保存不动产权利信息
    private void dealSaveCertifide(String stepId,List<SJ_Info_Bdcqlxgxx> bdcqlxxs){
        if(bdcqlxxs!=null){
            //保存权证信息数据
            for(SJ_Info_Bdcqlxgxx bdcqlxx:bdcqlxxs){
                checkCertificateInfo(bdcqlxx);//验证业务数据,包括证书类型和证书号码
                PermissionLevelResultEntity powerlevel = checkStepSvrPower(stepId,bdcqlxx);//验证步骤服务权限
                List<SJ_Info_Bdcqlxgxx> exist_bdcqls = sJInfoManagerMapper.selectBdcqlxxs(bdcqlxx.getReceiptNumber(),bdcqlxx.getServiceCode());
                if(
                        exist_bdcqls!=null
                        && exist_bdcqls.size()>0
                        && powerlevel.getPermissionLevel().equals(
                                BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE
                        )
                ){//验证空写
                    boolean isOrNot = true;
                    for(SJ_Info_Bdcqlxgxx exist_bdcql:exist_bdcqls){
                        isOrNot = isOrNot&&WriteNotNullSaveSelfOrNot(exist_bdcql);
                    }
                    if(!isOrNot) {
                        continue;
                    }
                }

                //查询该收件号是否已收该证号
                SJ_Info_Bdcqlxgxx bdcqlxx_temp =  sJInfoManagerMapper.selectBdcqlxxBySjbhAndBDCCertNo(bdcqlxx.getReceiptNumber(),bdcqlxx.getImmovableCertificateNo(),bdcqlxx.getServiceCode());

                //保存证书数据
                if(bdcqlxx.getInfoId()==null || bdcqlxx.getInfoId().length()<=0){
                    if(bdcqlxx_temp!=null){//证书重复写入
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.INPUT_SAME_CERT_NO);
                    }
                    bdcqlxx.setInfoId(IDUtil.getInfoId());
                    bdcqlxx.setInsertTime(new Date());
                    if(StringUtils.isBlank(bdcqlxx.getPreservationMan())){
                        bdcqlxx.setPreservationMan(UserUtil.checkAndGetUser());
                    }
                    sJInfoManagerMapper.insertBdcqlxx(bdcqlxx);
                }else{
                    if(bdcqlxx_temp!=null && !bdcqlxx.getInfoId().equals(bdcqlxx_temp.getInfoId())){//证书重复写入
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.INPUT_SAME_CERT_NO);
                    }
                    int i = sJInfoManagerMapper.updateBdcqlxx(bdcqlxx);
                    if(i<1){//受影响的行数不为1时抛出异常
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.UPDATE_BDC_CERT_NO_NOT_EXIST);
                    }
                }

                //权证关联的不动产信息的保存
                List<SJ_Bdc_Gl> bdcgls = bdcqlxx.getGlImmovableVoList();
                //权利人关联
                List<SJ_Qlr_Gl> qlrgls = bdcqlxx.getGlObligeeVoList();
                //义务人关联
                List<SJ_Qlr_Gl> ywrgls = bdcqlxx.getGlObligorVoList();

                dealSaveBdcGls(bdcgls,bdcqlxx.getInfoId());
                dealSaveQlrGls(qlrgls,bdcqlxx.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);
                dealSaveQlrGls(ywrgls,bdcqlxx.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_YWR);

            }
        }
    }
    //保存不动产抵押信息
    private void dealSaveBdcMortgage(String stepId,List<Sj_Info_Bdcdyxgxx> bdcdyxxs){
        if(bdcdyxxs!=null){
            //不动产抵押信息保存
            for(Sj_Info_Bdcdyxgxx bdcdyxx:bdcdyxxs){
                checkCurrentMortgage(bdcdyxx);//验证抵押证明号
                PermissionLevelResultEntity powerlevel = checkStepSvrPower(stepId,bdcdyxx);//验证步骤服务权限
                List<Sj_Info_Bdcdyxgxx> exist_bdcdys = sJInfoManagerMapper.selectBdcdyxxs(bdcdyxx.getReceiptNumber(),bdcdyxx.getServiceCode());
                if(
                        exist_bdcdys!=null
                        && exist_bdcdys.size()>0
                        && powerlevel.getPermissionLevel().equals(
                                BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE
                        )
                ){
                    boolean isOrNot = true;
                    for(Sj_Info_Bdcdyxgxx exist_bdcdl:exist_bdcdys){
                        isOrNot = isOrNot&&WriteNotNullSaveSelfOrNot(exist_bdcdl);
                    }
                    if(!isOrNot) {
                        continue;
                    }
                }

                //查询该收件号是否已收该抵押证明号
                Sj_Info_Bdcdyxgxx bdcdyxx_temp = sJInfoManagerMapper.selectBdcdyxxBySjbhAndMortgageNo(bdcdyxx.getReceiptNumber(),bdcdyxx.getMortgageCertificateNo(),bdcdyxx.getServiceCode());

                //保存抵押证明数据
                if(bdcdyxx.getInfoId()==null || bdcdyxx.getInfoId().length()<=0){
                    if(bdcdyxx_temp!=null){
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.INPUT_SAME_MORTGAGE_CERT_NO);
                    }
                    bdcdyxx.setInfoId(IDUtil.getInfoId());
                    bdcdyxx.setInsertTime(new Date());
                    if(StringUtils.isBlank(bdcdyxx.getPreservationMan())){
                        bdcdyxx.setPreservationMan(UserUtil.checkAndGetUser());
                    }
                    sJInfoManagerMapper.insertBdcdyxx(bdcdyxx);
                } else {
                    if(bdcdyxx_temp!=null && !bdcdyxx.getInfoId().equals(bdcdyxx_temp.getInfoId())){
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.INPUT_SAME_MORTGAGE_CERT_NO);
                    }
                    int i = sJInfoManagerMapper.updateBdcdyxx(bdcdyxx);
                    if(i<1){//受影响的行数不为1时抛出异常
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.UPDATE_BDC_MORTGAGE_NO_NOT_EXIST);
                    }
                }

                //权证关联的不动产信息的保存
                List<SJ_Bdc_Gl> bdcgls = bdcdyxx.getGlImmovableVoList();
                //抵押权人关联
                List<SJ_Qlr_Gl> dyqrgls = bdcdyxx.getGlMortgageHolderVoList();
                //抵押人关联
                List<SJ_Qlr_Gl> dyrgls = bdcdyxx.getGlMortgagorVoList();

                dealSaveBdcGls(bdcgls,bdcdyxx.getInfoId());//未完成
                dealSaveQlrGls(dyqrgls,bdcdyxx.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);//未完成
                dealSaveQlrGls(dyrgls,bdcdyxx.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);//未完成
            }
        }
    }
    //保存交易合同信息
    private void dealSaveJyhtxx(String stepId,Sj_Info_Jyhtxx jyhtxx){
        if(jyhtxx!=null){
            PermissionLevelResultEntity powerlevel = checkStepSvrPower(stepId,jyhtxx);//验证步骤服务权限
            List<Sj_Info_Jyhtxx> exist_jyhts = sJInfoManagerMapper.selectJyhtxxs(jyhtxx.getReceiptNumber(),jyhtxx.getServiceCode());
            if(
                    exist_jyhts!=null
                    && exist_jyhts.size()>0
                    && powerlevel.getPermissionLevel().equals(
                        BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE
                    )
            ){
                boolean isOrNot = true;
                for(Sj_Info_Jyhtxx exist_jyht:exist_jyhts){
                    isOrNot = isOrNot&&WriteNotNullSaveSelfOrNot(exist_jyht);
                }
                if(!isOrNot) {
                    return;
                }
            }

            Sj_Info_Jyhtxx jyhtxx_temp = sJInfoManagerMapper.selectJyhtxxBySqbh(jyhtxx.getReceiptNumber());
            if(StringUtils.isBlank(jyhtxx.getInfoId())){
                if(jyhtxx_temp!=null){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.TRANSACTION_CONTRACT_IS_EXIST);
                }
                jyhtxx.setInfoId(IDUtil.getInfoId());
                jyhtxx.setInsertTime(new Date());
                if(StringUtils.isBlank(jyhtxx.getPreservationMan())){
                    jyhtxx.setPreservationMan(UserUtil.checkAndGetUser());
                }
                sJInfoManagerMapper.insertJyhtxx(jyhtxx);
            }else{
                if(jyhtxx_temp!=null && !jyhtxx_temp.getInfoId().equals(jyhtxx.getInfoId())){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.TRANSACTION_CONTRACT_IS_EXIST);
                }
                int i = sJInfoManagerMapper.updateJyhtxx(jyhtxx);
                if(i<1){//受影响的行数不为1时抛出异常
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.UPDATE_TRANSACTION_CONTRACT_NOT_EXIST);
                }
            }
            //权证关联的不动产信息的保存
            List<SJ_Bdc_Gl> bdcgls = jyhtxx.getGlImmovableVoList();
            //权利人关联
            List<SJ_Qlr_Gl> buyergls = jyhtxx.getGlHouseBuyerVoList();
            //义务人关联
            List<SJ_Qlr_Gl> sellergls = jyhtxx.getGlHouseSellerVoList();

            dealSaveBdcGls(bdcgls,jyhtxx.getInfoId());//未完成
            dealSaveQlrGls(buyergls,jyhtxx.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_GFZ);//未完成
            dealSaveQlrGls(sellergls,jyhtxx.getInfoId(),BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_SFZ);//未完成
        }
    }
    //保存银行抵押合同信息
    private void dealSaveDyhtxx(String stepId,Sj_Info_Dyhtxx dyhtxx){
        if(dyhtxx!=null){
            PermissionLevelResultEntity powerlevel = checkStepSvrPower(stepId,dyhtxx);//验证步骤服务权限
            List<Sj_Info_Dyhtxx> exist_dyhts = sJInfoManagerMapper.selectDyhtxxs(dyhtxx.getReceiptNumber(),dyhtxx.getServiceCode());
            if(
                    exist_dyhts!=null
                    && exist_dyhts.size()>0
                    && powerlevel.getPermissionLevel().equals(
                        BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE
                    )
            ){
                boolean isOrNot = true;
                for(Sj_Info_Dyhtxx exist_dyht:exist_dyhts){
                    isOrNot = isOrNot&&WriteNotNullSaveSelfOrNot(exist_dyht);
                }
                if(!isOrNot) {
                    return;
                }
            }
            Sj_Info_Dyhtxx dyhtxx_temp = sJInfoManagerMapper.selectDyhtxxBySqbh(dyhtxx.getReceiptNumber());
            if(StringUtils.isBlank(dyhtxx.getInfoId())){
                if(dyhtxx_temp!=null){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.MORTGAGE_CONTRACT_IS_EXIST);
                }
                dyhtxx.setInfoId(IDUtil.getInfoId());
                dyhtxx.setInsertTime(new Date());
                if(StringUtils.isBlank(dyhtxx.getPreservationMan())){
                    dyhtxx.setPreservationMan(UserUtil.checkAndGetUser());
                }
                sJInfoManagerMapper.insertDyhtxx(dyhtxx);
            }else{
                if(dyhtxx_temp!=null && !dyhtxx_temp.getInfoId().equals(dyhtxx.getInfoId())){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.MORTGAGE_CONTRACT_IS_EXIST);
                }
                int i = sJInfoManagerMapper.updateDyhtxx(dyhtxx);
                if(i<1){//受影响的行数不为1时抛出异常
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.UPDATE_MORTGAGE_CONTRACT_NOT_EXIST);
                }
            }
            //权证关联的不动产信息的保存
            List<SJ_Bdc_Gl> bdcgls = dyhtxx.getGlImmovableVoList();
            //权利人关联
            List<SJ_Qlr_Gl> dyqrgls = dyhtxx.getGlMortgageHolderVoList();
            //义务人关联
            List<SJ_Qlr_Gl> dyrgls = dyhtxx.getGlMortgagorVoList();
            String infoId = dyhtxx.getInfoId();
            dealSaveBdcGls(bdcgls,infoId);//未完成
            dealSaveQlrGls(dyqrgls,infoId,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYQR);//未完成
            dealSaveQlrGls(dyrgls,infoId,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_DYR);//未完成
        }
    }
    //保存地税信息
    private void dealSaveQsxx(String stepId,List<Sj_Info_Qsxx> qsxxs){
        if(qsxxs!=null){
            for(Sj_Info_Qsxx qsxx:qsxxs) {
                PermissionLevelResultEntity powerlevel = checkStepSvrPower(stepId, qsxx);//验证步骤服务权限
                List<Sj_Info_Qsxx> exist_qsxxs = sJInfoManagerMapper.selectDsxxs(qsxx.getReceiptNumber(), qsxx.getServiceCode());
                if (
                        exist_qsxxs != null
                                && exist_qsxxs.size() > 0
                                && powerlevel.getPermissionLevel().equals(
                                BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE
                        )
                ) {
                    boolean isOrNot = true;
                    for (Sj_Info_Qsxx exist_qsxx : exist_qsxxs) {
                        isOrNot = isOrNot && WriteNotNullSaveSelfOrNot(exist_qsxx);
                    }
                    if (!isOrNot) {
                        return;
                    }
                }
                Sj_Info_Qsxx qsxx_temp = sJInfoManagerMapper.selectQsxxBySqbh(
                        qsxx.getReceiptNumber(),
                        qsxx.getServiceCode(),
                        qsxx.getXtsphm(),
                        qsxx.getNsrsbh());
                if (StringUtils.isBlank(qsxx.getInfoId())) {
                    if (qsxx_temp != null) {
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.TAX_INFO_IS_EXIST);
                    }
                    qsxx.setInfoId(IDUtil.getInfoId());
                    qsxx.setInsertTime(new Date());
                    if (StringUtils.isBlank(qsxx.getPreservationMan())) {
                        qsxx.setPreservationMan(UserUtil.checkAndGetUser());
                    }
                    System.out.println("错误项："+qsxx.getXtsphm());
                    sJInfoManagerMapper.insertQsxx(qsxx);
                } else {
                    if (qsxx_temp != null && !qsxx_temp.getInfoId().equals(qsxx.getInfoId())) {
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.TAX_INFO_IS_EXIST);
                    }
                    int i = sJInfoManagerMapper.updateQsxx(qsxx);
                    if (i < 1) {//受影响的行数不为1时抛出异常
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.UPDATE_TAX_INFO_NOT_EXIST);
                    }
                }
            }
        }
    }
    //保存各系统处理结果信息
    private void dealSaveHandleResults(String stepId,List<SJ_Info_Handle_Result> handleResults){
        if(handleResults!=null){
            for(SJ_Info_Handle_Result handleResult:handleResults){
                PermissionLevelResultEntity powerlevel = checkStepSvrPower(stepId,handleResult);//验证步骤服务权限
                List<SJ_Info_Handle_Result> exist_handleReslts = sJInfoManagerMapper.selectHandleResults(handleResult.getReceiptNumber(),handleResult.getServiceCode());
                if(//处理空写
                        exist_handleReslts!=null
                        && exist_handleReslts.size()>0
                        && powerlevel.getPermissionLevel().equals(
                            BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE
                        )
                ){
                    boolean isOrNot = true;
                    for(SJ_Info_Handle_Result exist_handleReslt:exist_handleReslts){
                        isOrNot = isOrNot&&WriteNotNullSaveSelfOrNot(exist_handleReslt);
                    }
                    if(!isOrNot) {
                        continue;
                    }
                }
                //处理结果是可以有多条，故不加验证
//                SJ_Info_Handle_Result handleResult_temp = sJInfoManagerMapper.selectHandleResultBySqbhAndServiceCode(handleResult.getReceiptNumber(),handleResult.getServiceCode());
                if(StringUtils.isBlank(handleResult.getInfoId())){
//                    if(handleResult_temp!=null){
//                        throw new ZtgeoBizException(BizOrBizExceptionConstant.TAX_INFO_IS_EXIST);
//                    }
                    if(exist_handleReslts!=null && exist_handleReslts.size()>0){
                        handleResult.setInfoId(exist_handleReslts.get(0).getInfoId());
                        handleResult.setOldNumber(exist_handleReslts.get(0).getOldNumber());
                        handleResult.setProvideUnit(exist_handleReslts.get(0).getProvideUnit());
                        handleResult.setPreservationMan(exist_handleReslts.get(0).getPreservationMan());
                        handleResult.setDataComeFromMode(exist_handleReslts.get(0).getDataComeFromMode());
                        int i = sJInfoManagerMapper.updateHandleResult(handleResult);
                        if(i<1){//受影响的行数不为1时抛出异常
                            throw new ZtgeoBizException(BizOrBizExceptionConstant.UPDATE_HANDLE_RESULT_NOT_EXIST);
                        }
                    }else {
                        handleResult.setInfoId(IDUtil.getInfoId());
                        handleResult.setInsertTime(new Date());
                        if (StringUtils.isBlank(handleResult.getPreservationMan())) {
                            handleResult.setPreservationMan(UserUtil.checkAndGetUser());
                        }
                        sJInfoManagerMapper.insertHandleResult(handleResult);
                    }
                }else{
//                    if(qsxx_temp!=null && ! qsxx_temp.getInfoId().equals(qsxx.getInfoId())){
//                        throw new ZtgeoBizException(BizOrBizExceptionConstant.TAX_INFO_IS_EXIST);
//                    }
                    int i = sJInfoManagerMapper.updateHandleResult(handleResult);
                    if(i<1){//受影响的行数不为1时抛出异常
                        throw new ZtgeoBizException(BizOrBizExceptionConstant.UPDATE_HANDLE_RESULT_NOT_EXIST);
                    }
                }
            }
        }
    }
    //处理不动产
    public void dealSaveImmovable(String stepId,SJ_Info_Immovable immovable){
        if(immovable!=null){
            PermissionLevelResultEntity powerlevel = checkStepSvrPower(stepId,immovable);//验证步骤服务权限
            List<SJ_Info_Immovable> exist_immovs = sJInfoManagerMapper.selectImmovs(immovable.getReceiptNumber(),immovable.getServiceCode());
            if(
                    exist_immovs!=null
                            && exist_immovs.size()>0
                            && powerlevel.getPermissionLevel().equals(
                                BizOrBizExceptionConstant.POWER_LEVEL_NULL_WRITE
                            )
            ){
                boolean isOrNot = true;
                for(SJ_Info_Immovable exist_immov:exist_immovs){
                    isOrNot = isOrNot&&WriteNotNullSaveSelfOrNot(exist_immov);
                }
                if(!isOrNot) {
                    return;
                }
            }
            SJ_Info_Immovable immov_temp = sJInfoManagerMapper.selectImmovBySqbh(immovable.getReceiptNumber());
            if(StringUtils.isBlank(immovable.getInfoId())){
                if(immov_temp!=null){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_IS_EXIST);
                }
                immovable.setInfoId(IDUtil.getInfoId());
                immovable.setInsertTime(new Date());
                if(StringUtils.isBlank(immovable.getPreservationMan())){
                    immovable.setPreservationMan(UserUtil.checkAndGetUser());
                }
                sJInfoManagerMapper.insertImmovable(immovable);
            }else{
                if(immov_temp!=null && !immov_temp.getInfoId().equals(immovable.getInfoId())){//infoID传入的和已经存在的不一致
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.IMMOVABLE_IS_EXIST);
                }
                int i = sJInfoManagerMapper.updateImmovable(immovable);
                if(i<1){//受影响的行数不为1时抛出异常
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.UPDATE_IMMOVABLE_NOT_EXIST);
                }
            }
            //权证关联的不动产信息的保存
            List<SJ_Bdc_Gl> bdcgls = immovable.getGlImmovableVoList();
            //权利人关联
            List<SJ_Qlr_Gl> qlrgls = immovable.getGlObligeeVoList();
            //代理人关联
            List<SJ_Qlr_Gl> dlrgls = immovable.getGlAgentVoList();
            String infoId = immovable.getInfoId();
            dealSaveBdcGls(bdcgls,infoId);//未完成
            dealSaveQlrGls(qlrgls,infoId,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR);//未完成
            dealSaveQlrGls(dlrgls,infoId,BizOrBizExceptionConstant.OBLIGEE_TYPE_OF_QLR_DLR);
        }
    }
    //处理不动产关联信息
    private void dealSaveBdcGls(List<SJ_Bdc_Gl> bdcgls,String infoId){
        //查现有的关联数据
        List<SJ_Bdc_Gl> bdcgls_exist = sJInfoManagerMapper.selectBdcglByInfoId(infoId);

        if(bdcgls!=null && bdcgls.size()>0){
            //判断多余项并确定是否需要删除
            if(bdcgls_exist!=null) {
                for (SJ_Bdc_Gl bdcgl_exist : bdcgls_exist) {
                    if(isOutBdcgl(bdcgl_exist,bdcgls)){
                        deleteImmovExist(bdcgl_exist);
                        sJInfoManagerMapper.deleteBDCGLById(bdcgl_exist.getRelationId());
                    }
                }
            }
            bdcgls_exist = sJInfoManagerMapper.selectBdcglByInfoId(infoId);
            //去除当前存在的重复项
            List<Integer> sameIndexs = findSameBdcGlSelf(bdcgls_exist);
            for(Integer index:sameIndexs){//删除重复项，注意这里按照相同info不同immovable来删除
                deleteImmovExist(bdcgls_exist.get(index));
                sJInfoManagerMapper.deleteBDCGLById(bdcgls_exist.get(index).getRelationId());
            }
            bdcgls_exist = sJInfoManagerMapper.selectBdcglByInfoId(infoId);
            System.out.println("处理后的数据条数为："+bdcgls_exist.size()+"\n具体为："+JSONArray.toJSONString(bdcgls_exist));
            //执行插入或更新（插入前判断重复关系）
            for(SJ_Bdc_Gl bdcgl:bdcgls){
                SJ_Bdc_Gl samegl = isCopyBdc(bdcgl,bdcgls_exist);
                String immovableId = null;
                //不动产信息入库
                if(bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)){
                    SJ_Bdc_Zd_Info zdInfo = bdcgl.getZdInfo();
                    if(zdInfo!=null) {
                        if (StringUtils.isNotBlank(zdInfo.getParcelId())) {
                            //验证是否已保存该净地信息
                            if(sJInfoManagerMapper.selectCountOfParcel(zdInfo.getParcelId())>0) {
                                sJInfoManagerMapper.updateZdInfo(zdInfo);
                            } else {
                                if(samegl!=null){//存在重复
                                    SJ_Bdc_Zd_Info zd_info_exit = sJInfoManagerMapper.selectBdcJDByInfoId(samegl.getImmovableId());
                                    zdInfo.setParcelId(zd_info_exit.getParcelId());
                                    sJInfoManagerMapper.updateZdInfo(zdInfo);
                                }else {
                                    sJInfoManagerMapper.insertZdInfo(zdInfo);
                                }
                            }
                        } else {
                            if(samegl!=null){//存在重复
                                SJ_Bdc_Zd_Info zd_info_exit = sJInfoManagerMapper.selectBdcJDByInfoId(samegl.getImmovableId());
                                zdInfo.setParcelId(zd_info_exit.getParcelId());
                                sJInfoManagerMapper.updateZdInfo(zdInfo);
                            }else {
                                zdInfo.setParcelId(IDUtil.getImmovableId());//注入主键
                                sJInfoManagerMapper.insertZdInfo(zdInfo);
                            }
                        }
                        immovableId = zdInfo.getParcelId();
                    }
                }else{
                    SJ_Bdc_Fw_Info fwInfo = bdcgl.getFwInfo();
                    if(fwInfo!=null) {
                        if (StringUtils.isNotBlank(fwInfo.getHouseId())) {
                            //验证是否已保存该房地信息
                            if(sJInfoManagerMapper.selectCountOfHouse(fwInfo.getHouseId())>0) {
                                sJInfoManagerMapper.updateFdInfo(fwInfo);
                            } else {
                                if(samegl!=null){//存在重复
                                    SJ_Bdc_Fw_Info fw_exit = sJInfoManagerMapper.selectBdcFDByInfoId(samegl.getImmovableId());
                                    fwInfo.setHouseId(fw_exit.getHouseId());
                                    sJInfoManagerMapper.updateFdInfo(fwInfo);
                                }else{
                                    sJInfoManagerMapper.insertFdInfo(fwInfo);
                                }
                            }
                        } else {
                            if(samegl!=null){//存在重复
                                SJ_Bdc_Fw_Info fw_exit = sJInfoManagerMapper.selectBdcFDByInfoId(samegl.getImmovableId());
                                fwInfo.setHouseId(fw_exit.getHouseId());
                                if(samegl.getRelationId().equals("RELAT-20191022-DC44F4DDE34")){
                                    System.out.println("不同的实体："+ JSONObject.toJSONString(fwInfo));
                                }
                                sJInfoManagerMapper.updateFdInfo(fwInfo);
                            }else {
                                fwInfo.setHouseId(IDUtil.getImmovableId());//注入主键
                                sJInfoManagerMapper.insertFdInfo(fwInfo);
                            }
                        }
                        immovableId = fwInfo.getHouseId();
                    }
                }
                //保存关联信息
                if(StringUtils.isNotBlank(bdcgl.getRelationId())){
                    sJInfoManagerMapper.updateBdcgl(bdcgl);
                }else{
                    if (StringUtils.isBlank(bdcgl.getImmovableId())) {
                        bdcgl.setImmovableId(immovableId);
                    }
                    if (StringUtils.isBlank(bdcgl.getInfoId())) {
                        bdcgl.setInfoId(infoId);
                    }
                    if(samegl!=null){
                        bdcgl.setRelationId(samegl.getRelationId());
                        sJInfoManagerMapper.updateBdcgl(bdcgl);
                    }else {
                        bdcgl.setRelationId(IDUtil.getStepGlId());
                        sJInfoManagerMapper.insertBdcgl(bdcgl);
                    }
                }
            }
        } else {//传入的不动产关联数据为空
            //删除之
            sJInfoManagerMapper.deleteBDCGlByInfoId(infoId);
            if(bdcgls_exist!=null) {
                for (SJ_Bdc_Gl bdcgl_exist : bdcgls_exist) {
                    deleteImmovExist(bdcgl_exist);
                }
            }
        }
    }
    //处理权利人关联信息
    private void dealSaveQlrGls(List<SJ_Qlr_Gl> qlrgls,String infoId,String obligeeType){
        List<SJ_Qlr_Gl> qlrgls_exist = sJInfoManagerMapper.selectQlrGlByInfoId(infoId,obligeeType);
        if(qlrgls!=null && qlrgls.size()>0){
            //判断多余项并确定是否需要删除
            if(qlrgls_exist!=null) {
                for (SJ_Qlr_Gl qlrgl_exist : qlrgls_exist) {
                    if(isOutQlrgl(qlrgl_exist,qlrgls)){
                        RemoveExistQlr(qlrgl_exist);
                        sJInfoManagerMapper.deleteQlrGlById(qlrgl_exist.getRelationId());
                    }
                }
            }
            qlrgls_exist = sJInfoManagerMapper.selectQlrGlByInfoId(infoId,obligeeType);
            //去除当前存在的重复项
            List<Integer> sameIndexs = findSameQlrGlSelf(qlrgls_exist);
            for(Integer index:sameIndexs){//删除重复项，注意这里按照相同info不同immovable来删除
                RemoveExistQlr(qlrgls_exist.get(index));
                sJInfoManagerMapper.deleteQlrGlById(qlrgls_exist.get(index).getRelationId());
            }
            qlrgls_exist = sJInfoManagerMapper.selectQlrGlByInfoId(infoId,obligeeType);
            System.out.println("处理后的‘权利人’数据条数为："+qlrgls_exist.size()+"\n具体为："+JSONArray.toJSONString(qlrgls_exist));
            for(SJ_Qlr_Gl qlrgl:qlrgls){
                SJ_Qlr_Gl samegl = isCopyQlr(qlrgl,qlrgls_exist);
                SJ_Qlr_Info qlr = qlrgl.getRelatedPerson();
                if(qlr!=null){
                    System.out.println(qlr.getObligeeId());
                    if(StringUtils.isNotBlank(qlr.getObligeeId())){
                        //验证是否已保存该权利人，防止空写入人员信息
                        if(sJInfoManagerMapper.selectCountOfObligee(qlr.getObligeeId())>0) {
                            sJInfoManagerMapper.updateQlr(qlr);
                        } else {
                            if(samegl!=null) {
                                qlr.setObligeeId(samegl.getObligeeId());
                                sJInfoManagerMapper.updateQlr(qlr);
                            }else{
                                sJInfoManagerMapper.insertQlr(qlr);
                            }
                        }
                    }else{
                        if(samegl!=null){
                            qlr.setObligeeId(samegl.getObligeeId());
                            sJInfoManagerMapper.updateQlr(qlr);
                        }else {
                            qlr.setObligeeId(IDUtil.getObligeeId());//注入主键
                            sJInfoManagerMapper.insertQlr(qlr);
                        }
                    }
                }
                if(StringUtils.isNotBlank(qlrgl.getRelationId())){
                    sJInfoManagerMapper.updateQlrgl(qlrgl);
                }else{
                    if(StringUtils.isBlank(qlrgl.getInfoId())) {
                        qlrgl.setInfoId(infoId);
                    }
                    if(StringUtils.isBlank(qlrgl.getObligeeId())) {
                        qlrgl.setObligeeId(qlr.getObligeeId());
                    }
                    if(samegl!=null){
                        qlrgl.setRelationId(samegl.getRelationId());
                        sJInfoManagerMapper.updateQlrgl(qlrgl);
                    }else {
                        qlrgl.setRelationId(IDUtil.getStepGlId());
                        sJInfoManagerMapper.insertQlrgl(qlrgl);
                    }
                }
            }
        }else{
            //删除之
            if(qlrgls_exist!=null){
                for(SJ_Qlr_Gl qlrgl_exist:qlrgls_exist){
                    RemoveExistQlr(qlrgl_exist);
                }
            }
            sJInfoManagerMapper.deleteQLRGlByInfoIdAndObligeeType(infoId,obligeeType);
        }
    }

    @Override
    public SJ_Sjsq findSjsqServiceDataByCode(String sqbh, String serviceCode, String serviceDataTo) {
        if (sqbh==null || sqbh.length()<=0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_DATA_BUT_RECEIPTNUM_IS_NULL);
        }
        SJ_Sjsq sjsq = sJInfoManagerMapper.selectSjsqBySjbh(sqbh);
        if(!(StringUtils.isBlank(serviceCode) && StringUtils.isBlank(serviceDataTo))){
            //输入异常
            serviceDataTo = SysPubDataDealUtil.checkServiceInfo(serviceCode,serviceDataTo);//验证这两项不同时为空
            //判断办件是否存在
            if(sjsq == null){
                throw new ZtgeoBizException(BizOrBizExceptionConstant.TARGET_RECEIPT_IS_NULL);
            }
            //执行查询
            sjsq = dealGetServiceData(sjsq,serviceCode,serviceDataTo);
        }
        sjsq = SysPubDataDealUtil.getRespSjsq(sjsq);
        return sjsq;
    }

    @Override
    public String removeServiceData(String infoId, String serviceCode,String taskId, String serviceDataTo) {
        /*
            鉴权
         */
        //获取并检查操作表标识
        serviceDataTo = SysPubDataDealUtil.checkServiceInfo(serviceCode,serviceDataTo);

        //查当前Info对应的办件ReceiptNumber并判断，根据serviceDataTo查找
        SJ_Information info = findInfomationByInfoIdAndServiceDataTo(infoId,serviceDataTo);
        if(info==null || !info.getServiceCode().equals(serviceCode)){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.NO_INFORMATION_OF_OUR_WANT);
        }

        //taskId 和 当前登录用户查询是否签收有当前正在执行的任务
        //验证 1. 是否本人签收并未提交，2. 是否具备流程步骤的写权限（包括空写）
        String stepId = checkUserAssignAndStepPower(taskId);
        //验证步骤服务权限,空写时需要验证是否本人提交
        PermissionLevelResultEntity powerlevel = checkStepSvrPower(stepId,info);
        if(powerlevel.isResult()){
            if(!info.getPreservationMan().equals(UserUtil.checkAndGetUser()) &&
                    !BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())){
                throw new ZtgeoBizException(BizOrBizExceptionConstant.NO_POWER_DELETE_OTHER_PERSON_INFO);
            }
        }
        /*
            鉴权结束
         */

        /*
            执行删除
         */
        //查询对应不动产及关系表数据并删除
        List<SJ_Bdc_Gl> bdc_gls = sJInfoManagerMapper.selectBdcglByInfoId(infoId);
        for(SJ_Bdc_Gl bdc_gl:bdc_gls){
            if(StringUtils.isBlank(bdc_gl.getImmovableType())){
                throw new ZtgeoBizException(BizOrBizExceptionConstant.DELETE_DEFALT_REASON_BY_IMMOVABLE_TYPE_NULL);
            }
            if(bdc_gl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_FD)){
                sJInfoManagerMapper.deleteFWById(bdc_gl.getImmovableId());
            }else if(bdc_gl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)){
                sJInfoManagerMapper.deleteZDById(bdc_gl.getImmovableId());
            }else{
                throw new ZtgeoBizException(BizOrBizExceptionConstant.DELETE_DEFALT_REASON_BY_IMMOVABLE_TYPE_NOT_CLEAR);
            }
            sJInfoManagerMapper.deleteBDCGLById(bdc_gl.getRelationId());
        }
        //查询对应权利人及关系表数据并删除
        List<SJ_Qlr_Gl> qlr_gls = sJInfoManagerMapper.selectQlrGlByInfoIdOnly(infoId);
        for(SJ_Qlr_Gl qlr_gl:qlr_gls){
            sJInfoManagerMapper.deleteQlrById(qlr_gl.getObligeeId());
            sJInfoManagerMapper.deleteQlrGlById(qlr_gl.getRelationId());
        }
        //删除服务，根据serviceDataTo删除服务
        deleteServiceData(serviceDataTo,infoId);
        return "服务数据删除成功";
    }

    private SJ_Sjsq dealGetServiceData(SJ_Sjsq sjsq, String serviceCode, String serviceDataTo){
        switch (serviceDataTo){
            case BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE:
                List<SJ_Info_Bdcqlxgxx> bdcqlxxs = this.sJInfoManagerMapper.selectBdcqlxxs(sjsq.getReceiptNumber(),serviceCode);
                for(SJ_Info_Bdcqlxgxx bdcqlxx:bdcqlxxs){
                    bdcqlxx.setGlObligeeVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(bdcqlxx.getInfoId(),"权利人"));
                    bdcqlxx.setGlObligorVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(bdcqlxx.getInfoId(),"义务人"));
                    bdcqlxx.setGlImmovableVoList(dealGetBdcGls(bdcqlxx));
                }
                sjsq.setImmovableRightInfoVoList(bdcqlxxs);
                break;
            case BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE:
                List<Sj_Info_Bdcdyxgxx> bdcdyxxs = this.sJInfoManagerMapper.selectBdcdyxxs(sjsq.getReceiptNumber(),serviceCode);
                for (Sj_Info_Bdcdyxgxx bdcdyxgxx:bdcdyxxs){
                    bdcdyxgxx.setGlMortgageHolderVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(bdcdyxgxx.getInfoId(),"抵押权人"));
                    bdcdyxgxx.setGlMortgagorVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(bdcdyxgxx.getInfoId(),"抵押人"));
                    bdcdyxgxx.setGlImmovableVoList(dealGetBdcGls(bdcdyxgxx));
                }
                sjsq.setImmovableCurrentMortgageInfoVoList(bdcdyxxs);
                break;
            case BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE:
                List<Sj_Info_Jyhtxx> jyhtxxs = this.sJInfoManagerMapper.selectJyhtxxs(sjsq.getReceiptNumber(),serviceCode);
                if(jyhtxxs!=null && jyhtxxs.size()>1){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_TRANSACTION_CONTRACT_COUNT_ERROR);
                }
                if(jyhtxxs!=null && jyhtxxs.size()>0){
                    Sj_Info_Jyhtxx jyhtxx = jyhtxxs.get(0);
                    jyhtxx.setGlHouseSellerVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(jyhtxx.getInfoId(),"售房者"));
                    jyhtxx.setGlHouseBuyerVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(jyhtxx.getInfoId(),"购房者"));
                    jyhtxx.setGlImmovableVoList(dealGetBdcGls(jyhtxx));
                    sjsq.setTransactionContractInfo(jyhtxx);
                }
                break;
            case BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE:
                List<Sj_Info_Dyhtxx> dyhtxxs = this.sJInfoManagerMapper.selectDyhtxxs(sjsq.getReceiptNumber(),serviceCode);
                if(dyhtxxs!=null && dyhtxxs.size()>1){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_MORTGAGE_CONTRACT_COUNT_ERROR);
                }
                if(dyhtxxs!=null && dyhtxxs.size()>0){
                    Sj_Info_Dyhtxx dyhtxx = dyhtxxs.get(0);
                    dyhtxx.setGlMortgageHolderVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(dyhtxx.getInfoId(),"抵押权人"));
                    dyhtxx.setGlMortgagorVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(dyhtxx.getInfoId(),"抵押人"));
                    dyhtxx.setGlImmovableVoList(dealGetBdcGls(dyhtxx));
                    sjsq.setMortgageContractInfo(dyhtxx);
                }
                break;
            case BizOrBizExceptionConstant.TAXATION_RECEIPT_SERVICE:
                List<Sj_Info_Qsxx> qsxxs = this.sJInfoManagerMapper.selectDsxxs(sjsq.getReceiptNumber(),serviceCode);
//                if(qsxxs!=null && qsxxs.size()>1){
//                    throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_TAXATION_COUNT_ERROR);
//                }
                if(qsxxs!=null){
//                    Sj_Info_Qsxx qsxx = qsxxs.get(0);
                    sjsq.setTaxInfoVoList(qsxxs);
                }
                break;
            case BizOrBizExceptionConstant.HANDLE_RESULT_SERVICE:
                List<SJ_Info_Handle_Result> handleResults = this.sJInfoManagerMapper.selectHandleResults(sjsq.getReceiptNumber(),serviceCode);
                sjsq.setHandleResultVoList(handleResults);
                break;
            case BizOrBizExceptionConstant.IMMOVEABLE_BUILDING_SERVICE:
                List<SJ_Info_Immovable> immovs = this.sJInfoManagerMapper.selectImmovs(sjsq.getReceiptNumber(),serviceCode);
                if(immovs!=null && immovs.size()>1){
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_IMMOV_COUNT_ERROR);
                }
                if(immovs!=null && immovs.size()>0){
                    SJ_Info_Immovable immov = immovs.get(0);
                    immov.setGlObligeeVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(immov.getInfoId(),"权利人"));
                    immov.setGlAgentVoList(this.sJInfoManagerMapper.selectQlrGlByInfoId(immov.getInfoId(),"权利代理人"));
                    immov.setGlImmovableVoList(dealGetBdcGls(immov));
                    sjsq.setImmovableSelf(immov);
                }
                break;
        }
        return sjsq;
    }

    private List<SJ_Bdc_Gl> dealGetBdcGls(SJ_Information info){
        List<SJ_Bdc_Gl> bdc_gls = this.sJInfoManagerMapper.selectBdcglByInfoId(info.getInfoId());
        for(SJ_Bdc_Gl bdc_gl:bdc_gls){
            if(bdc_gl.getImmovableType()==null){
                throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_IMMOVABLE_TYPE_NOT_CLEAR);
            }
            if(bdc_gl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_FD)){
                bdc_gl.setFwInfo(this.sJInfoManagerMapper.selectBdcFDByInfoId(bdc_gl.getImmovableId()));
            } else if(bdc_gl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)){
                bdc_gl.setZdInfo(this.sJInfoManagerMapper.selectBdcJDByInfoId(bdc_gl.getImmovableId()));
            } else{
                throw new ZtgeoBizException(BizOrBizExceptionConstant.GET_IMMOVABLE_TYPE_NOT_CLEAR);
            }
        }
        return bdc_gls;
    }

    private SJ_Information findInfomationByInfoIdAndServiceDataTo(String infoId,String serviceDataTo){
        switch (serviceDataTo){
            case BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE:
                return sJInfoManagerMapper.selectBDCQLServiceInformationByInfoId(infoId);
            case BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE:
                return sJInfoManagerMapper.selectBDCDYServiceInformationByInfoId(infoId);
            case BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE:
                return sJInfoManagerMapper.selectJYHTServiceInformationByInfoId(infoId);
            case BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE:
                return sJInfoManagerMapper.selectDYHTServiceInformationByInfoId(infoId);
            case BizOrBizExceptionConstant.TAXATION_RECEIPT_SERVICE:
                return sJInfoManagerMapper.selectQSServiceInformationByInfoId(infoId);
            case BizOrBizExceptionConstant.HANDLE_RESULT_SERVICE:
                return sJInfoManagerMapper.selectHandleServiceInformationByInfoId(infoId);
            case BizOrBizExceptionConstant.IMMOVEABLE_BUILDING_SERVICE:
                return sJInfoManagerMapper.selectBDCNoCertServiceInformationByInfoId(infoId);
            default:
                throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_TABLE_ERROR_MSG);
        }
    }

    private void deleteServiceData(String serviceDataTo,String infoId){
        switch (serviceDataTo){
            case BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE:
                sJInfoManagerMapper.deleteBDCQLById(infoId);
                break;
            case BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE:
                sJInfoManagerMapper.deleteBDCDYById(infoId);
                break;
            case BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE:
                sJInfoManagerMapper.deleteJYHTById(infoId);
                break;
            case BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE:
                sJInfoManagerMapper.deleteDYHTById(infoId);
                break;
            case BizOrBizExceptionConstant.TAXATION_RECEIPT_SERVICE:
                sJInfoManagerMapper.deleteQSById(infoId);
                break;
            case BizOrBizExceptionConstant.HANDLE_RESULT_SERVICE:
                sJInfoManagerMapper.deleteHandleById(infoId);
                break;
            case BizOrBizExceptionConstant.IMMOVEABLE_BUILDING_SERVICE:
                sJInfoManagerMapper.deleteBDCNoCertById(infoId);
                break;
            default:
                throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_TABLE_ERROR_MSG);
        }
    }

    @Override
    public boolean preCheckServicesForAotuSubmitOut(String taskId,SJ_Sjsq sjsq){
        String stepId = (String) stepComponent.getSteps(taskId);        //获取stepId
        SJ_Power_Step_Service stepSvr = new SJ_Power_Step_Service();    //步骤服务权限
        stepSvr.setStepId(stepId);
        stepSvr.setStatus("可用");
        List<SJ_Power_Step_Service> stepSvrList = stepManagerMapper.selectStepSvrs(stepSvr);//步骤服务权限list
        for(SJ_Power_Step_Service stepSvrObj:stepSvrList){
            if(StringUtils.isNotBlank(stepSvrObj.getPermissionLevel())
                    && stepSvrObj.getPermissionLevel().contains(BizOrBizExceptionConstant.POWER_LEVEL_WRITE)){//服务具备写权限
                SJ_Service service = svrManagerMapper.selectSvrById(stepSvrObj.getServiceId());
                String serviceDataTo = service.getServiceDataTo();
                if(StringUtils.isNotBlank(serviceDataTo)){//对应服务数据是否存在
                    switch (serviceDataTo){
                        case BizOrBizExceptionConstant.IMMOVABLE_RIGHT_RECEIPT_SERVICE:
                            if(sjsq.getImmovableRightInfoVoList()==null || sjsq.getImmovableRightInfoVoList().size()<=0){
                                return false;
                            }
                            break;
                        case BizOrBizExceptionConstant.IMMOVABLE_MORTGAGE_RECEIPT_SERVICE:
                            if(sjsq.getImmovableCurrentMortgageInfoVoList()==null || sjsq.getImmovableCurrentMortgageInfoVoList().size()<=0){
                                return false;
                            }
                            break;
                        case BizOrBizExceptionConstant.TRANSACTION_CONTRACT_RECEIPT_SERVICE:
                            if(sjsq.getTransactionContractInfo()==null){
                                return false;
                            }
                            break;
                        case BizOrBizExceptionConstant.MORTGAGE_CONTRACT_RECEIPT_SERVICE:
                            if(sjsq.getMortgageContractInfo()==null){
                                return false;
                            }
                            break;
                        case BizOrBizExceptionConstant.TAXATION_RECEIPT_SERVICE:
                            if(sjsq.getTaxInfoVoList()==null || sjsq.getTaxInfoVoList().size()<=0){
                                return false;
                            }
                            break;
                        case BizOrBizExceptionConstant.HANDLE_RESULT_SERVICE:
                            if(sjsq.getHandleResultVoList()==null||sjsq.getHandleResultVoList().size()<=0){
                                return false;
                            }
                            break;
                        case BizOrBizExceptionConstant.IMMOVEABLE_BUILDING_SERVICE:
                            if(sjsq.getImmovableSelf()==null){
                                return false;
                            }
                            break;
                    }
                } else {
                    throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_TABLE_NOT_WRITE_MSG);
                }
            }
        }
        return true;
    }

    private List<Integer> findSameBdcGlSelf(List<SJ_Bdc_Gl> bdcgls_exist){
        List<Integer> same_indexs = new ArrayList<Integer>();
        if(bdcgls_exist!=null && bdcgls_exist.size()>1){
            for (int i=0;i<bdcgls_exist.size()-1;i++){
                boolean isContinue = false;
                SJ_Bdc_Gl bdcgl_index = bdcgls_exist.get(i);
                SJ_Bdc_Fw_Info fwInfo = null;
                SJ_Bdc_Zd_Info zdInfo = null;
                boolean isFd = true;
                if(bdcgl_index.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)){
                    isFd = false;
                }
                if(isFd){
                    fwInfo = sJInfoManagerMapper.selectBdcFDByInfoId(bdcgl_index.getImmovableId());
                }else{
                    zdInfo = sJInfoManagerMapper.selectBdcJDByInfoId(bdcgl_index.getImmovableId());
                }
                for(Integer same_index:same_indexs){
                    if(i==same_index){
                        isContinue = true;
                        break;
                    }
                }
                if(isContinue) continue;
                for(int j=i+1;j<bdcgls_exist.size();j++){
                    SJ_Bdc_Gl bdcgl = bdcgls_exist.get(j);
                    if(isFd){
                        if(!bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)){
                            if(isSameFD(bdcgl_index,bdcgl)){
                                same_indexs.add(j);
                            }
                        }
                    } else {
                        if (bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)){
                            if(isSameJD(bdcgl_index,bdcgl)){
                                same_indexs.add(j);
                            }
                        }
                    }
                }
            }
        }
        return same_indexs;
    }

    private SJ_Bdc_Gl isCopyBdc(SJ_Bdc_Gl bdcgl,List<SJ_Bdc_Gl> bdcgls_exist){
//        boolean isSame = false;
        SJ_Bdc_Gl bdc_gl = null;
        if(bdcgls_exist!=null && bdcgls_exist.size()>0) {
            for (SJ_Bdc_Gl bdcgl_exist : bdcgls_exist) {
                if(bdcgl.getImmovableType().equals(bdcgl_exist.getImmovableType())){//不动产类型需要一致
                    if(StringUtils.isNotBlank(bdcgl.getRelationId())){
                        if(bdcgl.getRelationId().equals(bdcgl_exist.getRelationId())) {//待保存的数据存在关系id且重复
                            bdc_gl = bdcgl_exist;
                            break;
                        } else {    //待保存的数据存在关系id且不重复
                            continue;
                        }
                    } else {//待保存的数据不存在关系id
                        if (bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)) {//净地判断重复
                            if(isSameJD(bdcgl_exist,bdcgl)){
                                bdc_gl = bdcgl_exist;
                                break;
                            }
                        } else {//房地判断重复
                            if(isSameFD(bdcgl_exist,bdcgl)){
                                bdc_gl = bdcgl_exist;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return bdc_gl;
    }

    private boolean isOutBdcgl(SJ_Bdc_Gl bdcgl_exist,List<SJ_Bdc_Gl> bdcgls){
        boolean isOut = true;
        for(SJ_Bdc_Gl bdcgl:bdcgls){
            if(bdcgl.getImmovableType().equals(bdcgl_exist.getImmovableType())){
                if(StringUtils.isNotBlank(bdcgl.getRelationId())){
                    if(bdcgl.getRelationId().equals(bdcgl_exist.getRelationId())){
                        isOut = false;
                        break;
                    }
                }else{//这种新入关系id为空的情况
                    if (bdcgl.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)) {//净地判断重复
                        if(isSameJD(bdcgl_exist,bdcgl)){
                            isOut = false;
                            break;
                        }
                    } else {//房地判断重复
                        if(isSameFD(bdcgl_exist,bdcgl)){
                            isOut = false;
                            break;
                        }
                    }
                }
            }
        }
        return isOut;
    }

    private boolean isSameJD(SJ_Bdc_Gl bdcgl_exist,SJ_Bdc_Gl bdcgl){
        SJ_Bdc_Zd_Info zd_exist = sJInfoManagerMapper.selectBdcJDByInfoId(bdcgl_exist.getImmovableId());
        SJ_Bdc_Zd_Info zd = bdcgl.getZdInfo();
        if(zd==null){
            zd = sJInfoManagerMapper.selectBdcJDByInfoId(bdcgl.getImmovableId());
        }
        if(
                zd != null &&
                        zd_exist!=null &&
                        (StringUtils.isNotBlank(zd_exist.getParcelUnicode()) &&
                            StringUtils.isNotBlank(zd.getParcelUnicode())&&
                            zd_exist.getParcelUnicode().equals(zd.getParcelUnicode()) ||
                                StringUtils.isNotBlank(zd_exist.getImmovableUnitNumber()) &&
                                        StringUtils.isNotBlank(zd.getImmovableUnitNumber())&&
                                        zd_exist.getImmovableUnitNumber().equals(zd.getImmovableUnitNumber())
                        )
        ){
            return true;
        }
        return false;
    }

    private boolean isSameFD(SJ_Bdc_Gl bdcgl_exist,SJ_Bdc_Gl bdcgl){
        SJ_Bdc_Fw_Info fw_exist = sJInfoManagerMapper.selectBdcFDByInfoId(bdcgl_exist.getImmovableId());
        SJ_Bdc_Fw_Info fw = bdcgl.getFwInfo();
        if(fw==null){
            fw = sJInfoManagerMapper.selectBdcFDByInfoId(bdcgl.getImmovableId());
        }
        if(
                fw!=null &&
                        fw_exist!=null &&
                        (StringUtils.isNotBlank(fw_exist.getHouserUnifiedCode()) &&                                  //不动产统一标识
                            StringUtils.isNotBlank(fw.getHouserUnifiedCode())&&
                            fw_exist.getHouserUnifiedCode().equals(fw.getHouserUnifiedCode()) ||
                                StringUtils.isNotBlank(fw_exist.getHouseholdId()) &&                                        //户编号
                                StringUtils.isNotBlank(fw.getHouseholdId())&&
                            fw_exist.getHouseholdId().equals(fw.getHouseholdId()) ||
                                StringUtils.isNotBlank(fw_exist.getImmovableUnicode()) &&                                   //不动产统一编码
                                StringUtils.isNotBlank(fw.getImmovableUnicode())&&
                            fw_exist.getImmovableUnicode().equals(fw.getImmovableUnicode()) ||
                                StringUtils.isNotBlank(fw_exist.getImmovableUnitNumber()) &&                                //不动产单元号
                                StringUtils.isNotBlank(fw.getImmovableUnitNumber())&&
                                fw_exist.getImmovableUnitNumber().equals(fw.getImmovableUnitNumber())
                        )
        ){
            return true;
        }
        return false;
    }

    private Integer deleteImmovExist(SJ_Bdc_Gl bdcgl_exist){
        Integer count = 0;
        if(sJInfoManagerMapper.selectNotUseBdcGlCount(bdcgl_exist.getImmovableId(),bdcgl_exist.getImmovableType(),bdcgl_exist.getInfoId())<=0){
            if(bdcgl_exist.getImmovableType().equals(BizOrBizExceptionConstant.IMMOVABLE_TYPE_OF_JD)) {
                count = sJInfoManagerMapper.deleteZDById(bdcgl_exist.getImmovableId());
            } else {
                count = sJInfoManagerMapper.deleteFWById(bdcgl_exist.getImmovableId());
            }
        }
        return count;
    }

    private boolean isOutQlrgl(SJ_Qlr_Gl qlrgl_exist,List<SJ_Qlr_Gl> qlrgls){
        boolean isOut = true;
        SJ_Qlr_Info qlr_exist = sJInfoManagerMapper.selectObligeeById(qlrgl_exist.getObligeeId());
        if(qlr_exist!=null) {
            for (SJ_Qlr_Gl qlrgl : qlrgls) {
                if (StringUtils.isNotBlank(qlrgl.getRelationId())) {
                    if (qlrgl.getRelationId().equals(qlrgl_exist.getRelationId())) {
                        isOut = false;
                        break;
                    }
                } else {
                    SJ_Qlr_Info qlr = qlrgl.getRelatedPerson();
                    if (
                            StringUtils.isNotBlank(qlr.getObligeeDocumentType()) &&
                                    StringUtils.isNotBlank(qlr_exist.getObligeeDocumentType()) &&
                                    qlr.getObligeeDocumentType().equals(qlr_exist.getObligeeDocumentType())
                    ){
                        if(
                                qlr.getObligeeName().equals(qlr_exist.getObligeeName()) &&
                                        qlr.getObligeeDocumentNumber().equals(qlr_exist.getObligeeDocumentNumber())
                        ){
                            isOut = false;
                            break;
                        }
                    }
                }
            }
        }
        return isOut;
    }

    private SJ_Qlr_Gl isCopyQlr(SJ_Qlr_Gl qlrgl,List<SJ_Qlr_Gl> qlrgls_exist){
        SJ_Qlr_Gl sameQlrgl = null;
        if(qlrgls_exist!=null && qlrgls_exist.size()>0){
            for(SJ_Qlr_Gl qlrgl_exist:qlrgls_exist){
                if(StringUtils.isNotBlank(qlrgl.getRelationId())){
                    if(qlrgl.getRelationId().equals(qlrgl_exist.getRelationId())){
                        sameQlrgl = qlrgl_exist;
                        break;
                    }else{
                        continue;
                    }
                }else{
                    if(qlrgl.getObligeeName().equals(qlrgl_exist.getObligeeName())){
                        SJ_Qlr_Info qlr = qlrgl.getRelatedPerson();
                        SJ_Qlr_Info qlr_exist = sJInfoManagerMapper.selectObligeeById(qlrgl_exist.getObligeeId());
                        if(StringUtils.isNotBlank(qlr.getObligeeDocumentNumber())){
                            if(StringUtils.isNotBlank(qlr_exist.getObligeeDocumentNumber()) && qlr.getObligeeDocumentNumber().equals(qlr_exist.getObligeeDocumentNumber())){
                                sameQlrgl = qlrgl_exist;
                                break;
                            }
                        }else{
                            if(StringUtils.isBlank(qlr_exist.getObligeeDocumentNumber())){
                                sameQlrgl = qlrgl_exist;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return sameQlrgl;
    }

    private List<Integer> findSameQlrGlSelf(List<SJ_Qlr_Gl> qlrgls_exist){
        List<Integer> same_indexs = new ArrayList<Integer>();
        if(qlrgls_exist!=null && qlrgls_exist.size()>1){
            for (int i=0;i<qlrgls_exist.size()-1;i++){
                boolean isContinue = false;
                SJ_Qlr_Gl qlrgl_index = qlrgls_exist.get(i);
                SJ_Qlr_Info qlr_index = sJInfoManagerMapper.selectObligeeById(qlrgl_index.getObligeeId());
                if(qlr_index==null){
                    same_indexs.add(i);
                    isContinue = true;
                }
                for(Integer same_index:same_indexs){
                    if(i==same_index){
                        isContinue = true;
                        break;
                    }
                }
                if(isContinue) continue;
                for(int j=i+1;j<qlrgls_exist.size();j++){
                    SJ_Qlr_Gl qlrgl = qlrgls_exist.get(j);
                    if(qlrgl.getObligeeName().equals(qlrgl_index.getObligeeName())){
                        SJ_Qlr_Info qlr = sJInfoManagerMapper.selectObligeeById(qlrgl.getObligeeId());
                        if(qlr==null){
                            if(j==qlrgls_exist.size()-1){
                                same_indexs.add(j);
                            }
                        }else{
                            if(StringUtils.isNotBlank(qlr_index.getObligeeDocumentNumber())){
                                if(StringUtils.isNotBlank(qlr.getObligeeDocumentNumber()) && qlr.getObligeeDocumentNumber().equals(qlr_index.getObligeeDocumentNumber())){
                                    same_indexs.add(j);
                                }
                            }else{
                                if(StringUtils.isBlank(qlr.getObligeeDocumentNumber())){
                                    same_indexs.add(j);
                                }
                            }
                        }
                    }
                }
            }
        }
        return same_indexs;
    }

    private void RemoveExistQlr(SJ_Qlr_Gl qlrgl_exist){
        if(sJInfoManagerMapper.selectUsedQlrCount(qlrgl_exist.getObligeeId(),qlrgl_exist.getInfoId())<=0){
            sJInfoManagerMapper.deleteQlrById(qlrgl_exist.getObligeeId());
        }
    }
}
