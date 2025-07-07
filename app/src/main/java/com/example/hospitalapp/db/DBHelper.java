package com.example.hospitalapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "hospital.db";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 用户表
        db.execSQL("CREATE TABLE IF NOT EXISTS user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "mobile TEXT, " +
                "id_card TEXT, " +
                "gender TEXT, " +
                "age INTEGER)");

        // 科室表
        db.execSQL("CREATE TABLE IF NOT EXISTS department (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT)");

        // 医生表
        db.execSQL("CREATE TABLE IF NOT EXISTS doctor (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "department_id INTEGER, " +
                "specialty TEXT)");

        // 预约表
        db.execSQL("CREATE TABLE IF NOT EXISTS appointment (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "doctor_id INTEGER, " +
                "date TEXT, " +           // 格式：yyyy-MM-dd
                "time_slot TEXT, " +      // 上午 / 下午
                "status TEXT, " +         // 待支付 / 已支付 / 已取消
                "pay_channel TEXT)");     // 支付渠道

        // 插入默认数据
        insertDefaultData(db);
    }

    private void insertDefaultData(SQLiteDatabase db) {
        // 插入科室
        db.execSQL("INSERT INTO department (name) VALUES ('儿科')");
        db.execSQL("INSERT INTO department (name) VALUES ('肠胃科')");
        db.execSQL("INSERT INTO department (name) VALUES ('内科')");
        db.execSQL("INSERT INTO department (name) VALUES ('精神科')");
        db.execSQL("INSERT INTO department (name) VALUES ('耳鼻喉科')");
        db.execSQL("INSERT INTO department (name) VALUES ('外科')");

        // 儿科医生
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('张晓婧', 1, '擅长新生儿护理，对新生儿常见疾病的诊治和护理有丰富经验')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('李泽宇', 1, '专注儿童免疫系统疾病，擅长儿童疫苗接种指导和免疫相关疾病治疗')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('王宇航', 1, '专门从事儿童呼吸系统疾病的诊治，对儿童哮喘等呼吸道疾病有独特见解')");

        // 肠胃科医生
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('刘凯文', 2, '擅长消化系统疾病的诊断和治疗，尤其是消化道炎症性疾病')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('孙悦', 2, '专注胃肠道疾病，对胃炎、结肠炎等疾病有丰富临床经验')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('周俊杰', 2, '擅长各类肠胃疾病的诊治，尤其是功能性胃肠病的治疗')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('钱媛媛', 2, '专门研究胃食管反流病，对食管疾病有深入研究')");

        // 内科医生
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('吴若凡', 3, '擅长心血管疾病的诊断和治疗，包括高血压、冠心病等')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('郑浩然', 3, '专注呼吸系统疾病，对慢性支气管炎、肺炎等有丰富经验')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('冯雪婷', 3, '擅长内分泌系统疾病，尤其是糖尿病和甲状腺疾病的诊治')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('程志强', 3, '专注老年病的诊治，对老年人常见病、多发病有独特见解')");

        // 精神科医生
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('何清清', 4, '擅长心理治疗，对抑郁症、焦虑症等心理疾病有丰富经验')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('蒋宋', 4, '专注精神分析治疗，擅长处理各类心理障碍')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('彭思韵', 4, '擅长各类精神障碍的诊断和治疗，尤其是青少年心理问题')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('罗远航', 4, '专注行为疗法，对强迫症、恐惧症等有特殊治疗方法')");

        // 耳鼻喉科医生
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('曾兴都', 5, '擅长耳部疾病的诊治，包括中耳炎、耳聋等')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('石确确', 5, '专注鼻部疾病，对鼻炎、鼻窦炎等有丰富经验')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('唐岩', 5, '擅长喉部疾病的诊治，尤其是声带疾病')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('高旭东', 5, '全科型医生，擅长耳鼻喉各类疾病的综合诊治')");

        // 外科医生
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('陈丽华', 6, '擅长骨科手术，对骨折、关节炎等有丰富经验')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('徐子墨', 6, '专注普外科手术，擅长各类常见外科手术')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('蔡俊辉', 6, '擅长泌尿外科手术，对泌尿系统疾病有深入研究')");
        db.execSQL("INSERT INTO doctor (name, department_id, specialty) VALUES ('黎欣怡', 6, '专注胸外科手术，擅长肺部手术和微创手术')");

        // 插入默认用户
        db.execSQL("INSERT INTO user (username, password, mobile, id_card, gender, age) VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{"张三", "123456", "13800138000", "110101199001011234", "男", 30});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS department");
        db.execSQL("DROP TABLE IF EXISTS doctor");
        db.execSQL("DROP TABLE IF EXISTS appointment");

        onCreate(db);
    }
}
