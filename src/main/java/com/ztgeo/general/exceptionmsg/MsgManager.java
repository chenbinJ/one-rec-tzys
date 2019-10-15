package com.ztgeo.general.exceptionmsg;



import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Date;

public class MsgManager {

    /**
     * common
     */
    public static String INSERT_ERROR = "新增失败!";
    public static String INSERT_SUCCESS = "新增成功!";
    public static String UPDATE_ERROR = "修改失败!";
    public static String UPDATE_SUCCESS = "修改成功!";
    public static String DELETE_ERROR = "删除失败!";
    public static String DELETE_SUCCESS = "删除成功!";
    public static String SELECT_ERROR = "查询失败!";
    public static String SELECT_SUCCESS = "查询成功!";
    public static String OPERATING_SUCCESS = "操作成功!";
    public static String OPERATING_ERROR = "操作失败!";
    public static String SYSTEM_ERROR = "系统异常";
    public static String SOMETHING_IS_NULL = "%s为空!";
    public static String SOMETHING_REPEAT="%s重复!";
    public static String NOT_OPERATING = "无操作!";
    public static String ROLE_ID_NULL="请选择角色";
    public static String FILE_ID_NULL="附件不存在";
    public final  static String END="结束";
    public final static String SHUI="feWater";
    public final static String DIAN=" feLectric";
    public final static String QI=" feGas";
    public final static String YXDS="feCableTV";
    public final static String FTP_TUPIAN_BAD="ftp服务器没有该图片";


    public final static String FILE_BAD="上传文件失败";
    public final static String FILE="文件";
    public final  static String IMAGE_TYPE="该附件不是图片类型,请从新选择";
    //文件类型
    public static String IMG_TYPE_PNG = "PNG";
    public static String IMG_TYPE_JPG = "JPG";
    public static String IMG_TYPE_JPEG = "JPEG";
    public static String IMG_TYPE_DMG = "BMP";
    public static String IMG_TYPE_GIF = "GIF";
    public static String IMG_TYPE_SVG = "SVG";
    /**
     * status
     */
     //状态码

    public static String FINST_ID_NULL="条件实列为空，请从新选择";
    public static String DATE_PARSE_EXCEPTION = "时间转换异常!";
    public static String DATE_IS_NULL = "时间不存在!";
     public final static String SJFJTM_NOT_COMPARISON="找不到人脸对比条目";
     public final static String ENTRY_INSERT_FILE_BAD="新增附件表失败";
     public final static String ENTRY_INSERT_BAD="新增条目表失败";
    public final static String ENTRY_INSERT_FILE_TIAOMU_BAD="新增条目附件表失败";
    public final static String ENTRY_DELETE_TIAOMU_FILE_BAD="删除条目附件表失败";
    public final static String ENTRY_DELETE_FILE_BAD="删除附件表失败";
    public final static String SJFINST_PID_EXISTENCE="不得删除文件或文件夹，有子级";
    public final static String SHFINST_CNAME_EXISTISE="有相同名称不得添加,请从新选择";
    public final static String APPROVE_INSERT_BAD="新增审批表失败";
    public final static String SJFINST_PRITURE_NULL="没有传入图片";
    public final static String POSITION_USER_NULL="该用户没有职务，请配置";
    public final static String PICINSITE="现场图片";
    public final static String PICINIDENTITYCARD="身份证图片";
    public final static String STEP_MOULD_ID_NULL="请先部署在配置流程图";
    public final static String STEP_POSITION_NODE="岗位步骤权限分配";
    public final static String STEP_POSITION_TITLE="岗位步骤权限";
    public final static String STEP_POSITION_NULL="请配置岗位和步骤关系";
    public final static String STEP_POSITION_STATUS="可用";
    public final static String SJFJTM_PROCESS_NULL="请先绑定实例和条目关系";
    public final static String DEPLOMENT_MODULE_NULL="请部署模板";
    public final static String TASK_STEP_NULL="请先绑定节点与步骤关系";
    public final static String USER_FLOW_NULL="没有已审批过，流程还未结束流程";
    public final static String APPROVE_IS_NULL="没有找到审批数据";
    public final static String USER_POSITION_NULL="该岗位下面没有用户，请设置";
    public final static String TASK_NULL="没有找到task对象";
    public final static String FLOW_PROCESSNAME_NOT="用户不是所属节点审批人";
    public final static String MODULE_START_PARENT_NULL="该模板没有找到父级为开始节点";
    public final static String SJFJTM_NODE="人证对比";
    public final static String BIXUAN="必选";
    public final static String PROCESS_NULL="实例为空";
    public final static String PROCESS_DX_NULL="找不到实例对象";
    public final static String TEMPLATE_CATEGORY_MODLE_NULL="有模糊没有配置模板大类,请先配置模板大类";
    public final static String AUTHON_FJTM_LEVEL="该用户没有新建权限";
    public final static String FJINST_CNAME_NULL="附件名称为空,请从新输入";
    public final static String FILE_NULL="附件不得为空";
    public final static String MODULE_NAME_NULL="请设置各个节点的名称";
    public final static String APPROVE_END="流程已结束";
    public final static String USER_QX="该用户没有操作权限";
    public final static String TASK_PERSON="该节点不是你审批,无法操作";
    public final static String USER_FLOW_QXNULL="该用户对流程没有操作权限";
    public final static String WRITE="Write";


     //菜单模块
     public final static String MENU_PC_STATE="0";
     public final static String MENY_APP_STATE="1";

     public final static int APPLY_FLOW_STATE=0;

     public final static  int MONTHLYPLAN_SB_STATE=1;//上报
     public final static  int MONTHLYPLAN_FB_STATE=2;//发布

    /**
     * 对象删除标记
     */
    public static Integer LS_NUMBER=0;//新增文章时浏览次数默认为0
    public static String NOT_DELETE = "0";
    public static String DELETE = "1";
    public static String STATUS_USE = "0";
    public static String STATUS_NO_USE = "1";
    public static String ROOM_TYPE="1";
    public static String ROOM_READ="0";

    /**
     * 部门提示信息
     */
    public static String MODEL_MB="模板不存在";
    public static String MODEL_ID_NULL="模板id为空";
    /**
     * 工作流
     */
    public static String WORKFLOW_MODEL_MAIN_ERROR = "数据模型不符要求，请至少设计一条主线流程";
    public static String WORKFLOW_MODEL_IS_NULL = "模型数据为空，请先设计流程并成功保存，再进行发布";
    public static String WORKFLOW_PROCESS_DEFINITON_FLAG = "已%sID为[%s]的流程定义。";
    public static String WORKFLOW_APPROVE_RESULT_IS_NULL = "审批结果为空";
    public static String WORKFLOW_APPROVE_INSERT_ERROR = "流程新增失败";
    public static String WORKFLOW_DISCARD = "流程废弃";
    public static String WORKFLOW_COMPLETE = "流程完成";
    public static String EXCLUSIVE_GATEWAY_IS_ONE = "流程异常,排他网关仅有一条流程走向";//排他网关仅有一条走向异常


}
