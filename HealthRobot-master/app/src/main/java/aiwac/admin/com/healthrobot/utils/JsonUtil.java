package aiwac.admin.com.healthrobot.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import aiwac.admin.com.healthrobot.bean.HealthTestResultAbstract;
import aiwac.admin.com.healthrobot.bean.HealthTestResultAbstractInfo;
import aiwac.admin.com.healthrobot.bean.HealthTestResultDetail;
import aiwac.admin.com.healthrobot.bean.LectureAVDetail;
import aiwac.admin.com.healthrobot.bean.LectureArticleDetail;
import aiwac.admin.com.healthrobot.bean.LectureCourse;
import aiwac.admin.com.healthrobot.bean.LectureCourseAbstractInfo;
import aiwac.admin.com.healthrobot.bean.MessageInfo;
import aiwac.admin.com.healthrobot.bean.TimerEntity;
import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.bean.WifiInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.UserData;
import aiwac.admin.com.healthrobot.exception.JsonException;

import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_HEALTH_TEST_RESULT_ABSTRACT_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_HEALTH_TEST_RESULT_DETAIL_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_ARTICLE_ABSTRACT_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_ARTICLE_DETAIL_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE;


/**     对象和json直接的相互转换
 * Created by luwang on 2017/10/23.
 */

public class JsonUtil {


    /*     用户 Json 字符串格式
        String jsonStr =
                {
                "opt" : "insert"
                "user":
                    [{"id":1,"number":"15911112222","name":"zhangsan"}]
                }
     */

