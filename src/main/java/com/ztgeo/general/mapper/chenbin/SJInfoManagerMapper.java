package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public interface SJInfoManagerMapper {
    public Integer selectCountOfSvr(@Param("svrId") String svrId);
    public SJ_Sjsq selectSjsqBySjbh(@Param("sqbh") String sqbh);
    public List<SJ_Info_Bdcqlxgxx> selectBdcqlxxsBySqbh(@Param("sqbh") String sqbh);
    public List<Sj_Info_Bdcdyxgxx> selectBdcdyxxsBySqbh(@Param("sqbh") String sqbh);
    public List<Sj_Info_Dyhtxx> selectDyhtxxsBySqbh(@Param("sqbh") String sqbh);
    public List<Sj_Info_Jyhtxx> selectJyhtxxsBySqbh(@Param("sqbh") String sqbh);
    public List<SJ_Info_Bdcqlxgxx> selectBdcqlxxs(@Param("sqbh") String sqbh,@Param("serviceCode") String serviceCode);
    public List<Sj_Info_Bdcdyxgxx> selectBdcdyxxs(@Param("sqbh") String sqbh, @Param("serviceCode") String serviceCode);
    public List<Sj_Info_Dyhtxx> selectDyhtxxs(@Param("sqbh") String sqbh, @Param("serviceCode") String serviceCode);
    public List<Sj_Info_Jyhtxx> selectJyhtxxs(@Param("sqbh") String sqbh, @Param("serviceCode") String serviceCode);
    public List<Sj_Info_Qsxx> selectDsxxs(@Param("sqbh") String sqbh,@Param("serviceCode") String serviceCode);
    public List<SJ_Info_Handle_Result> selectHandleResults(@Param("sqbh") String sqbh,@Param("serviceCode") String serviceCode);
    public List<SJ_Qlr_Gl> selectQlrGlByInfoId(@Param("infoId") String infoId,@Param("qlrlx") String qlrlx);

    public List<SJ_Bdc_Gl> selectBdcglByInfoId(@Param("infoId") String infoId);
    public SJ_Bdc_Fw_Info selectBdcFDByInfoId(@Param("houseId") String houseId);
    public SJ_Bdc_Zd_Info selectBdcJDByInfoId(@Param("landId") String landId);

    public SJ_Info_Bdcqlxgxx selectBdcqlxxBySjbhAndBDCCertNo(@Param("receiptNumber") String receiptNumber,@Param("immovableCertificateNo") String immovableCertificateNo,@Param("serviceCode") String serviceCode);
    public SJ_Info_Bdcqlxgxx selectBdcqlxxBySjbhAndFCCertNo(@Param("receiptNumber") String receiptNumber,@Param("houseCertificateNo") String houseCertificateNo,@Param("landCertificateNo") String landCertificateNo,@Param("serviceCode") String serviceCode);
    public Sj_Info_Bdcdyxgxx selectBdcdyxxBySjbhAndMortgageNo(@Param("receiptNumber") String receiptNumber,@Param("mortgageCertificateNo") String mortgageCertificateNo,@Param("serviceCode") String serviceCode);
    public Sj_Info_Jyhtxx selectJyhtxxBySqbh(@Param("receiptNumber") String receiptNumber);
    public Sj_Info_Dyhtxx selectDyhtxxBySqbh(@Param("receiptNumber") String receiptNumber);
    public Sj_Info_Qsxx selectQsxxBySqbh(@Param("receiptNumber") String receiptNumber,
                                         @Param("serviceCode")String serviceCode,
                                         @Param("xtsphm")String xtsphm,
                                         @Param("nsrsbh")String nsrsbh);
    public String selectReceiptNumberByRegisterNumber(@Param("registerNumber") String registerNumber);

    public Integer selectCountOfHouse(@Param("houseId") String houseId);
    public Integer selectCountOfParcel(@Param("parcelId") String parcelId);
    public Integer selectCountOfObligee(@Param("obligeeId") String obligeeId);

    public Integer updateSjsq(SJ_Sjsq sjsq);
    public Integer insertSjsq(SJ_Sjsq sjsq);
    public Integer insertBdcqlxx(SJ_Info_Bdcqlxgxx bdcqlxgxx);
    public Integer updateBdcqlxx(SJ_Info_Bdcqlxgxx bdcqlxgxx);
    public Integer insertBdcdyxx(Sj_Info_Bdcdyxgxx bdcdyxgxx);
    public Integer updateBdcdyxx(Sj_Info_Bdcdyxgxx bdcdyxgxx);
    public Integer insertJyhtxx(Sj_Info_Jyhtxx jyhtxx);
    public Integer updateJyhtxx(Sj_Info_Jyhtxx jyhtxx);
    public Integer insertDyhtxx(Sj_Info_Dyhtxx dyhtxx);
    public Integer updateDyhtxx(Sj_Info_Dyhtxx dyhtxx);
    public Integer insertQsxx(Sj_Info_Qsxx qsxx);
    public Integer updateQsxx(Sj_Info_Qsxx qsxx);
    public Integer insertHandleResult(SJ_Info_Handle_Result handleResult);
    public Integer updateHandleResult(SJ_Info_Handle_Result handleResult);

    public Integer insertBdcgl(SJ_Bdc_Gl bdcgl);
    public Integer updateBdcgl(SJ_Bdc_Gl bdcgl);
    public Integer updateZdInfo(SJ_Bdc_Zd_Info zdInfo);
    public Integer insertZdInfo(SJ_Bdc_Zd_Info zdInfo);
    public Integer updateFdInfo(SJ_Bdc_Fw_Info fwInfo);
    public Integer insertFdInfo(SJ_Bdc_Fw_Info fwInfo);
    public Integer updateQlrgl(SJ_Qlr_Gl qlrgl);
    public Integer insertQlrgl(SJ_Qlr_Gl qlrgl);
    public Integer updateQlr(SJ_Qlr_Info qlr);
    public Integer insertQlr(SJ_Qlr_Info qlr);


    public SJ_Information selectBDCQLServiceInformationByInfoId(@Param("infoId")String infoId);
    public SJ_Information selectBDCDYServiceInformationByInfoId(@Param("infoId")String infoId);
    public SJ_Information selectJYHTServiceInformationByInfoId(@Param("infoId")String infoId);
    public SJ_Information selectDYHTServiceInformationByInfoId(@Param("infoId")String infoId);
    public SJ_Information selectHandleServiceInformationByInfoId(@Param("infoId")String infoId);
    public SJ_Information selectBDCNoCertServiceInformationByInfoId(@Param("infoId")String infoId);
    public SJ_Information selectQSServiceInformationByInfoId(@Param("infoId")String infoId);

    public List<SJ_Qlr_Gl> selectQlrGlByInfoIdOnly(@Param("infoId")String infoId);

    public Integer deleteFWById(@Param("id") String id);
    public Integer deleteZDById(@Param("id")String id);
    public Integer deleteBDCGLById(@Param("id")String id);
    public Integer deleteQlrById(@Param("id")String id);
    public Integer deleteQlrGlById(@Param("id")String id);
    public Integer deleteBDCQLById(@Param("id")String id);
    public Integer deleteBDCDYById(@Param("id")String id);
    public Integer deleteJYHTById(@Param("id")String id);
    public Integer deleteDYHTById(@Param("id")String id);
    public Integer deleteQSById(@Param("id")String id);
    public Integer deleteHandleById(@Param("id")String id);
    public Integer deleteBDCNoCertById(@Param("id")String id);

    public List<SJ_Execute_depart> selectExeDepart(@RequestParam("receiptNumber") String receiptNumber);
    public Integer insertExeDepart(SJ_Execute_depart depart);
    public Integer updateExeDepart(SJ_Execute_depart depart);
}
