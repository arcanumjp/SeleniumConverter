package jp.arcanum.magnus.pages.home;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import jp.arcanum.magnus.AbstractPage;
import jp.arcanum.magnus.selenium.CsvRow;
import jp.arcanum.magnus.selenium.ExcelColInfo;
import jp.arcanum.magnus.selenium.ExcelRowInfo;
import jp.arcanum.magnus.selenium.SeleniumAction;
import jp.arcanum.magnus.selenium.actionimple.TestTitleAction;
import jp.arcanum.magnus.utl.AppProperties;
import jp.arcanum.magnus.utl.AppUtil;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagType;
import net.htmlparser.jericho.TagType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

public class HomePage extends AbstractPage{

	private FileUploadField _seleniumcode = new FileUploadField("seleniumcode");

	
	
	private CheckBox _checkfile = new CheckBox("checkfile",new Model(Boolean.FALSE)){
		public void onSelectionChanged() {
			System.out.println("***********************************************************");
		}
	};
	private FileUploadField _exceltemplate = new FileUploadField("exceltemplate");
	private FileUploadField _propertyfile = new FileUploadField("propertyfile");
	
	private TextArea _message = new TextArea("message", new Model());
	private StringBuffer _messages = new StringBuffer();
	private void addMessage(final String message){
		_messages.append(message + "\n");
	}
	
	
	private Button _convert = new Button("convert"){
		public void onSubmit() {
			onClickConvert();
		}
	};
	
	private CheckBox _csvcheck = new CheckBox("csvcheck",new Model(Boolean.FALSE));
	
	public HomePage(){
		
		setFormMultiPart(true);
		
		addForm(_seleniumcode);
		addForm(_checkfile);
		addForm(_exceltemplate);
		addForm(_propertyfile);
		addForm(_convert);
		addForm(_csvcheck);
		addForm(_message);
		
		// TODO
		//_csvcheck.setEnabled(false);
		
		//error("現在ＣＳＶ出力しか対応できていません。");
		
	}
	
