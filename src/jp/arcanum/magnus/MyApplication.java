package jp.arcanum.magnus;

import javax.servlet.ServletContext;

import jp.arcanum.magnus.pages.home.HomePage;
import jp.arcanum.magnus.utl.AppProperties;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;

public class MyApplication extends WebApplication{

	@Override
	public Class getHomePage() {
		return HomePage.class;
	}

    protected void init() {
    	
    	//　保守者へ、以下のinit()は消さないでください。
    	super.init();
    	
    	//　文字エンコーディング関係の設定
    	getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        
        
        //　ページの有効期限がきれた場合の設定
        Application.get().getApplicationSettings().setPageExpiredErrorPage(HomePage.class);

    }

    
    
    
//	public String getConfigurationType() {
//		// TODO 自動生成されたメソッド・スタブ
//		return DEPLOYMENT;
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

    
	
	
}
