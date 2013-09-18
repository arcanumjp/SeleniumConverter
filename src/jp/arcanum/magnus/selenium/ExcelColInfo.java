package jp.arcanum.magnus.selenium;

public class ExcelColInfo {

	private int _colindex;
	private int _rowindex;
	
	private String _colname;
	
	public ExcelColInfo(int rowindex, int colindex, String colname){
		_rowindex = rowindex;
		_colindex = colindex;
		_colname = colname;
	}
	
	public final int getExcelRowIndex(){
		return _rowindex;
	}
	public final int getExcelColIndex(){
		return _colindex;
	}
	
	public final String getExcelColName(){
		return  _colname;
	}
	
}
