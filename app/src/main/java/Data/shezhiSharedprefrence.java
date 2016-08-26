package data;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import utils.LogUtils;

/**
 * ��SharedPreferences�����ļ�Դ
 */
public class shezhiSharedprefrence {
    Context context;
    SharedPreferences shezhiHistory;


    public shezhiSharedprefrence(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        shezhiHistory = context.getSharedPreferences("shezhi_photoyuan", 0);
    }


    /**
     * �ж��Ƿ��ǵ�һ������SharedPreferences
     * @return
     */
    public Boolean isFirstin()
    {
        if(shezhiHistory.getBoolean("firstin",true))
        {
            SharedPreferences.Editor myeditor = shezhiHistory.edit();
            myeditor.putBoolean("firstin",false);
            myeditor.commit();
            return true;
        }else
        {
            return false;
        }
    }

}

