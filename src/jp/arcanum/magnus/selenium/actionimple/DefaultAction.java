package jp.arcanum.magnus.selenium.actionimple;

import java.util.List;

import jp.arcanum.magnus.selenium.ExcelColInfo;
import jp.arcanum.magnus.selenium.ExcelRowInfo;
import jp.arcanum.magnus.selenium.SeleniumAction;
import jp.arcanum.magnus.utl.AppProperties;

public class DefaultAction extends SeleniumAction{
	
	public DefaultAction(
			final AppProperties prop,
			final String action, 
			final String target, 
			final String value){
		super(prop, action, target, value);
	
	}

	@Override
	public String editString(ExcelRowInfo rowinfo, String colkey) {
		
		String ret = null;
		
		ret = _prop.getPropertyString("selenium.action." + _action + "." + colkey);
		if(ret != null){
			ret = replaceActionTargetValue(ret);
			return ret;
		}
		
		List list = rowinfo.getColStrList();
		for(int i = 0 ; i < list.size(); i++){
			
			ret = _prop.getPropertyString("selenium.action.default." + colkey);
			if(ret != null){
				ret = replaceActionTargetValue(ret);
				break;
			}
			
		}
		
		return ret;
		
	}
	

	
	
	
}	
