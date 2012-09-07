package com.fonenet.fonemarket;

import java.io.BufferedReader;  
import java.io.File;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;

import android.os.Handler;
  
public class HttpDownloader {  
      
    private URL url = null;  
    private String fileName = null;
    private int fileSize = 0;
    private Handler mHandler;
    
    public HttpDownloader(Handler handler)
    {
    	mHandler = handler;
    	
    }
      
    /**  
     * ����URL�����ļ�,ǰ��������ļ����е��������ı�,�����ķ���ֵ�����ı����е�����  
     * 1.����һ��URL����  
     * 2.ͨ��URL����,����һ��HttpURLConnection����  
     * 3.�õ�InputStream  
     * 4.��InputStream���ж�ȡ����  
     * @param urlStr  
     * @return  
     */  
    public String download(String urlStr){  
        StringBuffer sb = new StringBuffer();  
        String line = null;  
        BufferedReader buffer = null;  
        try {  
            url = new URL(urlStr);  
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();  
            buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));  
            while( (line = buffer.readLine()) != null){  
                sb.append(line);  
            }  
              
        }   
        catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            try {  
                buffer.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return sb.toString();  
    }  
  
    /**  
     *   
     * @param urlStr  
     * @param path  
     * @param fileName  
     * @return   
     *      -1:�ļ����س���  
     *       0:�ļ����سɹ�  
     *       1:�ļ��Ѿ�����  
     */  
    public int downFile(String urlStr, String path, String fileName,int fileType){  
        InputStream inputStream = null;  
        try { 
        	
           /* FileUtils fileUtils = new FileUtils();  
              
            if(fileUtils.isFileExist(path + fileName)){  
            	fileUtils.removeSDFile(path + fileName);
               // return 1;  
            } else { */ 
                inputStream = getInputStreamFromURL(urlStr,fileName); 
            	//URL myURL = new URL(urlStr);
            	//URLConnection conn = myURL.openConnection();
            	//conn.connect();
            	//inputStream = conn.getInputStream();
                FileUtils fileUtils = new FileUtils(mHandler,fileType);    
                if(fileUtils.isFileExist(path + fileName)){  
                	fileUtils.removeSDFile(path + fileName);  //for test
                   // return 1;  
                } 
                File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream,fileSize);  
                if(resultFile == null){  
                    return -1;  
                }  
          /*  }  */
        }   
        catch (Exception e) {  
            e.printStackTrace();  
            return -1;  
        }  
        finally{  
            try {  
                inputStream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return 0;  
    }  
      
    /**  
     * ����URL�õ�������  
     * @param urlStr  
     * @return  
     */  
    public InputStream getInputStreamFromURL(String urlStr,String szfileName) {  
        HttpURLConnection urlConn = null;  
        InputStream inputStream = null;  
        try {  
            url = new URL(urlStr); 
            urlConn = (HttpURLConnection)url.openConnection();  
            inputStream = urlConn.getInputStream(); 
            this.fileSize = urlConn.getContentLength();
            if(szfileName.isEmpty())
            {
            	this.fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
            }
            else
            {
            	this.fileName = szfileName;
            }
              
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return inputStream;  
    }  
}  
