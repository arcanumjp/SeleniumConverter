package jp.arcanum.magnus.utl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AppProperties {

	/**
	 * 監視するファイル名
	 */
	private String _filename;
	
	/**
	 * 読み込んだ内容
	 */
	private final Map _properties = new HashMap();

	/**
	 * 
	 * @param filename
	 */
	public AppProperties(final InputStream fis){

        InputStreamReader ir = null;
        BufferedReader br = null;
	    try {
	        ir = new InputStreamReader(fis , "UTF-8");
	        br = new BufferedReader(ir);
	        
	        String line;
	        while((line = br.readLine()) != null){
	        	line = line.trim();
	        	
	        	// コメントやイコールのない行は無視
	        	if(line.startsWith("#"))continue;
	        	if(line.indexOf("=")<0)continue;
	        	
	        	//　入力されたキーと同一のキーか調べる
	        	int eq = line.indexOf("=");
	        	String key = line.substring(0,eq).trim();
	        	String value = line.substring(eq + 1, line.length()).trim();
	        	_properties.put(key, value);
	        }
	        
	      } catch (Exception e) {
	    	  throw new RuntimeException("ファイル読み込み中に失敗",e);
	      }
	      finally{
	    	  try{
			        fis.close();
			        ir.close();
			        fis.close();
	    	  }
	    	  catch(Exception e){
	    		  throw new RuntimeException("ファイルを閉じるときに失敗", e);
	    	  }
	    	  
	      }
	
	
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getPropertyString(final String key){
		return (String)_properties.get(key);
	}
	

	public List getPropertyKeys(final String regexkey){

		List ret = new ArrayList();
		
		Iterator ite = _properties.keySet().iterator();
		
		while(ite.hasNext()){
			
			String key = (String)ite.next();
			if(key.startsWith(regexkey)){
				ret.add(key);
			}
		
		}
		
		return ret;
	}

	public final String toString(){
		
		StringBuffer ret = new StringBuffer();
		
		Iterator ite = _properties.keySet().iterator();
		while(ite.hasNext()){
			Object key = ite.next();
			String value = (String)_properties.get(key);
			ret.append( key + " --> " + value + "\n");
		}
		
		return ret.toString();
		
	}


}


