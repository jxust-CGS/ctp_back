package cn.jxust.paper.controller;

import java.util.List;

import cn.jxust.commen.controller.BaseController;
import cn.jxust.commen.model.User;
import cn.jxust.core.Constant;
import cn.jxust.paper.model.Paper;
import cn.jxust.paper.model.Paper_problem;
import cn.jxust.paper.model.Problem;
import cn.jxust.paper.model.Problem_judge;
import cn.jxust.paper.model.Problem_select;
import cn.jxust.paper.service.PaperService;
import cn.jxust.paper.service.ProblemService;
import cn.jxust.render.DwzRender;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

@ControllerBind(controllerKey = "/paper")
public class PaperController extends BaseController
{
	PaperService paperService=new PaperService();
	ProblemService problemService=new ProblemService();
	
	@Override
	public void index()
	{
		Page<Paper> page=paperService.findPaperPage(getParaToInt("pageNum", 1), getParaToInt("numPerPage", Constant.pageSize));
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
			Paper paper=paperService.getPaperById(id);
			setAttr("paper", paper);
		}
		render("edit.html");
	}
	
	/**
	 * 数据更新
	 */
	public void update()
	{
		Paper paper=getModel(Paper.class);
		User user=getSessionAttr("user");
		paperService.update(paper,user);
		render(DwzRender.closeCurrentAndRefresh("paperlist"));
	}
	
	/**
	 * 删除单个
	 */
	@Before(Tx.class)//为此action添加事务支持
	public void delete()
	{
		String id = getPara("id");
		paperService.deletePaperById(id);
		render(DwzRender.refresh("paperlist"));
	}
	
	/**
	 * 批量删除
	 */
	public void deletes()
	{
		String[] items = getParaValues("items");
		for(String item: items)
		{
			paperService.deletePaperById(item);
		}
		render(DwzRender.refresh("paperlist"));
	}
	
	/**
	 * 试卷信息管理界面
	 */
	public void management()
	{
		String paper_id = getPara("id");
		Paper paper=paperService.getPaperById(paper_id);
		setAttr("paper", paper); 
		List<Paper_problem> paper_problems=paperService.findPaper_problemByPaper_id(paper_id);
		setAttr("paper_problems", paper_problems);
		render("management.html");
	}

	public void modifyPaper_problem()
	{
		String paper_id=getPara("paper_id");
		Paper paper=paperService.getPaperById(paper_id);
		setAttr("paper", paper); 
		String paper_problem_id=getPara("id");
		if(null!=paper_problem_id)
		{
			Paper_problem paper_problem=paperService.getPaper_problemByPaper_problem_id(paper_problem_id);
			setAttr("paper_problem", paper_problem);
			Problem problem=problemService.findById(paper_problem.get("problem_id").toString());
			setAttr("problem", problem);
			if(problem.get("type").equals("select"))
			{
				Problem_select problem_select=problemService.findProblem_selectByPro_id(problem.get("id").toString());
				setAttr("problem_select", problem_select);
			}
			else
			{
				Problem_judge problem_judge=problemService.findProblem_judgeByPro_id(problem.get("id").toString());
				setAttr("problem_judge", problem_judge);
			}
		}
		render("problem_edit.html");
	}
	
	@Before(Tx.class)
	public void update_problem()
	{
		User user = getSessionAttr("user");
		String paper_name=getPara("paper.name");
		Paper_problem paper_problem = getModel(Paper_problem.class);
		Problem problem = getModel(Problem.class);
		Problem_select problem_select = getModel(Problem_select.class);
		Problem_judge problem_judge = getModel(Problem_judge.class);
		problemService.update2(problem, user,paper_name);
		if(problem.get("type").equals("select"))
		{
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
		paper_problem.set("problem_id",problem.get("id").toString());
		paperService.save_paper_problem(paper_problem);
		render(DwzRender.closeCurrentAndRefresh("managementpaper"));
	}
	
	@Before(Tx.class)
	public void delete_problem()
	{
		String paper_problem_id= getPara("id");
		paperService.deletePaper_problemByPaper_problem_id(paper_problem_id);
		render(DwzRender.refresh("managementpaper"));
	}
	
	@Before(Tx.class)
	public void delete_problems()
	{
		String[] items= getParaValues("items");
		for(String item:items)
		{
			paperService.deletePaper_problemByPaper_problem_id(item);
		}
		render(DwzRender.refresh("managementpaper"));
	}
}
