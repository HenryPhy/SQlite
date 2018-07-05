package com.example.day_8;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private EditText et_id,et_name,et_sex;
    private ListView lv;
    MySqliteHelper helper;//数据库操作帮助类
    SQLiteDatabase db;//数据库操作类
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_id = (EditText) findViewById(R.id.et_id);
        et_name = (EditText) findViewById(R.id.et_name);
        et_sex = (EditText) findViewById(R.id.et_sex);
        lv = (ListView) findViewById(R.id.lv);
        //实例化数据库操作帮助类
        helper=new MySqliteHelper(this);
        //获取一个可读写的db
        db=helper.getWritableDatabase();

        Cursor cursor = db.query("user", null, null, null, null, null, "_id desc");
        //实例化适配器，参数1.上下文对象，2.布局文件，3.结果集，4.从哪个列取值，5.方到哪个控件上，6.适配器的模式（观察者）
        adapter=new SimpleCursorAdapter(this,R.layout.item,cursor,
                new String[]{"_id","name","sex"},
                new int[]{R.id.tv_id,R.id.tv_name,R.id.tv_sex},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);
    }
    public void click(View view) {
        String id = et_id.getText().toString();
        String name = et_name.getText().toString();
        String sex = et_sex.getText().toString();
        switch (view.getId()){
            case R.id.btn_add://增加
                ContentValues cv=new ContentValues();
                cv.put("name",name);
                cv.put("sex",sex);
                long user = db.insert("user", null, cv);
                if(user>0){
                    Toast.makeText(this, "插入数据成功", Toast.LENGTH_SHORT).show();
                    Cursor cursor = db.query("user", null,null,null, null, null, "_id desc");
                    adapter.changeCursor(cursor);
                }
                break;
            case R.id.btn_update://修改
                ContentValues cv2=new ContentValues();
                cv2.put("name",name);
                int user1 = db.update("user", cv2, "_id=?", new String[]{id});
                if(user1>0){
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                    Cursor cursor = db.query("user", null,null,null, null, null, "_id desc");
                    adapter.changeCursor(cursor);
                }
                break;
            case R.id.btn_delete://删除
                int user2 = db.delete("user", "_id=?", new String[]{id});
                if(user2>0){
                    Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
                    Cursor cursor = db.query("user", null,null,null, null, null, "_id desc");
                    adapter.changeCursor(cursor);
                }
                break;
            case R.id.btn_query://查询
                //声明where后面的条件
                String where="";
                //实例化条件对应的值的集合
                List<String> list=new ArrayList<>();
                boolean flag=false;//标记当前是否是where后面的第一个条件（为false时是第一个条件），如果是第一个条件签名不加and,如果不是第一个条件前面加and
                //如果输入编号不为空
                if(!id.isEmpty()){
                    where+="_id=? ";
                    list.add(id);
                    flag=true;//第一个where后面的条件已经存在
                }
                if(!name.isEmpty()){
                    if(flag){//如果where后面的第一个条件已经存在就拼接带and的条件
                        where+=" and name=? ";
                    }else {
                        where+="name=? ";
                        flag=true;
                    }
                    list.add(name);

                }
                if(!sex.isEmpty()){
                    if(flag){
                        where+=" and sex=? ";
                    }else {
                        where+="sex=? ";
                    }
                    list.add(sex);
                }
                Log.i("tag",where);
                //声明放占位符对应的值的数组
                String[] arr=new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    //循环把集合中的值取出放到数组中
                    arr[i]=list.get(i);
                }
                Cursor cursor = db.query("user", new String[]{"_id","name","sex"}, where, arr, null, null, null);
                //改变数据源
                adapter.changeCursor(cursor);
                break;
        }
    }
}