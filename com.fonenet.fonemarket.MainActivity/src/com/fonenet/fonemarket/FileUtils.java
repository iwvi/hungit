package com.fonenet.fonemarket;


import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.os.Environment;  
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class FileUtils {  
  private String SDPATH;  
    
  private static int BUFF_SIZE = 8 * 1024;   
  private int length = 0;
  private int downloadSize = 0;
  private Handler mHandler;
  private int fileType;

private byte[] buffer; 
    
  public String getSDPATH(){  
      return SDPATH;  
  }  
    
  public FileUtils(Handler handler,int szfileType){  
      //得到当前外部存储设备的目录( /SDCARD )  
      SDPATH = Environment.getExternalStorageDirectory() + "/"; 
      mHandler = handler;
      fileType = szfileType;
  }  
    
  /**  
   * 在SD卡上创建文件  
   * @param fileName  
   * @return  
   * @throws IOException  
   */  
  public File createSDFile(String fileName) throws IOException{  
      File file = new File(SDPATH + fileName);  
      file.createNewFile();  
      return file;  
  }  
  
  /**  
   * 在SD卡上创建文件  
   * @param fileName  
   * @return  
   * @throws IOException  
   */  
  public boolean removeSDFile(String fileName) throws IOException{  
      File file = new File(SDPATH + fileName);  
      return file.delete(); 
  } 
    
  /**  
   * 在SD卡上创建目录  
   * @param dirName  
   * @return  
   */  
  public File createSDDir(String dirName){  
      File dir = new File(SDPATH + dirName);  
      dir.mkdir();  
      return dir;  
  }  
    
  /**  
   * 判断SD卡上的文件夹是否存在  
   * @param fileName  
   * @return  
   */  
  public boolean isFileExist(String fileName){  
      File file = new File(SDPATH + fileName);  
      return file.exists();  
  }  
    
  /**  
   * 将一个InputStream里面的数据写入到SD卡中  
   * @param path  
   * @param fileName  
   * @param input  
   * @return  
   */  
  public File write2SDFromInput(String path,String fileName,InputStream input,int totalSize){  
      File file = null;  
      OutputStream output = null;  
      try {  
          createSDDir(path);  
          file = createSDFile(path + fileName);  
          output = new FileOutputStream(file);  
          byte[] buffer = new byte[BUFF_SIZE];  
        //  while((input.read(buffer)) != -1){  
        //      output.write(buffer);  
        //  }  
          while((length = input.read(buffer)) != -1){  
              output.write(buffer,0,length);
              downloadSize += length;
              if(downloadSize == totalSize)
              {
            	  sendMsg(FoneConstValue.FILE_DOWNLOAD_SUCCSS,SDPATH+path+fileName);
              }
          } 
          output.flush();  
      }   
      catch (Exception e) {  
          e.printStackTrace();  
      }  
      finally{  
          try {  
              output.close();  
          } catch (IOException e) {  
              e.printStackTrace();  
          }  
      }  
      return file;  
  }  
  public void sendMsg(int flag,String string)
	{
	 // Looper mainLooper = Looper.getMainLooper ();
	 // Handler mHandler = new Handler(mainLooper);
	  String str = string;
	    Message msg = new Message();
	  //  Message msg = mHandler .obtainMessage(1, 1, 1, "ok!!!");
	    msg.what = fileType;
	    msg.obj = str;
	    msg.arg1 = flag;
	    mHandler.sendMessage(msg);
	}
  public static void upZipFile(File zFile, String folderPath) throws ZipException, IOException {
      File desDir = new File(folderPath);
      if (!desDir.exists()) {
          desDir.mkdirs();
      }
      ZipFile zf = new ZipFile(zFile);
      for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
          ZipEntry entry = ((ZipEntry)entries.nextElement());
          InputStream in = zf.getInputStream(entry);
          String str = folderPath + File.separator + entry.getName();
          str = new String(str.getBytes("8859_1"), "GB2312");
          File desFile = new File(str);
          if (!desFile.exists()) {
              File fileParentDir = desFile.getParentFile();
              if (!fileParentDir.exists()) {
                  fileParentDir.mkdirs();
              }
              desFile.createNewFile();
          }
          OutputStream out = new FileOutputStream(desFile);
          byte buffer[] = new byte[BUFF_SIZE];
          int realLength;
          while ((realLength = in.read(buffer)) > 0) {
              out.write(buffer, 0, realLength);
          }
          in.close();
          out.close();
      }
  }
  public static String getFileNameEx(String fileName)
  {
	  int start=fileName.lastIndexOf("/");  
      int end=fileName.lastIndexOf(".");  
      if(start!=-1 && end!=-1){  
          return fileName.substring(start+1,end);    
      }else{  
          return null;  
      }  
  }

}  