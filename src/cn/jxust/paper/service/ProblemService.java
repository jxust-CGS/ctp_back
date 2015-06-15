package cn.jxust.paper.service;

import java.util.Date;

import cn.jxust.commen.model.User;
import cn.jxust.paper.model.Problem;
import cn.jxust.paper.model.Problem_judge;
import cn.jxust.paper.model.Problem_select;

import com.jfinal.plugin.activerecord.Page;

public class ProblemService
{

	/**
	 * 查询试题页面
	 */
	public Page<Problem> findProblemPage(Integer pageNumber, Integer pageSize)
	{
		return Problem.dao.paginate(pageNumber, pageSize, "select problem.*,user.fullname", "from problem,user where problem.creater=user.id and problem.sign='1' order by id asc");
	}

	public Problem findById(String id)
	{
		return Problem.dao.findById(id);
	}

	public void save(Problem problem)
	{
		if(null==problem.get("id"))
		{
			problem.save();
		}
		else
		{
			problem.update();
		}
	}
	/**
	 * 题库管理中添加试题，默认加入题库
	 * @param problem
	 * @param user
	 * @param paper_name 
	 */
	public void update(Problem problem, User user)
	{
		if(null==problem.get("id"))
		{
			problem.set("sign", 1);
			problem.set("creater", user.get("id"));
			problem.set("createtime", new Date());
			problem.set("source", "由用户"+user.getStr("fullname")+"添加入题库");
		}
		save(problem);
	}
	
	/**
	 * 试卷管理中添加试题，默认不加入题库
	 * @param problem
	 * @param user
	 */
	public void update2(Problem problem, User user, String paper_name)
	{
		if(null==problem.get("id"))
		{
			problem.set("sign", 0);
			problem.set("creater", user.get("id"));
			problem.set("createtime", new Date());
			problem.set("source",paper_name);
		}
		save(problem);
	}

	public void activation(String id)
	{
		Problem problem=findById(id);
		problem.set("sign", 1);
		save(problem);
	}
	
	public void freeze(String id)
	{
		Problem problem=findById(id);
		problem.set("sign", 0);
		save(problem);
	}

	
	public Problem_select findProblem_selectByPro_id(String pro_id)
	{
		return Problem_select.dao.findFirst("select * from problem_select where pro_id=?",pro_id);
	}

	public Problem_judge findProblem_judgeByPro_id(String pro_id)
	{
		return Problem_judge.dao.findFirst("select * from problem_judge where pro_id=?",pro_id);
	}

	public void save_problem_select(Problem_select problem_select)
	{
		if(null==problem_select.get("id"))
		{
			problem_select.save();
		}
		else
		{
			problem_select.update();
		}
	}
	
	public void update_problem_select(Problem_select problem_select, String pro_id)
	{
		problem_select.set("pro_id", pro_id);
		save_problem_select(problem_select);
	}
	
	public void save_problem_judge(Problem_judge problem_judge)
	{
		if(null==problem_judge.get("id"))
		{
			problem_judge.save();
		}
		else
		{
			problem_judge.update();
		}
	}

	public void update_problem_judge(Problem_judge problem_judge,String pro_id)
	{
		problem_judge.set("pro_id", pro_id);
		save_problem_judge(problem_judge);
	}

	

	

}
