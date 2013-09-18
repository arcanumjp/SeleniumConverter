package jp.arcanum.magnus.pages.contact;

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

public class Contact extends AbstractPage{
	
	
	public Contact(){
		
		
	}
	
}
