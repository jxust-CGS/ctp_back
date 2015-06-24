package cn.jxust.commen.controller;

import cn.jxust.commen.model.News;
import cn.jxust.commen.model.User;
import cn.jxust.commen.service.NewsService;
import cn.jxust.core.Constant;
import cn.jxust.render.DwzRender;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
@ControllerBind(controllerKey = "/news")
public class NewsController extends BaseController
{
	NewsService newsService=new NewsService();

	@Override 
	public void index()
	{
		Page<News> page=newsService.findNewsPage(getParaToInt("pageNum", 1), getParaToInt("numPerPage", Constant.pageSize));
		this.setAttr("page", page);
		render("list.html");
	}
	
	/**
	 * 打开编辑页面（修改或添加）
	 */
	public void modify()
	{
		String id=getPara("id");
		if(null!=id)
		{
			News news= newsService.findById(id);
			this.setAttr("news", news);
		}
		render("edit.html");
	}
	
	/**
	 * 数据更新
	 */
	public void update()
	{
		User user= getSessionAttr("user");
		News news=getModel(News.class,"news");
		newsService.update(news,user);
		render(DwzRender.closeCurrentAndRefresh("newslist"));
	}
	
	/**
	 * 删除单个
	 */
	public void delete()
	{
		String id = getPara("id");
		newsService.delete(id);
		render(DwzRender.refresh("newslist"));
	}
	
	/**
	 * 批量删除
	 */
	public void deletes()
	{
		String[] items=getParaValues("items");
		for(String item:items)
		{
			newsService.delete(item);
		}
		render(DwzRender.refresh("newslist"));
	}
	
	/**
	 * 激活单个
	 */
	public void activation()
	{
		String id = getPara("id");
		newsService.activation(id);
		render(DwzRender.refresh("newslist"));
	}
	
	/**
	 * 批量激活
	 */
	public void activations()
	{
		String[] items=getParaValues("items");
		for(String item:items)
		{
			newsService.activation(item);
		}
		render(DwzRender.refresh("newslist"));
	}
	
	/**
	 * 冻结单个
	 */
	public void freeze()
	{
		String id = getPara("id");
		newsService.freeze(id);
		render(DwzRender.refresh("newslist"));
	}
	
	/**
	 * 批量冻结
	 */
	public void freezes()
	{
		String[] items=getParaValues("items");
		for(String item:items)
		{
			newsService.freeze(item);
		}
		render(DwzRender.refresh("newslist"));
	}
}
