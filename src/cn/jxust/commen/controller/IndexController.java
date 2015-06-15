package cn.jxust.commen.controller;

import java.util.Date;

import cn.jxust.commen.model.User;
import cn.jxust.core.Constant;

import com.jfinal.ext.kit.DateKit;
import com.jfinal.ext.route.ControllerBind;
@ControllerBind(controllerKey = "/")
public class IndexController extends BaseController
{
	@Override
	public void index()
	{	
		User user=getSessionAttr("user");
		this.setAttr("user", user);
		this.setAttr("version", Constant.VERSION);
		this.setAttr("webName", Constant.WEB_NAME);
		this.setAttr("footer", Constant.COPYRIGHT);
		this.setAttr("now", DateKit.toStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		this.setAttr("ip", getRequest().getRemoteAddr());
		render("index.html");
	}
}