    //解析json获取操作 ，如插入，更新等
    public static String parseOpt(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String opt = root.getString(Constant.JSON_OPT);
            LogUtil.d(Constant.JSON_PARSE_SUCCESS + opt);
            return opt;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析businessType 获取事物类型
    public static String parseBusinessType(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_CODE);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析errorCode 获取消息是否成功传递到后台
    public static String parseErrorCode(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_TIMER_STATUS);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析ErrorDesc
    public static String parseErrorDesc(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_ERRORDESC);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析用户登录密码
    public static String parsePWD(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.USER_DATA_FIELD_PASSWORD);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析uuid 获取定时任务消息的UUID属性
    public static String parseTimerUUID(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_UUID);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //将timerEntity对象转换成json字符串
    public static String timerEntity2Json(TimerEntity timerEntity){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, timerEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, Constant.WEBSOCKET_TIMER_BUSSINESSTYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, timerEntity.getUuid());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, timerEntity.getClientType());
            root.put(Constant.WEBSOCKET_TIMER_OPERATIONTYPE, timerEntity.getOperationType());
            root.put(Constant.WEBSOCKET_TIMER_ATTENTIONTYPE, timerEntity.getAttentionType());
            root.put(Constant.WEBSOCKET_TIMER_ATTENTIONCONTENT, timerEntity.getAttentionContent());
            root.put(Constant.WEBSOCKET_TIMER_ACTIVATIONMODE, timerEntity.getActivationMode());
            root.put(Constant.WEBSOCKET_TIMER_ACTIVATEDTIME, timerEntity.getActivatedTime());

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }



    //解析businessType 获取事物类型
    public static MessageInfo parseMessageInfo(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);

            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
            messageInfo.setUuid(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));
            messageInfo.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
            messageInfo.setDescription(root.getString(Constant.WEBSOCKET_MESSAGE_DESCRIPTION));
            messageInfo.setTime(StringUtil.longToString(root.getString(Constant.WEBSOCKET_MESSAGE_TIME)));

            return messageInfo;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }



    //发送定时提醒同步请求
    public static String timerSync2Json(){
        try{
            //基本信息
            JSONObject root = new JSONObject();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, UserData.getUserData().getNumber());
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, Constant.WEBSOCKET_SYNC_REMINDER_BUSSINESSTYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, Constant.WEBSOCKET_MESSAGE_CLIENTTYPE_NUMBER);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //解析同步定时提醒json
    public static List<TimerEntity> parseTimerSync(String jsonStr){
        try{
            List<TimerEntity> timerEntities = new ArrayList<TimerEntity>();
            JSONObject root = new JSONObject(jsonStr);

            JSONArray jsonArray = root.getJSONArray(Constant.WEBSOCKET_BUSINESS_DATA);
            for(int i=0; i<jsonArray.length(); i++) {
                //共同设置
                TimerEntity timerEntity = new TimerEntity();
                timerEntity.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                timerEntity.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                timerEntity.setOperationType(Constant.TIMER_OPERATIONTYPE_ADD);
                timerEntity.setBusinessType(Constant.WEBSOCKET_SYNC_REMINDER_BUSSINESSTYPE_CODE);
                timerEntity.setCommit(true);
                timerEntity.setOpen(true);

                JSONObject timerJson = jsonArray.getJSONObject(i);
                //不同的定时提醒
                timerEntity.setUuid(timerJson.getString(Constant.WEBSOCKET_MESSAGE_UUID));
                timerEntity.setActivatedTime(timerJson.getString(Constant.WEBSOCKET_TIMER_ACTIVATEDTIME));
                timerEntity.setAttentionType(timerJson.getString(Constant.WEBSOCKET_TIMER_ATTENTIONTYPE));
                timerEntity.setAttentionContent(timerJson.getString(Constant.WEBSOCKET_TIMER_ATTENTIONCONTENT));
                timerEntity.setActivationMode(timerJson.getString(Constant.WEBSOCKET_TIMER_ACTIVATIONMODE));

                timerEntities.add(timerEntity);
            }

            return timerEntities;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析json获取用户注册是否成功
    public static boolean isUserRegisterSucess(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String isSuccess = root.getString(Constant.USER_REGISTER_ISSUCCESS);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + isSuccess);
            return isSuccess.equals(Constant.USER_REGISTER_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json获取验证码
    public static String parseCheckcode(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String checkcode = jsonObject.getString(Constant.USER_REGISTER_CHECKCODE);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + checkcode);

            return checkcode;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json 判断用户是否注册
    public static boolean isUserRegisted(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String isNumberExist = jsonObject.getString(Constant.USER_REGISTER_PHONENUMBER_EXIST);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + isNumberExist);

            return isNumberExist.equals(Constant.USER_REGISTER_PHONENUMBER_EXIST_YES);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json获取用户
    public static User parseJson(String jsonStr) {

        //String jsonStr = "{"id":1,"number":"15911112222","name":"zhangsan"}";
        try {
            User user = new User();
            //将json字符串jsonData装入JSON数组，即JSONArray
            //jsonData可以是从文件中读取，也可以从服务器端获得
            JSONObject root = new JSONObject(jsonStr);

            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            // for (int i = 0; i< jsonArray.length(); i++) {
            //循环遍历，依次取出JSONObject对象
            //用getInt和getString方法取出对应键值
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            user.setId(jsonObject.getInt("id"));
            user.setName(jsonObject.getString("name"));
            user.setNumber(jsonObject.getString("number"));

            LogUtil.d( Constant.JSON_PARSE_SUCCESS + user.toString());
            // }

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static String parseObject(User user, String opt) {
        try{
            JSONObject root = new JSONObject();
            root.put(Constant.JSON_OPT, opt);

            JSONArray jsonArray = new JSONArray();

            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put(Constant.USER_REGISTER_NUMBER, user.getNumber());
            userJson.put("passwd", user.getPassword());
            userJson.put("name", user.getName());
            jsonArray.put(0, userJson);

            root.put(Constant.JSON_OBJECT_USER_NAME, jsonArray);


            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());

            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }

    }


    public static String parseCourseId(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            return root.getString("courseID");
        }catch (Exception e) {
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }




    public static String setBussinessType(String bussinessType){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, bussinessType);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    public static String wifiInfoToJson(WifiInfo wifiInfo){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, "0000");
            root.put("ssid", wifiInfo.getSsid());
            root.put("password",wifiInfo.getPassword());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    //解析json 获取讲座  视频 音频的摘要信息
    public static LectureCourseAbstractInfo parseLectureAVAbstractInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.RETURN_CODE_200)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                LectureCourseAbstractInfo lectureAVAbstractInfo = new LectureCourseAbstractInfo();

                lectureAVAbstractInfo.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureAVAbstractInfo.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureAVAbstractInfo.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureAVAbstractInfo.setUuid(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                //具体的课程摘要
                String dataStr = root.getString(Constant.WEBSOCKET_MESSAGE_DATA);
                JSONObject data = new JSONObject(dataStr);
                JSONArray jsonArray = data.getJSONArray(Constant.WEBSOCKET_MESSAGE_ITEMS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    LectureCourse lectureCourse = new LectureCourse();
                    JSONObject lectureCourseJson = jsonArray.getJSONObject(i);

                    //  在json里获取某一讲座课程的摘要信息
                    lectureCourse.setLectureID(lectureCourseJson.getString(Constant.WEBSOCKET_MESSAGE_LECTUREID));
                    lectureCourse.setName(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_NAME));
                    lectureCourse.setUpdateTime(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_UPDATETIME));
                    lectureCourse.setDescription(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_DESCRIPTION));
                    lectureCourse.setCover(ImageUtil.getBitmap(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_COVER)));
                    lectureCourse.setDuration(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_DURATION));

                    lectureAVAbstractInfo.getLectureCourseAbstracts().add( lectureCourse);
                }
                return lectureAVAbstractInfo;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }

    //解析json 获取讲座  文章的摘要信息
    public static LectureCourseAbstractInfo parseLectureArticleAbstractInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.RETURN_CODE_200)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                LectureCourseAbstractInfo lectureArticleAbstractInfo = new LectureCourseAbstractInfo();

                lectureArticleAbstractInfo.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureArticleAbstractInfo.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureArticleAbstractInfo.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureArticleAbstractInfo.setUuid(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                //具体的课程摘要
                String dataStr = root.getString(Constant.WEBSOCKET_MESSAGE_DATA);
                JSONObject data = new JSONObject(dataStr);
                JSONArray jsonArray = data.getJSONArray(Constant.WEBSOCKET_MESSAGE_ITEMS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    LectureCourse lectureCourse = new LectureCourse();
                    JSONObject lectureCourseJson = jsonArray.getJSONObject(i);

                    //  在json里获取某一讲座课程的摘要信息
                    lectureCourse.setLectureID(lectureCourseJson.getString(Constant.WEBSOCKET_MESSAGE_LECTUREID));
                    lectureCourse.setName(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_NAME));
                    lectureCourse.setUpdateTime(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_UPDATETIME));

                    lectureArticleAbstractInfo.getLectureCourseAbstracts().add( lectureCourse);
                }
                return lectureArticleAbstractInfo;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }


    //解析json 获取讲座  视音频的详细信息
    public static LectureAVDetail parseLectureAVDetailInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.RETURN_CODE_200)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                LectureAVDetail  lectureAVDetail = new  LectureAVDetail();

                lectureAVDetail.setLectureID(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureAVDetail.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureAVDetail.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureAVDetail.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                lectureAVDetail.setLink(root.getString(Constant.WEBSOCKET_MESSAGE_LECTURE_AV_LINK));

                return lectureAVDetail;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }



    //解析json 获取讲座  文章的详细信息
    public static LectureArticleDetail parseLectureArticleDetailInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.RETURN_CODE_200)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                LectureArticleDetail  lectureArticleDetail = new LectureArticleDetail();

                lectureArticleDetail.setLectureID(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureArticleDetail.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureArticleDetail.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureArticleDetail.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                lectureArticleDetail.setLectureContext(root.getString(Constant.WEBSOCKET_MESSAGE_LECTURE_CONTEXT));

                return lectureArticleDetail;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }


    //解析json 获取健康检测结果摘要的信息
    public static HealthTestResultAbstractInfo parseHealthTestResultAbstractInfo (String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.RETURN_CODE_200)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                HealthTestResultAbstractInfo  healthTestResultAbstractInfo = new HealthTestResultAbstractInfo();

                healthTestResultAbstractInfo.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                healthTestResultAbstractInfo.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                healthTestResultAbstractInfo.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                healthTestResultAbstractInfo.setUuid(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                //具体的课程摘要
                String dataStr = root.getString(Constant.WEBSOCKET_MESSAGE_DATA);
                JSONObject data = new JSONObject(dataStr);
                JSONArray jsonArray = data.getJSONArray(Constant.WEBSOCKET_MESSAGE_ITEMS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    HealthTestResultAbstract healthTestResultAbstract = new HealthTestResultAbstract();
                    JSONObject healthTestResultAbstractJson = jsonArray.getJSONObject(i);

                    //  在json里获取某一讲座课程的摘要信息
                    healthTestResultAbstract.setResultID(healthTestResultAbstractJson.getString(Constant.WEBSOCKET_MESSAGE_RESULTID));
                    healthTestResultAbstract.setDescription(healthTestResultAbstractJson.getString(Constant.WEBSOCKET_MESSAGE_DESCRIPTION));
                    healthTestResultAbstract.setUpdateTime(healthTestResultAbstractJson.getString(Constant.WEBSOCKET_MESSAGE_UPDATETIME));

                    healthTestResultAbstractInfo.getHealthTestResultAbstract().add( healthTestResultAbstract);
                }
                return healthTestResultAbstractInfo;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }


    //解析json 获取健康检测结果的详细信息
    public static HealthTestResultDetail parseHealthTestResultDetail(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.RETURN_CODE_200)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                HealthTestResultDetail healthTestResultDetail = new  HealthTestResultDetail();

                healthTestResultDetail.setLectureID(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                healthTestResultDetail.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                healthTestResultDetail.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                healthTestResultDetail.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                healthTestResultDetail.setResultContext(root.getString(Constant.WEBSOCKET_MESSAGE_RESULT_CONTEXT));

                return healthTestResultDetail;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }



    //生成查询讲座音频摘要的json
    public static String lectureAudioAbstract2Json( ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    //生成查询讲座视频摘要的json
    public static String lectureVideoAbstract2Json(){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }
    //生成查询讲座音视频详情的json
    public static String lectureAVDetail2Json(String lectureID ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_LECTUREID, lectureID);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //生成查询讲座文章摘要的json
    public static String lectureArticleAbstract2Json( ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_ARTICLE_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //生成查询讲座文章详情的json
    public static String lectureArticleDetail2Json(String lectureID){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_ARTICLE_DETAIL_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_LECTUREID, lectureID);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    //生成查询健康检测结果摘要的json
    public static String healthTestResultAbstract2Json(){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_HEALTH_TEST_RESULT_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    //生成查询健康检测结果详情的json
    public static String healthTestResultDetail2Json(String resultID){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_HEALTH_TEST_RESULT_DETAIL_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_HEALTH_TEST_RESULT, resultID);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }
}
