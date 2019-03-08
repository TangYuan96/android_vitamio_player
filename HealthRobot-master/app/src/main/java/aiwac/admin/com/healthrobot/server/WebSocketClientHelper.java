package aiwac.admin.com.healthrobot.server;


import android.content.Context;
import android.content.SharedPreferences;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import aiwac.admin.com.healthrobot.bean.HealthTestResultAbstractInfo;
import aiwac.admin.com.healthrobot.bean.HealthTestResultDetail;
import aiwac.admin.com.healthrobot.bean.LectureAVDetail;
import aiwac.admin.com.healthrobot.bean.LectureArticleDetail;
import aiwac.admin.com.healthrobot.bean.LectureCourseAbstractInfo;
import aiwac.admin.com.healthrobot.bean.MessageInfo;
import aiwac.admin.com.healthrobot.bean.TimerEntity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.TimerSqliteHelper;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;

/**     用于WebSocket客户端通信
 * Created by luwang on 2017/10/31.
 */

public class WebSocketClientHelper extends WebSocketClient {

    private Context context;

    private List<MessageInfo> messageInfos = Collections.synchronizedList(new ArrayList<MessageInfo>());

    private MessageInfo monitorMessage; //监控模式下的消息

    // 讲座   视频 音频  文章  和 健康检测结果摘要信息，每次获取到一次摘要json 更新一次
    private LectureCourseAbstractInfo lectureVideoAllInfo;
    private LectureCourseAbstractInfo lectureAudioAllInfo;
    private LectureCourseAbstractInfo lectureArticleAllInfo;
    private HealthTestResultAbstractInfo healthTestResultAbstractInfo;

    // 讲座   视频 音频  文章  和 健康检测结果详细信息，每次获取到一次摘要json 更新一次
//    private LectureAVDetail lectureAudioDetail;
//    private LectureAVDetail lectureVideoDetail;
    private LectureAVDetail lectureAVDetail;
    private LectureArticleDetail lectureArticleDetail;
    private HealthTestResultDetail healthTestResultDetail;


    public LectureCourseAbstractInfo getLectureVideoAllInfo(){
        return lectureVideoAllInfo;
    }

    public LectureCourseAbstractInfo getLectureAudioAllInfo(){
        return  lectureAudioAllInfo;
    }

    public LectureCourseAbstractInfo getLectureArticleAllInfo(){
        return  lectureArticleAllInfo;
    }

    public LectureAVDetail getLectureAudioDetail(){
        return lectureAVDetail;
    }

    public LectureAVDetail getLectureVideoDetail(){
        return lectureAVDetail;
    }

    public LectureArticleDetail getLectureArticleDetail(){
        return lectureArticleDetail;
    }

    public HealthTestResultAbstractInfo getHealthTestResultAbstractInfo(){
        return healthTestResultAbstractInfo;
    }
    public HealthTestResultDetail getHealthTestResultDetail(){
        return healthTestResultDetail;
    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    
    

    public WebSocketClientHelper(URI serverUri, Map<String, String> httpHeaders, Context context) {
        this(serverUri, new Draft_6455(), httpHeaders, 0, context);
        //LogUtil.d( "serverUri  : " + serverUri);
    }

    public WebSocketClientHelper(URI serverUri, Draft draft, Map<String, String> httpHeaders, Context context) {
        this(serverUri, draft, httpHeaders, 0, context);
    }

    public WebSocketClientHelper(URI serverUri, Draft draft, Map<String, String> httpHeaders, int connectionTimeout, Context context) {
        super(serverUri,draft,httpHeaders,connectionTimeout);

        //获取全局唯一的context对象，否则activity不能销毁问题
        this.context = context.getApplicationContext();
    }



    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_OPEN + getRemoteSocketAddress());

        //开启连接的时候检查要不要同步数据
        checkSyncTimer();
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_CLOSE + i + s + b);
        WebSocketApplication.getWebSocketApplication().setNull();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_EXCEPTION);
    }

    @Override
    public void onMessage(final String json) {
        //处理具体逻辑

       LogUtil.printJson( Constant.WEBSOCKET_MESSAGE_FROM_SERVER ,json,"##");

        try{
            String businessType = JsonUtil.parseBusinessType(json);

            if (businessType.equals(Constant.WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE)) //讲座音频摘要信息到达
            {
                lectureAudioAllInfo = JsonUtil.parseLectureAVAbstractInfo(json);
            }else if (businessType.equals(Constant.WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE)) //讲座视频摘要信息到达
            {
                lectureVideoAllInfo = JsonUtil.parseLectureAVAbstractInfo(json);
            }else if ((businessType.equals(Constant.WEBSOCKET_LECTURE_ARTICLE_ABSTRACT_TYPE_CODE))) //讲座文章摘要信息到达
            {
                lectureArticleAllInfo = JsonUtil.parseLectureArticleAbstractInfo(json);
            }else if ((businessType.equals(Constant.WEBSOCKET_HEALTH_TEST_RESULT_ABSTRACT_TYPE_CODE))) //健康检测结果摘要信息到达
            {
                healthTestResultAbstractInfo = JsonUtil.parseHealthTestResultAbstractInfo(json);
            }else if ((businessType.equals(Constant.WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE))) //讲座音视频详细信息到达
            {
                lectureAVDetail = JsonUtil.parseLectureAVDetailInfo(json);
            }else if  ((businessType.equals(Constant.WEBSOCKET_LECTURE_ARTICLE_DETAIL_TYPE_CODE))) //讲座文章详细信息到达
            {
                lectureArticleDetail = JsonUtil.parseLectureArticleDetailInfo(json);
            }else if ((businessType.equals(Constant.WEBSOCKET_HEALTH_TEST_RESULT_DETAIL_TYPE_CODE))) //健康检测结果详细信息到达
            {
                healthTestResultDetail = JsonUtil.parseHealthTestResultDetail(json);
            }



        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( "onMessage : " + e.getMessage());

        }

    }

    //判断是否重新安装，是否需要同步
    private void checkSyncTimer(){
        SharedPreferences pref = context.getSharedPreferences(Constant.DB_USER_TABLENAME, context.MODE_PRIVATE);
        String timerSync = pref.getString(Constant.USER_DATA_FIELD_TIMER_SYNC, "");
        if(!StringUtil.isValidate(timerSync)) { // 新的线程中发送同步请求
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        //发送同步请求
                        String json = JsonUtil.timerSync2Json();
                        send(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private void saveSyncTimer(final String json){
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    //处理将定时提醒数据保存到数据库
                    List<TimerEntity> timerEntities = JsonUtil.parseTimerSync(json);
                    TimerSqliteHelper timerSqliteHelper = new TimerSqliteHelper(context);
                    for(TimerEntity timerEntity : timerEntities){
                        timerSqliteHelper.insert(timerEntity);
                    }

                    //设置为已经同步
                    SharedPreferences.Editor editor = context.getSharedPreferences(Constant.DB_USER_TABLENAME, context.MODE_PRIVATE).edit();
                    editor.putString(Constant.USER_DATA_FIELD_TIMER_SYNC, Constant.USER_DATA_FIELD_TIMER_SYNC_VALUE);
                    editor.apply();
                    LogUtil.d( Constant.USER_DATA_PERSISTENCE_TIMER_SYNC);
                }catch (Exception e){
                    LogUtil.d( "saveSyncTimer : " + e.getMessage());
                }
            }
        });
    }


    public List<MessageInfo> getMessageInfos(){
        return messageInfos;
    }

    public MessageInfo getMonitorMessage(){
        return monitorMessage;
    }







}


