package com.antsoft.ant.beans.propertyeditor;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * local file로 부터 class를 load한다
 *
 * @author kim sang-kyun
 */
public class FileClassLoader extends ClassLoader implements Serializable, Cloneable{
  private Vector libPaths = new Vector(10, 10);


  public FileClassLoader() {
  }

  public String toString(){
    StringBuffer buf = new StringBuffer();
    for(int i=0; i<libPaths.size(); i++){
      buf.append("PM_FileClassLoader=" + (String)libPaths.elementAt(i));
      if(i != libPaths.size() -1) buf.append(",");
    }
    return buf.toString();
  }

  public synchronized Object clone(){
    try{
      FileClassLoader lo = (FileClassLoader)super.clone();
      lo.libPaths = (Vector)libPaths.clone();
      return lo;
      
    }catch(CloneNotSupportedException e){
      throw new InternalError();
    }
  }


  /**
   * add new library path
   *
   * @param path new library path
   */
  public void addPath(String path){
    libPaths.addElement(path);
  }

  /**
   * remove library path
   *
   * @param path library path to remove
   */
  public void removePath(String path){
    for(int i=0; i<libPaths.size(); i++){
      String libpath = (String)libPaths.elementAt(i);
      if(libpath.equals(path)){
        libPaths.removeElement(path);
      }
    }
  }

  public void removeAllPath(){
    libPaths.removeAllElements();
  }

  public Vector getAllPaths(){
    return libPaths;
  }  

  /**
   * full class name 표현을 file name 표현으로 바꾸어준다
   *
   * @param className full class name
   * @return file name
   */
  private String classNameToFileName(String className){
    String fileName = className;
    if(fileName.indexOf(".") != -1){
      fileName = fileName.replace('.', '/');
    }

    return fileName + ".class" ;
  }

  Class loadIt(String className) throws ClassNotFoundException {
    String fileName = classNameToFileName(className);

    for(int i=0; i<libPaths.size(); i++){
      String path = (String)libPaths.elementAt(i);

      if(path.endsWith(".zip") || path.endsWith(".jar")){

        File libFile = new File( path );
		ZipFile zipLibFile = null;
		try{
		  zipLibFile = new ZipFile( libFile );
		}
		catch(IOException e){
          //e.printStackTrace();
        }

		try{
		  ZipEntry entry = zipLibFile.getEntry(fileName);
          if(entry == null) continue;

     	  InputStream is = zipLibFile.getInputStream(entry);

		  int bufsize = (int)entry.getSize();
		  byte buf[] = new byte[bufsize];

          int len = 0, off = 0;
          //read the entire contents
          while(off < buf.length && (len = is.read(buf, off, buf.length-off)) >= 0){
            off += len;
          }
          is.close();

          //define the class
          return defineClass(className, buf, 0, buf.length);

		}catch(Exception e){
		    //throw new ClassNotFoundException(className);
		}
	  }


      else{
        if(!path.endsWith("\\"))
          path = path + "\\";

        String filePath = path + fileName;
        File classFile = new File(filePath);

        try{
          InputStream is;
          try{
            is = new FileInputStream(classFile);
          }
          catch(FileNotFoundException e){
            continue;
          }

          int bufsize = (int)classFile.length();
          byte buf[] = new byte[bufsize];
          is.read(buf, 0, bufsize);
          is.close();

          //Define the class
          return defineClass(className, buf, 0, buf.length);
        }catch(Exception e){
          throw new ClassNotFoundException(className);
        }
      }
    }

    return null;
  }


  private boolean checkExist(String className){
    String fileName = classNameToFileName(className);

    for(int i=0; i<libPaths.size(); i++){
      String path = (String)libPaths.elementAt(i);
      if(path.endsWith(".zip") || path.endsWith(".jar")){
        File libFile = new File( path );
        ZipFile zipLibFile = null;
    		try{
		     zipLibFile = new ZipFile( libFile );
    		}
		    catch(IOException e){
        }

    		try{
		      ZipEntry entry = zipLibFile.getEntry(fileName);
          if(entry == null) continue;
          else return true;
        }catch(Exception e){}
   	  }
      else{
        if(!path.endsWith("\\"))  path = path + "\\";

        String filePath = path + fileName;
        File classFile = new File(filePath);
        return classFile.exists();
      }
    }
    return false;
  }

  public boolean exist(String className){
    Class c = findLoadedClass(className);
    if(c != null) return true;

    try{
        c = findSystemClass(className);
        if(c!= null) return true;
    }
    catch(ClassNotFoundException e){}

    return checkExist(className);
  }

  public synchronized Class loadClass(String name, boolean resolve)
                                         throws ClassNotFoundException {

    //Try to fine it from the cache
    Class c = findLoadedClass(name);

    //not in cache
    if(c == null){
      //see if it can be loaded by system class loader
      try{
        //no need to call resolveClass() on the result
        //because findSystemClass() loads and links the class.

        return findSystemClass(name);
      }
      catch(ClassNotFoundException e){}

      //try to get it from file
      c = loadIt(name);
    }

    //link class if asked to do so
    if(c!= null && resolve){
      resolveClass(c);
    }
    return c;
  }
}

