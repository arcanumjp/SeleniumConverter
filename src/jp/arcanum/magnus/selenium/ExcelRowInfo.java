package jp.arcanum.magnus.selenium;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelRowInfo {

	private Map _record = new HashMap();
	
	private int _startrow = 65535;
	
	private int _endcol = 0;

	
	
	private HSSFWorkbook _workbook;
	public void setExcelWorkbook(HSSFWorkbook workbook){
		_workbook = workbook;
		
	}
	public HSSFWorkbook getExcelWorkbook(){
		return _workbook;
	}
	
	
	private String _indexstr;
	public void setIndexStr(final String indexstr){
		_indexstr = indexstr;
	}
	
	private List _colstrlist;
	public void setColStrList(final List colstrlist){
		_colstrlist = colstrlist;
	}
	public List getColStrList(){
		return _colstrlist;
	}
	
	public ExcelRowInfo(){
		
	}
	
	public void addExcelColInfo(final ExcelColInfo colinfo){
		_record.put(colinfo.getExcelColName(), colinfo);
		if(colinfo.getExcelRowIndex() <= _startrow){
			_startrow = colinfo.getExcelRowIndex();
		}
		if(colinfo.getExcelColIndex() >= _endcol){
			_endcol = colinfo.getExcelColIndex();
		}
		
	}
	
	public int getExcelColCount(){
		return _record.keySet().size();
	}
	
	public final ExcelColInfo get(final String name){
		return (ExcelColInfo)_record.get(name);
	}
	
	public final String[] getNames(){
		return null; 
	}

	public final int getStartRow(){
		return _startrow;
	}
	
	public final int getEndCol(){
		return _endcol;
	}

	public final ExcelColInfo getColInfo(int col){
		
		ExcelColInfo ret = null;
		
		Iterator ite = _record.keySet().iterator();
		while(ite.hasNext()){
			
			ExcelColInfo colinfo = (ExcelColInfo)_record.get(ite.next());
			if(colinfo.getExcelColIndex() == col){
				ret = colinfo;
			}
			
		}
	
		return ret;
	}
	
}
