package cn.jxust.paper.controller;

import cn.jxust.commen.controller.BaseController;
import cn.jxust.commen.model.User;
import cn.jxust.core.Constant;
import cn.jxust.paper.model.Problem;
import cn.jxust.paper.model.Problem_judge;
import cn.jxust.paper.model.Problem_select;
import cn.jxust.paper.service.ProblemService;
import cn.jxust.render.DwzRender;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

@ControllerBind(controllerKey = "/problem")
public class ProblemController extends BaseController
{
	ProblemService problemService=new ProblemService();
	
	@Override
	public void index()
	{
		Page<Problem> page=problemService.findProblemPage(getParaToInt("pageNum", 1), getParaToInt("numPerPage", Constant.pageSize));
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
			Problem problem= problemService.findById(id);
			this.setAttr("problem", problem);
			if(problem.get("type").equals("select"))
			{
				Problem_select problem_select=problemService.findProblem_selectByPro_id(problem.get("id").toString());
				this.setAttr("problem_select", problem_select);
			}
			if(problem.get("type").equals("judge"))
			{
				Problem_judge problem_judge=problemService.findProblem_judgeByPro_id(problem.get("id").toString());
				this.setAttr("problem_judge", problem_judge);
			}
			this.setAttr("problem", problem);
		}
		render("edit.html");
	}
	
	/**
	 * 数据更新
	 */
	@Before(Tx.class)//为此action添加事务支持
	public void update()
	{
		Problem problem=getModel(Problem.class);
		User user=this.getSessionAttr("user");
		if(problem.get("type").equals("select"))
		{
			Problem_select problem_select=getModel(Problem_select.class);
			if(null==problem_select.get("title"))
			{
				render(DwzRender.error("您选择的题型是选择题，<b>题目名</b>不能为空！"));
				return;
			}
			if(null==problem_select.get("main_body"))
			{
				render(DwzRender.error("您选择的题型是选择题，<b>题目正文</b>不能为空！"));
				return;
			}
			if(null==problem_select.get("answer"))
			{
				render(DwzRender.error("您选择的题型是选择题，<b>答案</b>不能为空！"));
				return;
			}
			if(null==problem_select.get("explan"))
			{
				render(DwzRender.error("您选择的题型是选择题，<b>解析</b>不能为空！"));
				return;
			}
			problemService.update_problem_select(problem_select,problem.get("id").toString());
		}
		if(problem.get("type").equals("judge"))
		{
			Problem_judge problem_judge=getModel(Problem_judge.class);
			if(null==problem_judge.get("title"))
			{
				render(DwzRender.error("您选择的题型是判断题，<b>题目名</b>不能为空！"));
				return;
			}
			if(null==problem_judge.get("main_body"))
			{
				render(DwzRender.error("您选择的题型是判断题，<b>题目正文</b>不能为空！"));
				return;
			}
			if(null==problem_judge.get("answer"))
			{
				render(DwzRender.error("您选择的题型是判断题，<b>答案</b>不能为空！"));
				return;
			}
			if(null==problem_judge.get("explan"))
			{
				render(DwzRender.error("您选择的题型是判断题，<b>解析</b>不能为空！"));
				return;
			}
			problemService.update_problem_judge(problem_judge,problem.get("id").toString());
		}
		problemService.update(problem,user);
		render(DwzRender.closeCurrentAndRefresh("problemlist"));
	}
	
	/**
	 * 激活单个
	 */
	public void activation()
	{
		String id = getPara("id");
		problemService.activation(id);
		render(DwzRender.refresh("managementpaper"));
	}
	
	/**
	 * 批量激活
	 */
	public void activations()
	{
		String[] items=getParaValues("items");
		for(String item:items)
		{
			problemService.activation(item);
		}
		render(DwzRender.refresh("managementpaper"));
	}
	
	/**
	 * 冻结单个
	 */
	public void freeze()
	{
		String id = getPara("id");
		problemService.freeze(id);
		render(DwzRender.refresh("problemlist"));
	}
	
	/**
	 * 批量冻结
	 */
	public void freezes()
	{
		String[] items=getParaValues("items");
		for(String item:items)
		{
			problemService.freeze(item);
		}
		render(DwzRender.refresh("problemlist"));
	}

	public void viewProblemDetail()
	{
		String pro_id=getPara("id");
		Problem problem=problemService.findById(pro_id);
		if(problem.get("type").equals("select"))
		{
			Problem_select problem_select=problemService.findProblem_selectByPro_id(pro_id);
			this.setAttr("problem_select", problem_select);
			render("problem_select_detail.html");
			return;
		}
		if(problem.get("type").equals("judge"))
		{
			Problem_judge problem_judge=problemService.findProblem_judgeByPro_id(pro_id);
			this.setAttr("problem_judge", problem_judge);
			render("problem_judge_detail.html");
			return;
		}
	}
}
