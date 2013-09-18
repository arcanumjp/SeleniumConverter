package jp.arcanum.magnus.utl;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.WebRequest;


public class AppUtil {

	public static final String DEFAULT_PROP_FILE = "/WEB-INF/src/jp/arcanum/magnus/selenium/Default.properties";

	public static final String DEFAULT_TEMPLATE  = "/WEB-INF/src/jp/arcanum/magnus/selenium/Default.xls";


	public static final String getAppPath(){
		HttpServletRequest request =
			((WebRequest)RequestCycle.get().getRequest()).getHttpServletRequest();
		return request.getSession().getServletContext().getRealPath("");
	}



}
