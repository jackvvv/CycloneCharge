package com.sinia.cyclonecharge.utils;

/**
 * Created by 忧郁的眼神 on 2016/7/26.
 */
public class Constants {

//        public static String BASE_URL = "http://192.168.0.106:8080/inCharge/xiNaiInterfaceController" +
//            ".do?interface&sign=123&messageFormat=json/xml&locale=zh_CN&appKey=001&method=";
    public static String BASE_URL = "http://123.57.89.35:8088/inCharge/xiNaiInterfaceController" +
            ".do?interface&sign=123&messageFormat=json/xml&locale=zh_CN&appKey=001&method=";


    /**
     * 支付宝支付
     */
    // 商户PID
    public static final String PARTNER = "2088521208983233";
    // 商户收款账号
    public static final String SELLER = "2088521208983233";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICXAIBAAKBgQCuUWgaMx7qP9sxRTq5BCUXWJWwzqPAz6AQnF13NcKvVkpx7z1J" +
            "cX7jEkhJoVZlJdi4MbVttOEhmflNhX34p7g+c5/5eDnsJ5oSjsmOMiQ5pBc+4yxK" +
            "4ocWCOSx7/6KYgBcwDeNaHdAqQdwD/BaZwcjeCh2VooVGuAWWWFIcfAIvwIDAQAB" +
            "AoGBAJ+VHSWnGS1ASk1f4HOSjNZDnb5gXl9cdF/gllxVFiboa3zElSnQQmZkzVLC" +
            "5dHxhOaTc1G83sGhMS87/2gNO0hGPR7hwOzCMnkOdrTy1/EagUoWLzK509nGcvSW" +
            "HWqf1RlhrxpIEHK6FusdnJH3Al0ZDUZB9IQvioV0Udv39jlpAkEA10WK/qoQ2y+x" +
            "L/U4Yl7xvXqalvSNH/O5Qv74uC/FpjW/bffyRnF1/wmXGh6FMOP8yxdsEufXgHXO" +
            "Zy6Gg+gDEwJBAM9MUPG+Y5hND8D6KvlbKe2oujKV2zBJlvjVDpKGm65L54PEjs83" +
            "aGduBdzUc0Rcl2NfJw1GUHrIIY7B3hIV7SUCQCIZTOwL6kGd61YqZLUITwhxfD0/" +
            "HX+bLpRTKPib8JXkW59CsCjCXit4zGuBvB1Db86HtkHEYZlij8A+WWZZgOsCQEfU" +
            "hPPPaglbIotW5E81VKkCX0qp/KKVzVd51CvXsthJevaAxI9u/qFgUW28vBhDET4g" +
            "uZJy++4frOkfqkiQCnUCQEKj0FdtdbWiNXGHdRbHNDo8beL07V7Mo4BQTZdu5KAj" +
            "w3ewlrBWc+oFg1VLOsSfgMisCXQ9MW0FPKTNS9pQCyU=";

    public final static String BOMB_KEY = "019852bf9aeb761ffb61b2d71ed3f8f7";

    public static class RESPONSE_TYPE {
        public final static int STATUS_SUCCESS = 200;

        public final static int STATUS_EXCEPTION = 300;

        public final static int STATUS_FAILED = 500;

        public final static int STATUS_3 = 400;
    }

    public static class SP_HELPER {
        public final static String USER_INFO = "user_info";

        public final static String IS_LOGIN = "is_login";

        public final static String HOT_COIN = "hot_coin";

        public final static String BROWSE_RECORD = "browse_record";

        public final static String IMG_URL = "img_url";

        public final static String IS_SET_PWD = "is_set_pwd";

        public final static String HAS_MIMA = "has_mima";
    }
}
