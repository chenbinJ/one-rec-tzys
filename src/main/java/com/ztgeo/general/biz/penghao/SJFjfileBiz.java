package com.ztgeo.general.biz.penghao;


import org.springframework.web.multipart.MultipartFile;

public interface SJFjfileBiz {

    /**
     * 点击附件返回base64位编码
     *
     * @param fileId
     * @return
     */
    byte[] selectByViewAttachment(String fileId, byte[] yte);


    /**
     * 新增附件与条目附件表
     *
     * @param file    文件
     * @param finstId 条目实列Id (父级)
     * @return
     */
    Object insertByAttachmentsAndEntryProcess(String fjinstEntryId, MultipartFile file, String finstId, String processId);

    /**
     * 根据id查看信息
     *
     * @param fileId
     * @return
     */
    Object selectByPrimaryKey(String fileId);

    /**
     * 新增条目附件表
     *
     * @param cid
     * @return
     */
    Object insertSjFjinst(String fjinstEntryId, String cid, String processId, String cname);

    /**
     * 删除条目实列或者附件表
     *
     * @param cid
     * @param fileId
     * @return
     */
    String deleteEntryOrAttachmentsAndEntryProcess(String fjinstEntryId, String cid, String fileId);

}
