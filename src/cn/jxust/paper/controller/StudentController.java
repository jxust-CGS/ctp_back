package cn.jxust.paper.controller;

import cn.jxust.commen.controller.BaseController;
import cn.jxust.commen.model.User;
import cn.jxust.core.Constant;
import cn.jxust.paper.service.UserService;
import cn.jxust.render.DwzRender;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/student")
public class StudentController extends BaseController
{
	UserService userService=new UserService();

	@Override 
	public void index()
	{
		Page<User> page=userService.findStudentPage(getParaToInt("pageNum", 1), getParaToInt("numPerPage", Constant.pageSize));
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
			User student= userService.findById(id);
			this.setAttr("student", student);
		}
		render("edit.html");
	}
	
	/**
	 * 数据更新
	 */
	public void update()
	{
		User student=getModel(User.class,"student");
		userService.update(student);
		render(DwzRender.closeCurrentAndRefresh("studentlist"));
	}
	
	/**
	 * 删除单个
	 */
	public void delete()
	{
		String id = getPara("id");
		userService.delete(id);
		render(DwzRender.refresh("studentlist"));
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
		render(DwzRender.refresh("studentlist"));
	}
	
}
