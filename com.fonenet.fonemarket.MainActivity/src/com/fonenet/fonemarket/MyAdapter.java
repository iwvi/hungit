package com.fonenet.fonemarket;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author chenzheng_java
 * @description 该类的部分实现模仿了SimpleAdapter
 */
public class MyAdapter extends BaseAdapter {

	Handler uiHandler;
      
    private ArrayList<HashMap<String, Object>> data;  
    /** 
     * LayoutInflater 类是代码实现中获取布局文件的主要形式 
     *LayoutInflater layoutInflater = LayoutInflater.from(context); 
     *View convertView = layoutInflater.inflate(); 
     *LayoutInflater的使用,在实际开发种LayoutInflater这个类还是非常有用的,它的作用类似于 findViewById(), 
    不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！ 
    而findViewById()是找具体xml下的具体 widget控件(如:Button,TextView等)。 
     */  
    private LayoutInflater layoutInflater;  
    private Context context;  
      
      
    public MyAdapter(Context context,ArrayList<HashMap<String, Object>> data) {  
          
        this.context = context;  
        this.data = data;  
        this.layoutInflater = LayoutInflater.from(context); 
        this.uiHandler = ((Tab1Activity)context).handler;
    }  
  
    /** 
     *获取列数  
     */  
    public int getCount() {  
        return data.size();  
    }  
    /** 
     *获取某一位置的数据  
     */  
    public Object getItem(int position) {  
        return data.get(position);  
    }  
    /** 
     *获取唯一标识 
     */  
    public long getItemId(int position) {  
        return position;  
    }  
  
    /** 
     * android绘制每一列的时候，都会调用这个方法 
     */  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        ZuJian zuJian = null;  
        if(convertView==null){  
            zuJian = new ZuJian();  
            // 获取组件布局  
            convertView = layoutInflater.inflate(R.layout.activity_tab1, null);  
            zuJian.imageView = (ImageView) convertView.findViewById(R.id.image);  
            zuJian.titleView = (TextView) convertView.findViewById(R.id.title);  
            zuJian.infoView = (TextView) convertView.findViewById(R.id.info);  
            zuJian.button = (Button) convertView.findViewById(R.id.view_btn);  
            // 这里要注意，是使用的tag来存储数据的。  
            convertView.setTag(zuJian);  
        }  
        else {  
            zuJian = (ZuJian) convertView.getTag();  
        }  
        // 绑定数据、以及事件触发  
        
        zuJian.imageView.setBackgroundResource((Integer) data.get(position).get("image"));  
        zuJian.titleView.setText((String)data.get(position).get("title"));  
        zuJian.infoView.setText((String)data.get(position).get("info")); 
        zuJian.button.setOnClickListener(new OnClickListener(){  
  
            public void onClick(View v) { 
            	int p = position;
            	String z = (String)data.get(position).get("title");
  
            	
                showInfo(p); 
                
                 // download file
                new Thread(){
                	public void run(){
                		//try {  http://192.168.7.76:8080/glxt/
                			 HttpDownloader downloader = new HttpDownloader(uiHandler);  
                	         //   int lrc = downloader.downFile("http://192.168.7.66/Market.apk","test/");  
                	            int lrc = downloader.downFile("http://192.168.7.76:8080/glxt/interface/usstore.jsp?projectid=76&type=3&date=0&version=1&imsi=9001010123456789","test/","",FoneConstValue.FILE_TYPE_STORE_APP); 
                	            System.out.println(lrc);  
        					//下载文件，参数：第一个URL，第二个存放路径
        			//	} catch (ClientProtocolException e) {
        					// TODO Auto-generated catch block
        				//	e.printStackTrace();
        			//	} catch (IOException e) {
        					// TODO Auto-generated catch block
        			//		e.printStackTrace();
        			//	}
                	}
                }.start();
                
               // installApk(Environment.getExternalStorageDirectory()+"/test/Market.apk");
            }  
              
        });  
        return convertView;  
    }  
  
    /** 
     *当用户点击按钮时触发的事件，会弹出一个确认对话框 
     */  
     public void showInfo(int  pos){    
  
                 new AlertDialog.Builder(context)    
  
                 .setTitle("我的listview")    
  
                .setMessage("介绍..."+ pos)    
  
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {    
  
                public void onClick(DialogInterface dialog, int which) {    
  
                     }    
  
                 })    
  
               .show();    
  
            }  
    
  
}  