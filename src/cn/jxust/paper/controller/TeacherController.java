package cn.jxust.paper.controller;

import cn.jxust.commen.controller.BaseController;
import cn.jxust.commen.model.User;
import cn.jxust.core.Constant;
import cn.jxust.paper.service.UserService;
import cn.jxust.render.DwzRender;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/teacher")
public class TeacherController extends BaseController
{
	UserService userService=new UserService();

	@Override 
	public void index()
	{
		Page<User> page=userService.findTeacherPage(getParaToInt("pageNum", 1), getParaToInt("numPerPage", Constant.pageSize));
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
			User teacher= userService.findById(id);
			this.setAttr("teacher", teacher);
		}
		render("edit.html");
	}
	
	/**
	 * 数据更新
	 */
	public void update()
	{
		User teacher=getModel(User.class,"teacher");
		userService.update(teacher);
		render(DwzRender.closeCurrentAndRefresh("teacherlist"));
	}
	
	/**
	 * 删除单个
	 */
	public void delete()
	{
		String id = getPara("id");
		userService.delete(id);
		render(DwzRender.refresh("teacherlist"));
	}
	
	/**
	 * 批量删除
	 */
	public void deletes()
	{
		String[] items=getParaValues("items");
		for(String item:items)
		{
			userService.delete(item);
		}
		render(DwzRender.refresh("teacherlist"));
	}
	
	/**
	 * 激活单个
	 */
	public void activation()
	{
		String id = getPara("id");
		userService.activation(id);
		render(DwzRender.refresh("teacherlist"));
	}
	
	/**
	 * 批量激活
	 */
	public void activations()
	{
		String[] items=getParaValues("items");
		for(String item:items)
		{
			userService.activation(item);
		}
		render(DwzRender.refresh("teacherlist"));
	}
	
	/**
	 * 冻结单个
	 */
	public void freeze()
	{
		String id = getPara("id");
		userService.freeze(id);
		render(DwzRender.refresh("teacherlist"));
	}
	
	/**
	 * 批量冻结
	 */
	public void freezes()
	{
		String[] items=getParaValues("items");
		for(String item:items)
		{
			userService.freeze(item);
		}
		render(DwzRender.refresh("teacherlist"));
	}
}
 