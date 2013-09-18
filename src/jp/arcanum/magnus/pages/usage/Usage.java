package jp.arcanum.magnus.pages.usage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jp.arcanum.magnus.AbstractPage;
import jp.arcanum.magnus.selenium.CsvRow;
import jp.arcanum.magnus.selenium.ExcelColInfo;
import jp.arcanum.magnus.selenium.ExcelRowInfo;
import jp.arcanum.magnus.selenium.SeleniumAction;
import jp.arcanum.magnus.utl.AppProperties;
import jp.arcanum.magnus.utl.AppUtil;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.StartTagType;
import net.htmlparser.jericho.TagType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;

public class Usage extends AbstractPage{
	
	private TextArea _defaultprop = new TextArea("defaultprop");
	
	
	public Usage(){
		
		addForm(_defaultprop);
		

		StringBuffer defprop = new StringBuffer();
		FileInputStream fin = null;
		InputStreamReader ir = null;
		BufferedReader br = null;
		try {
			fin = new FileInputStream(AppUtil.getAppPath() + AppUtil.DEFAULT_PROP_FILE);
			ir = new InputStreamReader(fin,"UTF-8");
			br = new BufferedReader(ir);
			String line;
			while ((line = br.readLine()) != null) {
			    defprop.append(line + "\n");
			}
			_defaultprop.setModel(new Model(new String( defprop.toString())));

		} 
		catch (Exception e) {
			_defaultprop.setModel(new Model("デフォルトプロパティファイルが読み込めませんでした"));
			if(br != null){
				try {
					br.close();
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
			if(ir != null){
				try {
					ir.close();
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
			if(fin != null){
				try {
					fin.close();
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
			}
		}
		
		
		
	}
	
}
