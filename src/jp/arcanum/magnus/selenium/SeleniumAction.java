package jp.arcanum.magnus.selenium;

import jp.arcanum.magnus.selenium.actionimple.DefaultAction;
import jp.arcanum.magnus.utl.AppProperties;
import sun.nio.ch.DefaultSelectorProvider;

public abstract class SeleniumAction {

	protected AppProperties _prop;
	protected String _action;
	protected String _target;
	protected String _value;
	
	private String _targetlocatertype;
	private String _valuelocatertype;
	
	protected int _testindex;
	public void setTestIndex(int index){
		_testindex = index;
	}
	public int getTestIndex(){
		return _testindex;
	}
	
	
	/**
	 * コンストラクタ
	 * @param action
	 * @param target
	 * @param value
	 */
	public SeleniumAction(
			final AppProperties prop,
			final String action, 
			final String target, 
			final String value){
		
		_prop = prop;
		_action = action;
		
		if(target == null){
			// do nothing
		}
		else{
			
			_target = target.trim();
			String[] left = {"identifier", "id", "name", "dom", "xpath", "link"};
			for(int i = 0 ; i < left.length; i++){
				if(_target.startsWith(left[i])){
					
					_targetlocatertype = left[i];
					_target = target.replace(left[i], "").trim();
					_target = _target.replaceFirst("=", "").trim();
				}
				
			}
			
			//　変換できなかった場合
			if(_targetlocatertype == null){
				
				//ElementLocatorのprefix(locatorType)が無い場合、Seleniumは以下のものと解釈して動作します。
				//"document."から始まっている場合  dom=と解釈して動作  
				//"//"から始まっている場合  xpath=と解釈して動作  
				//それ以外  identifier=と解釈して動作  
				if(_target.startsWith("document.")){
					_targetlocatertype = "dom";
				}
				else if(_target.startsWith("//")){
					_targetlocatertype = "xpath";
				}
				else{
					_targetlocatertype = "identifier";
				}
				
			}
			
		}
		_value = value;
	
		
		
	}
	
	
	public String getString(){
		
		String ret = null;
		
		
		return ret;
		
	}
	
	public static final SeleniumAction getAction(
			final AppProperties prop,
			final String action, 
			final String target, 
			final String value){
		
		SeleniumAction ret = null;
		
		if(action.equalsIgnoreCase("Store")){
			// new Store Imple
		}
		
		if(action.equals("click")){
			// new Click Action
		}
		if(action.equals("clickAndWait")){
			// new ClickAndWait Action
		}
		
		
		// ... 
		
		if(ret == null){
			ret = new DefaultAction(prop, action, target, value);
			
		}
		
		
		return ret;
		
	}

	protected String replaceActionTargetValue(final String str){
		
		String ret = str;
		
		try {
			ret = ret.replaceAll("\\$\\{action\\}" , _action);
			ret = ret.replaceAll("\\$\\{target\\}" , _target);
			ret = ret.replaceAll("\\$\\{value\\}" , _value);
			
			ret = ret.replaceAll("&nbsp;", "");
			
			ret = ret.replace("\\n", System.getProperty("line.separator") );
		} 
		catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println("'" + str + "' '" + _target + "'" );
			
			
			// locatorがxpathの場合は//から始まるため、正規表現で置き換えを行う際エラーになる。
			
			
		}

		return ret;
		
	}
	
	
	
	public String getString(ExcelRowInfo rowinfo, int row, int index){
		
		String ret = null;
		
		ExcelColInfo colinfo = rowinfo.getColInfo(index);
		if(colinfo == null){
			return ret;
		}
		
		
		String colkey = colinfo.getExcelColName();
		if(colkey.equals("${index}")){
			return String.valueOf(_testindex);
		}
		
		
		colkey = colkey.replaceAll("\\$\\{", "");
		colkey = colkey.replaceAll("\\}", "");
		
		return editString(rowinfo, colkey);
		
	}

	public abstract String editString(ExcelRowInfo rowinfo, String colkey);
	
}