	private void onClickConvert(){
		
		// 存在確認
		FileUpload selfile = _seleniumcode.getFileUpload();
		if(selfile == null){
			_seleniumcode.error("Selenium Codeを指定してください。");
			return;
		}
		
		//　チェックの場合
		if((Boolean)_checkfile.getModelObject() == true){
			
			if(_exceltemplate.getFileUpload() == null){
				_exceltemplate.error("エクセルテンプレートファイルを指定してください。");
				return;
				
			}
			
			try {
				POIFSFileSystem fs = new POIFSFileSystem(_exceltemplate.getFileUpload().getInputStream());
			} 
			catch (IOException e) {
				_exceltemplate.error("指定されたExcelテンプレートファイルはExcelファイルではない可能性があります。");
				return;
			}
			
			
			if(_propertyfile.getFileUpload() == null){
				_exceltemplate.error("プロパティファイルを指定してください。");
				return;
				
			}
			
		}
		
		//-------------------------------------------
		// プロパティファイルの処理
		//-------------------------------------------
		
		
		FileUpload propfile = _propertyfile.getFileUpload();
		AppProperties prop = null;
		if(propfile == null){
		
			try {
				InputStream is = new FileInputStream(AppUtil.getAppPath() + AppUtil.DEFAULT_PROP_FILE);
				prop = new AppProperties(is);
			} 
			catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		else{
			try {
				prop = new AppProperties(propfile.getInputStream());
			} 
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		//-------------------------------------------
		//　Excelテンプレートの処理
		//-------------------------------------------
		
		ExcelRowInfo rowinfo = getExcelInfo(prop);
		
		
		//-------------------------------------------
		// SeleniumCodeの処理
		//-------------------------------------------


		// TRタグから
		List list = getTrList(selfile);
		List actionlist = new ArrayList();
		int testindex = 1;
		for(int i = 0 ; i < list.size(); i++){
			
			Element elem = (Element)list.get(i);
			
			List children =	elem.getChildElements();
			List columns = new ArrayList();
			for(int j = 0 ; j < children.size(); j++){
				Element child = (Element)children.get(j);
				if(child.getStartTag().getName().equalsIgnoreCase("td")){
					
					columns.add(child.getContent());
					
				}
					
			}
			if(children.size() >=3 ){

				SeleniumAction action = SeleniumAction.getAction(
						prop,
						((Segment)columns.get(0)).toString(),
						((Segment)columns.get(1)).toString(),
						((Segment)columns.get(2)).toString()
				);
				action.setTestIndex(testindex);
				testindex++;
				actionlist.add(action);
				
			}
			else{
				
				SeleniumAction action;
				if(columns.isEmpty()){
					action = new TestTitleAction(
							prop,
							null,
							null,
							null
					);
					
				}
				else{
					action = new TestTitleAction(
							prop,
							((Segment)columns.get(0)).toString(),
							null,
							null
					);
				}
				actionlist.add(action);
			}
			
		}
		
		//-------------------------------------------
		// 出力処理
		//-------------------------------------------
		
		//　CSVチェックの状態により処理を分ける
		boolean csvcheck = (Boolean)_csvcheck.getModelObject();
		
		//　チェックされている場合
		if(csvcheck){
			
			//------------------------------------------
			//　ｃｓｖで出力
			//------------------------------------------
			outputCSV(actionlist, rowinfo);
			
			
		}
		else{
			
			//------------------------------------------
			//　Excelで出力
			//------------------------------------------
			
			outputExcel(actionlist, rowinfo);
			
			
//			try {
//
//				HSSFWorkbook workbook = rowinfo.getExcelWorkbook();
//				HSSFSheet sheet = workbook.getSheetAt((short)0);
//				
//				for(int i = 0 ; i < actionlist.size(); i++){
//					
//					SeleniumAction action = (SeleniumAction)actionlist.get(i);
//					HSSFRow newrow = sheet.createRow(i);
//					HSSFCell cell = newrow.createCell((short)0);
//					HSSFRichTextString str = new HSSFRichTextString("hoge" + i);
//					cell.setCellValue(str);
//					
//					
//				}
//				ByteArrayInputStream bis = new ByteArrayInputStream(workbook.getBytes());
//				download(_seleniumcode.getFileUpload().getClientFileName() + ".xls", bis);
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new RuntimeException();
//			}
			
			
			
		}
		
			
		
		
	}
	
	private void outputExcel(List actionlist, ExcelRowInfo rowinfo){
		
		final HSSFWorkbook book = rowinfo.getExcelWorkbook();
		try{
			
			HSSFSheet sheet = book.getSheetAt((short)0);
			sheet.shiftRows(rowinfo.getStartRow() + 1, sheet.getLastRowNum(), actionlist.size() - 1);
			HSSFRow templaterow = sheet.getRow(rowinfo.getStartRow());
			
			for(int i = 0 ; i < actionlist.size(); i++){
				
				SeleniumAction action = (SeleniumAction)actionlist.get(i);

				sheet.createRow(rowinfo.getStartRow() + i);
				HSSFRow row = sheet.getRow(rowinfo.getStartRow() + i);
				
				
				for (int j=0; j<templaterow.getLastCellNum(); j++) {
					HSSFCell titleCell = templaterow.getCell((short)j);
					HSSFCell cell = row.createCell((short)j);
					if (titleCell != null) {
						// 値を取得
						switch (titleCell.getCellType()) {
							case HSSFCell.CELL_TYPE_BLANK :
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN :
								cell.setCellValue(titleCell.getBooleanCellValue());
								break;
							case HSSFCell.CELL_TYPE_ERROR :
								cell.setCellValue(titleCell.getErrorCellValue());
								break;
							case HSSFCell.CELL_TYPE_FORMULA :
								cell.setCellFormula(titleCell.getCellFormula());
								break;
							case HSSFCell.CELL_TYPE_NUMERIC :
								cell.setCellValue(titleCell.getNumericCellValue());
								break;
							case HSSFCell.CELL_TYPE_STRING :
								
								String cellvalue = action.getString(rowinfo, i, j);
								if(cellvalue != null){
									cell.setCellValue(new HSSFRichTextString(cellvalue));
								}
								else{
									cell.setCellValue(titleCell.getRichStringCellValue());
								}
								
								break;
							default :
						}
						
						// スタイルを取得
						cell.setCellStyle(titleCell.getCellStyle());
					}
				
				}
				
			}
		
			sheet.shiftRows(rowinfo.getStartRow() + 1, sheet.getLastRowNum(), -1);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		
		
		
		
		
		
		
		
		
		
		// 出力オブジェクトを生成する。 ** 以下無名インナークラスが長いので注意 **
		AbstractResourceStreamWriter writer = new AbstractResourceStreamWriter() {

			public String getContentType() {
				return "application/octet-stream;charset=MS932";
			}
			public void write(OutputStream output) {
				try {
					book.write(output);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		};

		//　ダウンロード処理を行う
		ResourceStreamRequestTarget target = 
			new ResourceStreamRequestTarget(writer,"hogehoge.xls");
		this.getRequestCycle().setRequestTarget(target);
		
		
	}

	private void outputCSV(List actionlist, ExcelRowInfo rowinfo){
		
		
		// Action分処理を行って、パースする
		StringBuffer csvstr = new StringBuffer();
		for(int i = 0 ; i < actionlist.size(); i++){
			SeleniumAction action = (SeleniumAction)actionlist.get(i);

			CsvRow csvrow = null;
			if(action instanceof TestTitleAction){
				
				//　タイトルの前の１行
				csvstr.append(""+ "\r\n");
				
				csvrow = new CsvRow(1);
				String celstr = action.getString(rowinfo,(i+1), 0);
				csvrow.addField(0, celstr);
				csvstr.append(csvrow.toString() + "\r\n");
				
			}
			else{
				
				csvrow = new CsvRow(rowinfo.getEndCol());
				for(int j = 0 ; j < rowinfo.getEndCol(); j++){
					
					String celstr = action.getString(rowinfo,(i+1), j);
					csvrow.addField(j, celstr);
					
				}
				csvstr.append(csvrow.toString() + "\r\n");

			}
		}
		
		// ダウンロード処理
		ByteArrayInputStream bis = new ByteArrayInputStream(csvstr.toString().getBytes());
		download(_seleniumcode.getFileUpload().getClientFileName() + ".csv", bis);
		

	}
	
	private void download(final String defaultfilename, final InputStream is){

		
		// 出力オブジェクトを生成する。 ** 以下無名インナークラスが長いので注意 **
		AbstractResourceStreamWriter writer = new AbstractResourceStreamWriter() {

			public String getContentType() {
				return "application/octet-stream;charset=MS932";
			}
			public void write(OutputStream output) {
				
		        //File file = new File(Util.EXCEL_PATH);        
		        BufferedInputStream in = null;        
		        try {            
		        	//in = new BufferedInputStream(new FileInputStream(file));            
		        	in = new BufferedInputStream(is);
		        	byte buf[]=new byte[1024];            
		        	int len;            
		        	while((len=in.read(buf))!=-1){                
		        		output.write(buf,0,len);            
		        	}        
		        	
		        } catch (SocketException e) {            
		        	//●ダウンロード処理中にダウンロードダイアログの「キャンセル」が            
		        	//クリックされた場合の例外。            
		        	//●ただし、ダウンロードダイアログが表示されているバックグラウンドで            
		        	//ブラウザへのダウンロードが行われていることに留意すること。            
		        	//●つまり小さいファイルでは、ダイアログが表示される時には、ダウンロード            
		        	//処理は完了し、サーブレットは終了してしまっており、SocketExceptionの            
		        	//も発生しないということです。        
		        } 
		        catch (Exception e) {
		        	e.printStackTrace();
		        } 
		        finally {            
		        	try {
						if (in != null) {                
							in.close();            
						}            
						if (output != null) {                
							output.flush();                
							output.close();            
						}
					} catch (IOException e) {
						//　もう何もできないす
					}        
		        }								
			}
		};

		//　ダウンロード処理を行う
		ResourceStreamRequestTarget target = 
			new ResourceStreamRequestTarget(writer,defaultfilename);
		this.getRequestCycle().setRequestTarget(target);
		
		
	}
	
	
	
	private ExcelRowInfo getExcelInfo(AppProperties prop){
		
		ExcelRowInfo ret = new ExcelRowInfo();

		//-------------------------------------------
		//　エクセルファイルの確定
		//-------------------------------------------
		
		FileUpload excelfile = _exceltemplate.getFileUpload();
		InputStream isexcel;
		if(excelfile == null){
			try {
				isexcel = new FileInputStream(AppUtil.getAppPath() + AppUtil.DEFAULT_TEMPLATE);
			}
			catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		else{
			try {
				isexcel = excelfile.getInputStream();
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		//-------------------------------------------
		// Excel Col情報の生成
		//-------------------------------------------
		
		String indexcolstr = prop.getPropertyString("excel.settings.index");
		if(indexcolstr == null){
			_propertyfile.error("プロパティファイルが間違っている可能性。デフォルトを適用します。 excel.settings.index");
			indexcolstr = "index";
		}
		
		List keylist = prop.getPropertyKeys("excel.settings.col");
		
		
		//　ＰＯＩで読み込み
		try {
			POIFSFileSystem fs = new POIFSFileSystem(isexcel);

			HSSFWorkbook workbook = new HSSFWorkbook(fs);
			int sheetscnt = workbook.getNumberOfSheets();
			if(sheetscnt != 1){
				_exceltemplate.error("Ｅｘｃｅｌテンプレートファイルのシート数は１つにしてください。");
				return ret;
			}
			
			HSSFSheet sheet = workbook.getSheetAt(0);
			ret.setExcelWorkbook(workbook);

			for(int i = sheet.getFirstRowNum() ; i < sheet.getLastRowNum(); i++ ){
				
				HSSFRow row = sheet.getRow(i);
				if(row == null)continue;
			
				for(short j = row.getFirstCellNum() ; j < row.getLastCellNum(); j++){
					
					HSSFCell cell = row.getCell(j);
					if(cell == null)continue;
					
					String value = cell.getRichStringCellValue().getString();
					
					//addMessage("ROW [" + i + "] COL [" + j + "] : " + value  );
					
					if(value.contains("${" + indexcolstr + "}")){
						ExcelColInfo colinfo = new ExcelColInfo(i,j,value);
						ret.addExcelColInfo(colinfo);
					}
					for(int k = 0 ; k < keylist.size(); k++){
						String key = (String)keylist.get(k);
						String propstr = prop.getPropertyString(key);
						if(value.contains("${" + propstr + "}")){
							ExcelColInfo colinfo = new ExcelColInfo(i,j,value);
							ret.addExcelColInfo(colinfo);
						}
					}
					
				}
				
			}
			
		
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		addMessage("--------------------------------------------------");
		addMessage("テンプレートExcelをパースしました。");
		addMessage("テンプレートExcelの開始行を　[" + ret.getStartRow() + "]に設定しました");
		
		ret.setColStrList(keylist);
		return ret;
		
	}
	
	

	
	private List getTrList(FileUpload selfile){
		
		List ret = new ArrayList();
		
		try{
			
			// ファイルを解析
			Source src = new Source(selfile.getInputStream());

			// <table>のリストから、<tr>のリストを取得
			List list = src.getAllElements();
			List tablelist = new ArrayList();

			for(int i = 0 ; i < list.size(); i++){
				Element elem = (Element)list.get(i);
				
				if(elem.getStartTag().getName().equalsIgnoreCase("tr")){
					ret.add(elem);
				}
			}
		
		
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
		
		return ret;

	}


	
	
	protected void onBeforeRender() {
		super.onBeforeRender();
		_message.setModel(new Model(_messages.toString()));
		_messages = new StringBuffer();
		
	}
	
	
	private void foo(AppProperties prop){
		// エクセル上での書式の取得
		String indexfomula = prop.getPropertyString("selenium.settings.index");
		if(indexfomula == null){
			_propertyfile.error("プロパティファイルの設定が間違っている可能性があります。デフォルトの値を使用します。　selenium.settings.index");
			indexfomula = "index";
		}
		String actionfomula = prop.getPropertyString("selenium.settings.action");
		if(actionfomula == null){
			_propertyfile.error("プロパティファイルの設定が間違っている可能性があります。デフォルトの値を使用します。　selenium.settings.action");
			actionfomula = "action";
		}
		String targetfomula = prop.getPropertyString("selenium.settings.target");
		if(targetfomula == null){
			_propertyfile.error("プロパティファイルの設定が間違っている可能性があります。デフォルトの値を使用します。　selenium.settings.target");
			targetfomula = "target";
		}
		String valuefomula = prop.getPropertyString("selenium.settings.value");
		if(valuefomula == null){
			_propertyfile.error("プロパティファイルの設定が間違っている可能性があります。デフォルトの値を使用します。　selenium.settings.value");
			valuefomula = "value";
		}


	}
	
}
