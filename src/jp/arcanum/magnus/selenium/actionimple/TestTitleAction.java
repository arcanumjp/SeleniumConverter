package jp.arcanum.magnus.selenium.actionimple;

import java.util.List;

import jp.arcanum.magnus.selenium.ExcelColInfo;
import jp.arcanum.magnus.selenium.ExcelRowInfo;
import jp.arcanum.magnus.selenium.SeleniumAction;
import jp.arcanum.magnus.utl.AppProperties;

public class TestTitleAction extends SeleniumAction{
	
	public TestTitleAction(
			final AppProperties prop,
			final String action, 
			final String target, 
			final String value){
		super(prop, action, target, value);
	
	}

	@Override
	public String editString(ExcelRowInfo rowinfo, String colkey) {
		
		String ret = null;
		

		
		return ret;
		
	}
	

	
	public String getString(ExcelRowInfo rowinfo, int row, int index){
		
		String ret = null;
		
		if(index == 0){
			return _action;
		}
		
		return ret;
	}

	
}	
