package jp.arcanum.magnus;

import jp.arcanum.magnus.pages.contact.Contact;
import jp.arcanum.magnus.pages.home.WhatsThisSite;
import jp.arcanum.magnus.pages.usage.Usage;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;

public class AbstractPage extends WebPage{

	private Form _form = new Form("form"){
		{
			//Set maximum size to 100K for demo purposes
	        setMaxSize(Bytes.kilobytes(100));
			
		}
	};
	
	
	Button _defaultbtn = new Button("defaultbtn"){
		public void onSubmit() {
			System.out.println("***********************************************************************");
			System.out.println(" default button here !!");
			System.out.println("***********************************************************************");
		}
	};
	
	
	public AbstractPage(){
		
		//　メニューリンク
		//add(new PageLink("whatsthissite", WhatsThisSite.class));
		add(new PageLink("usage", Usage.class));
		add(new PageLink("contact", Contact.class));
		
		//　メッセージエリア
		add(new FeedbackPanel("feedback"));
		
		//　画面のデフォルトボタンを無効にする設定
		AttributeAppender defattr = new AttributeAppender("style", new Model("display:none;"),"");
		_defaultbtn.add(defattr);
		_form.add(_defaultbtn);
		_form.setDefaultButton(_defaultbtn);
		

		add(_form);
		
		
		
	}
	
	
	
	protected void onBeforeRender() {
		super.onBeforeRender();
		
		//if(getFeedbackMessage().getMessage(). ){
		//	
		//}
		
	}



	protected void setFormMultiPart(boolean b){
		_form.setMultiPart(b);
	}
	
	protected void addForm(Component component){
		_form.add(component);
	}
	
	
}
