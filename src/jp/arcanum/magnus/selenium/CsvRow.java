package jp.arcanum.magnus.selenium;

import java.util.ArrayList;
import java.util.List;

public class CsvRow {

	//private List _fields = new ArrayList();
	private String[] _fields;
	
	public CsvRow(int colcnt){
		_fields = new String[colcnt];
	}
	
	
	public void addField(final int col, final String value){
		
		_fields[col] = value;
		
	}
	
	public final String toString(){
		
		String ret = "";
		
		for(int i = 0 ; i < _fields.length; i++){
			if(_fields[i] == null){
				ret = ret + "\"\",";
			}
			else{
				ret = ret + "\"" + _fields[i] + "\",";
			}
		}
		
		return ret;
		
	}
	
	
}
