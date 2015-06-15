package cn.jxust.paper.service;

import java.util.Date;
import java.util.List;

import cn.jxust.commen.model.User;
import cn.jxust.paper.model.Paper;
import cn.jxust.paper.model.Paper_problem;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

public class PaperService
{

	public Page<Paper> findPaperPage(Integer pageNumber,Integer pageSize)
	{
		return Paper.dao.paginate(pageNumber, pageSize, "select paper.*,user.fullname", "from paper,user where paper.creater=user.id order by createtime desc");
	}

	public Paper getPaperById(String id)
	{
		return Paper.dao.findById(id);
	}

	public void save(Paper paper)
	{
		if(null==paper.get("id"))
		{
			paper.save();
		}
		else
		{
			paper.update();
		}
	}
	
	public void update(Paper paper,User user)
	{
		if(null==paper.get("id"))
		{
			paper.set("createtime", new Date());
			paper.set("creater", user.get("id"));
		}
		save(paper);
	}
	
	public void deletePaper_problemByPaper_id(String paper_id)
	{
		/*
		List<Paper_problem> paper_problems=Paper_problem.dao.find("select * from paper_problem where paper_id=?",paper_id);
		for(Paper_problem paper_problem:paper_problems)
		{
			paper_problem.delete();
		}
		*/
		Db.update("delete from paper_problem where paper_id=?",paper_id);
	}
	 
	public void deletePaperById(String id)
	{
		deletePaper_problemByPaper_id(id);
		Paper.dao.deleteById(id);
	}

	/**
	 * 查找本试卷的所有试卷信息，及试题的对应分数
	 */
	public List<Paper_problem> findPaper_problemByPaper_id(String paper_id)
	{
		return Paper_problem.dao.find("select paper_problem.value,problem.*,paper_problem.id as pp_id from paper_problem,problem where paper_id=? and paper_problem.problem_id=problem.id order by problem.type asc,problem.createtime desc", paper_id);
	}

	public Paper_problem getPaper_problemByPaper_problem_id(String parper_problem_id)
	{
		return Paper_problem.dao.findById(parper_problem_id);
	}

	public void deletePaper_problemByPaper_problem_id(String paper_problem_id)
	{
		Paper_problem.dao.deleteById(paper_problem_id);
	}

	public void save_paper_problem(Paper_problem paper_problem)
	{
		if(null!=paper_problem.get("id"))
		{
			paper_problem.update();
		}
		else
		{
			paper_problem.save();
		}
	}
	
}
